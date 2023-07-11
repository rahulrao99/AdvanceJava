package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.enitites.Movie;

public interface MovieRepository extends  JpaRepository<Movie, Long>{
	
	
}
