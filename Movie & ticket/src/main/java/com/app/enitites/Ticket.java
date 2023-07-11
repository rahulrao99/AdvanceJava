package com.app.enitites;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreType
@ToString
public class Ticket extends BaseEntity{
@Column(length=20,nullable=false)
	public String name;
@Column(length=20,nullable=false)
	private LocalDate pdate;

@ManyToOne
@JoinColumn(name="movie")
private Movie movie;

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public LocalDate getPdate() {
	return pdate;
}

public void setPdate(LocalDate pdate) {
	this.pdate = pdate;
}

public Movie getMovie() {
	return movie;
}

public void setMovie(Movie movie) {
	this.movie = movie;
}



	
	
	
	
}
