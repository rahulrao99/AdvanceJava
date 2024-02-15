package com.app.entity;

import org.hibernate.annotations.Collate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class Product extends BaseEntity{

	@Column
	private String pname;;
	@Column
	private String brand;
	@Column
	private String seller;
	@Column
	private double price;
	@Column 
	private int quantity;
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Product(String pname, String brand, String seller, double price, int quantity) {
		super();
		this.pname = pname;
		this.brand = brand;
		this.seller = seller;
		this.price = price;
		this.quantity = quantity;
	}
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Product [pname=" + pname + ", brand=" + brand + ", seller=" + seller + ", price=" + price
				+ ", quantity=" + quantity + "]";
	}
	
	
}
