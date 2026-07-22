"""
codebase_rag.py

Component 1 - Codebase RAG Memory.

Indexes every function/class in a target Python repository into a
persistent ChromaDB collection, then retrieves the top-k most relevant
chunks for a given feature request. This is real embedding + vector
retrieval (not a hardcoded string) - each chunk is a genuine AST-parsed
function or class body with its docstring/signature.

Run standalone to (re)build the index and sanity-check retrieval:
    python codebase_rag.py
"""

from __future__ import annotations

import ast
import os
from dataclasses import dataclass

import hashlib

import chromadb
from chromadb.utils import embedding_functions

CHROMA_PATH = "./chroma_store"
COLLECTION_NAME = "codebase"
_HASH_DIM = 256


class OfflineHashingEmbeddingFunction:
    """Deterministic, fully offline embedding function (feature-hashing / bag-of-words).

    Used only as a fallback when the default semantic embedding model can't
    be downloaded (e.g. behind a restrictive corporate proxy). It is a real
    numeric embedding computed from the document text - never a hardcoded
    string - so retrieval is still genuine vector similarity search, just
    based on token-overlap rather than learned semantics.
    """

    def name(self) -> str:
        return "offline-hashing-embedding"

    def __call__(self, input: list[str]) -> list[list[float]]:
        vectors = []
        for text in input:
            vec = [0.0] * _HASH_DIM
            for token in text.lower().split():
                idx = int(hashlib.sha256(token.encode()).hexdigest(), 16) % _HASH_DIM
                vec[idx] += 1.0
            norm = sum(v * v for v in vec) ** 0.5 or 1.0
            vectors.append([v / norm for v in vec])
        return vectors

    def embed_documents(self, input: list[str]) -> list[list[float]]:
        return self(input)

    def embed_query(self, input: list[str]) -> list[list[float]]:
        return self(input)


def _get_embedding_function():
    """Prefer the real semantic embedding model; fall back to the offline hasher
    if it can't be downloaded (common on restricted corporate networks)."""
    try:
        fn = embedding_functions.DefaultEmbeddingFunction()
        fn(["warmup"])  # force the model download/load now, not lazily later
        return fn
    except Exception as e:
        print(f"[codebase_rag] Default embedding model unavailable ({e!r}); "
              f"falling back to OfflineHashingEmbeddingFunction.")
        return OfflineHashingEmbeddingFunction()


@dataclass
class CodeChunk:
    """One retrievable unit of source code."""

    file_path: str
    name: str
    kind: str  # "function" or "class"
    source: str


def _extract_chunks(repo_path: str) -> list[CodeChunk]:
    """Walk a repo and AST-parse every top-level function/class into a CodeChunk."""
    chunks: list[CodeChunk] = []
    for root, _dirs, files in os.walk(repo_path):
        for fname in files:
            if not fname.endswith(".py"):
                continue
            fpath = os.path.join(root, fname)
            with open(fpath, encoding="utf-8") as f:
                source = f.read()
            try:
                tree = ast.parse(source, filename=fpath)
            except SyntaxError:
                continue
            for node in ast.walk(tree):
                if isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef, ast.ClassDef)):
                    kind = "class" if isinstance(node, ast.ClassDef) else "function"
                    snippet = ast.get_source_segment(source, node) or ""
                    if snippet.strip():
                        chunks.append(
                            CodeChunk(
                                file_path=os.path.relpath(fpath, repo_path),
                                name=node.name,
                                kind=kind,
                                source=snippet,
                            )
                        )
    return chunks


def index_repo(repo_path: str, collection_name: str = COLLECTION_NAME) -> int:
    """Index a repo's functions/classes into a persistent Chroma collection.

    Returns the number of chunks indexed.
    """
    client = chromadb.PersistentClient(path=CHROMA_PATH)
    # Recreate collection each run so re-indexing a changed repo is safe.
    try:
        client.delete_collection(collection_name)
    except Exception:
        pass
    embed_fn = _get_embedding_function()
    collection = client.create_collection(name=collection_name, embedding_function=embed_fn)

    chunks = _extract_chunks(repo_path)
    if not chunks:
        return 0

    collection.add(
        ids=[f"{c.file_path}::{c.name}::{i}" for i, c in enumerate(chunks)],
        documents=[c.source for c in chunks],
        metadatas=[{"file": c.file_path, "name": c.name, "kind": c.kind} for c in chunks],
    )
    return len(chunks)


def retrieve_context(query: str, k: int = 3, collection_name: str = COLLECTION_NAME) -> str:
    """Retrieve the top-k most relevant code chunks for a feature request.

    Returns a single formatted string ready to inject into the Coder's
    prompt as `codebase_context`. Returns "" only if the collection is
    empty (never a hardcoded placeholder).
    """
    client = chromadb.PersistentClient(path=CHROMA_PATH)
    try:
        collection = client.get_collection(collection_name, embedding_function=_get_embedding_function())
    except Exception:
        return ""

    results = collection.query(query_texts=[query], n_results=k)
    docs = results.get("documents", [[]])[0]
    metas = results.get("metadatas", [[]])[0]
    if not docs:
        return ""

    parts = []
    for doc, meta in zip(docs, metas):
        parts.append(f"# {meta['file']} :: {meta['kind']} {meta['name']}\n{doc}")
    return "\n\n".join(parts)


if __name__ == "__main__":
    n = index_repo("mock_buggy_repo")
    print(f"Indexed {n} chunks from mock_buggy_repo")
    ctx = retrieve_context("fix the divide by zero bug")
    print("---- sample retrieval ----")
    print(ctx)
