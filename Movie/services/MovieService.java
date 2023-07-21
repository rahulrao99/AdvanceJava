package com.app.service;

import java.util.List;

import com.app.dto.ApiResponseDto;
import com.app.dto.AuthRequestDto;
import com.app.dto.AuthResponseDto;
import com.app.entities.Movie;

public interface MovieService {

	List<AuthResponseDto>getAllMovies();
	ApiResponseDto addMovie(AuthRequestDto movie);
	ApiResponseDto deleteMovieByNo(Long mov);
	
	ApiResponseDto updateMovie(Movie updatedMovie);
	List<AuthResponseDto> getByName(String name);
}
