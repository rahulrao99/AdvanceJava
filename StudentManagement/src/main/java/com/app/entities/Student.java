package com.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="studd")
@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@ToString

public class Student extends BaseEntity {

	@Column(length = 20)
	private String fname;
	@Column(length = 20)
	private String lname;
	@Column(length = 20)
	private String city;
	@Column(length = 20)
	private int age;
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public Student() {
    }
	
	public Student(String fname, String lname, String city, int age) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.city = city;
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "Student [fname=" + fname + ", lname=" + lname + ", city=" + city + ", age=" + age + "]";
	}
	
	
}
