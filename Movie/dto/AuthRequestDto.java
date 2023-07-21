package com.app.dto;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.app.entities.MovieCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AuthRequestDto {

	@NotBlank(message = "Movie name cannot be blank..!!")
	private String movieName;
	private MovieCategory category;
	@Future(message = "release date must be in future..!!")
	private LocalDate releaseDate;
	@Positive(message = "must be positive")
	private double budget;
	@NotBlank(message = "Director name cannot be blank..!!")
	private String director;
}
