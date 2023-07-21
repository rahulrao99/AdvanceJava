package com.app.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "movies")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends BaseEntity {

	@Column(name = "name",length = 20)
	private String movieName;
	@Enumerated(EnumType.STRING)
	private MovieCategory category;
	@Column(name = "releasedate",length = 20)
	private LocalDate releaseDate;
	private double budget;
	@Column(length = 20)
	private String director;
}
