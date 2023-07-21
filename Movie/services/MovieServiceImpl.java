package com.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dto.ApiResponseDto;
import com.app.dto.AuthRequestDto;
import com.app.dto.AuthResponseDto;
import com.app.entities.Movie;
import com.app.repository.MovieRepository;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public List<AuthResponseDto> getAllMovies() {
		List<Movie> movieList= movieRepo.findAll();
		List<AuthResponseDto> movie=new ArrayList<AuthResponseDto>();
		movieList.forEach((v)->{movie.add(mapper.map(v, AuthResponseDto.class));
		});
		return movie;
	}
	@Override
	
	public ApiResponseDto addMovie( AuthRequestDto movie) {
		Movie mov=new Movie(movie.getMovieName(), movie.getCategory(),
				movie.getReleaseDate(), movie.getBudget(), movie.getDirector());
		movieRepo.save(mov);
		return new ApiResponseDto("movie added");
		
	}
	@Override
	public ApiResponseDto deleteMovieByNo(Long empid) {
		if(!movieRepo.existsById(empid))
			return new ApiResponseDto("Emp id is invalid");
		movieRepo.deleteById(empid);
		return new ApiResponseDto("Emp with id"+empid+" deleted .!");
	}
	
	
	
	 public ApiResponseDto updateMovie(Movie movie) {
			movieRepo.save(movie);
			return new ApiResponseDto("Movie Updated Successfully");
	 }
	@Override
	public List<AuthResponseDto> getByName(String name) {
		
				List<Movie> movie=movieRepo.findByMovieName(name);
				List<AuthResponseDto>amovie=new ArrayList<AuthResponseDto>();
				movie.forEach((v)->{amovie.add(mapper.map(v, AuthResponseDto.class));
				});
				return amovie;
	}
	

}
