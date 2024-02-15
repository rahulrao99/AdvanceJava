package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.Product;

@Service

public interface ProductService {

	List <Product> getAllProduct();
	
	Product addProduct(Product prd);
	
	String updateProduct(Long id, Product prd);
	
	Product getProductById(Long id);
	
	String deleteProductById(Long id);
	
}
