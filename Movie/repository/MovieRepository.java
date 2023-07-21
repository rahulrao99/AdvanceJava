package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	List<Movie> findByMovieName(String name);

}
