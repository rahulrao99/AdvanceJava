package com.app.entities;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="employee")
public class Employee extends BaseEntity{

	@Column
	private String fname;
	@Column
	private String lname;
	@Column
	private String email;
	@Column
	private String password;
	private double salary;
	@Column
	private String city;
	
}
