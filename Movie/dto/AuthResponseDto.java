package com.app.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

import com.app.entities.MovieCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {
@NotBlank
	private String movieName;

	private MovieCategory category;
	@Past
	private LocalDate releaseDate;
	@Positive
	private double budget;
	private String director;
}
