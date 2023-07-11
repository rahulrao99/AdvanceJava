package com.app.service;

import java.util.List;

import com.app.enitites.Movie;

public interface MovieService {
	List<Movie> getAllMovies();
	
	Movie addMovieDetails(Movie transientMovie);
	
	Movie updateMovieDetails(Movie transientMovie);
	
	String deleteMovieDetails(Long mid);
	
	Movie getMovieDetails(Long mid);
	
	
}
