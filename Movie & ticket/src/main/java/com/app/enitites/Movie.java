package com.app.enitites;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString(exclude="tlist")
//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreType
public class Movie extends BaseEntity{

	public String getName() {
		return mName;
	}


	public void setName(String mname) {
		mName = mname;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getRating() {
		return rating;
	}


	public void setRating(int rating) {
		this.rating = rating;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public List<Ticket> getTlist() {
		return tlist;
	}


	public void setTlist(List<Ticket> tlist) {
		this.tlist = tlist;
	}


	@Column(name="movie_name",length=20)
	private String mName;
	@Column(name="price")
	private double price;
	
	private int rating;
	@Column(name="relese_date")
	private LocalDate date;
	
	@OneToMany(mappedBy="movie",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
	private List<Ticket> tlist=new ArrayList<>();
	
	public boolean addticket(Ticket t) {
		tlist.add(t);
		t.setMovie(this);
		return true;
	}
	
	
	public boolean removeticket(Ticket t) {
		tlist.remove(t);
		t.setMovie(null);
		return true;
	}
	
	
	
	
	
	
	
}
