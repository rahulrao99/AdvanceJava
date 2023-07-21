package com.app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bank")

public class Bank extends BaseEntity {
	@Column(length=20)
	private String fname;
	@Column(length=20)
	private String lname;
	private int acctno;
	private double balance;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	@NotEmpty
	private String password;
	@Enumerated(EnumType.STRING)
	private BankType btype;
	

}
