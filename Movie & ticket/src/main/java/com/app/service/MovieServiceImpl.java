package com.app.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.app.enitites.Movie;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.MovieRepository;

@Service
@Transactional
public class MovieServiceImpl implements MovieService{
	
	@Autowired
	private MovieRepository mRepo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public List<Movie> getAllMovies() {
		// TODO Auto-generated method stub
		return mRepo.findAll();
	}

	@Override
	public Movie addMovieDetails(Movie transientMovie) {
		// TODO Auto-generated method stub
		return mRepo.save(transientMovie);
	}

	@Override
	public Movie updateMovieDetails(Movie transientMovie) {
		// TODO Auto-generated method stub
		return mRepo.save(transientMovie);
	}

	@Override
	public String deleteMovieDetails(Long mid) {
		// TODO Auto-generated method stub
		String msg="movie id invalid";
		if(mRepo.existsById(mid)) {
			mRepo.deleteById(mid);
			msg="mid deleted"+mid+"deleted..!!";
		}
		return null;
	}

	@Override
	public Movie getMovieDetails(Long mid) {
		// TODO Auto-generated method stub
		return mRepo.findById(mid)
					.orElseThrow(()->new ResourceNotFoundException("invalid mid"));
	}
}
