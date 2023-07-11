package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.enitites.Movie;
import com.app.service.MovieService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin(origins="http:localhost:3000")
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	private MovieService mService;
	
	public MovieController() {
		System.out.println("in def ctor " +getClass());
	}
		@GetMapping
	public List<Movie> listMovies() {
		System.out.println("in list movie");
			return mService.getAllMovies();
	}

	@PostMapping
	public ResponseEntity<?> saveMovieDetails(@RequestBody Movie transientMovie){
		System.out.println("in save " +transientMovie);
		return new ResponseEntity<>(mService.addMovieDetails(transientMovie),HttpStatus.CREATED);
	}
	
	@PutMapping
	public Movie updateMovieDetails(Movie transientMovie) {
		System.out.println("in save "+ transientMovie);
		return mService.updateMovieDetails(transientMovie);
	}

	@DeleteMapping("/{mid}")
	public String deleteMovieDetails(@PathVariable Long mid) {
		System.out.println("in del " +mid);
		String msg="deleted";
		 mService.deleteMovieDetails(mid);
		 return msg;
	} 

//	public Movie getMovieDetails(Long mid) {
//		return (mService.getMovieDetails(mid));
//	}
	
	
}
