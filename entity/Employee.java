package com.app.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="new_emp")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude="password")
public class Employee extends BaseEntity{
	@Column(name="first_name",length=20)
	private String firstName;
	@Column(name="last_name",length=20)
	private String lastName;
	@Column(length=30,unique=true)
	private String email;
	@Column(length=20,nullable=false)
	private String password;
	@Column(name="join_date")
	private LocalDate joinDate;
	private double salary;
	@Column(length=30)
	private String location;
	@Column(length=30)
	private String department;
	
}
