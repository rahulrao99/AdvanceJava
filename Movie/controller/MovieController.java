
package com.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AuthRequestDto;
import com.app.dto.AuthResponseDto;
import com.app.entities.Movie;
import com.app.service.MovieService;



@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {

	@Autowired
	private MovieService movieServ;
	
	@GetMapping
		public ResponseEntity<?> allAllMovies() {
		return ResponseEntity.status(HttpStatus.OK).body(movieServ.getAllMovies());
	}
	
	@PostMapping("/add")
	public ResponseEntity<?>addNewMovie(@RequestBody @Valid AuthRequestDto movie){
		return ResponseEntity.status(HttpStatus.CREATED).body(movieServ.addMovie(movie));
	}
	
	@DeleteMapping("/{movieNo}")
	public ResponseEntity<?> deleteMovie(@RequestParam Long movieNo) {
		return ResponseEntity.status(HttpStatus.OK).body(movieServ.deleteMovieByNo(movieNo));
	}
	
	@PutMapping("/movieNo")
    public ResponseEntity<?> updateMovie(@RequestBody Movie detachedMovie){
         return ResponseEntity.ok(movieServ.updateMovie(detachedMovie));
    }
	
	 @GetMapping("/byname")
   public ResponseEntity<?> getMoviesByName(@RequestParam String name) {
		 	return ResponseEntity.status(HttpStatus.FOUND).body(movieServ.getByName(name));
		 
   }
	
}
//@RestController
//@RequestMapping("/movies")
//public class MovieController {
//
//    @Autowired
//    private MovieRepository movieRepository;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED) 
//    public Movie addMovie(@Valid @RequestBody Movie movie) {
//        return movieRepository.save(movie);
//    }
//
//    @PutMapping("/{movieNo}")
//    public ResponseEntity<Movie> updateMovie(
//            @PathVariable Long movieNo,
//            @Valid @RequestBody Movie updatedMovie) {
//        Optional<Movie> optionalMovie = movieRepository.findById(movieNo);
//        if (optionalMovie.isPresent()) {
//            Movie movie = optionalMovie.get();
//            movie.setReleaseDate(updatedMovie.getReleaseDate());
//            movie.setBudget(updatedMovie.getBudget());
//            return ResponseEntity.ok(movieRepository.save(movie));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping(params = "name")
//    public ResponseEntity<List<Movie>> getMoviesByName(@RequestParam String name) {
//        List<Movie> movies = movieRepository.findByName(name);
//        if (!movies.isEmpty()) {
//            return ResponseEntity.ok(movies);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{movieNo}")
//    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieNo) {
//        Optional<Movie> optionalMovie = movieRepository.findById(movieNo);
//        if (optionalMovie.isPresent()) {
//            movieRepository.delete(optionalMovie.get());
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
