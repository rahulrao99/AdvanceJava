package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Product;
import com.app.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService prdSer;
	
	@GetMapping("/getprd")
	public List<Product> getAllProduct(){
	return prdSer.getAllProduct();
	}
	
	@PutMapping("/updateprd/{id}")
	public String updateProduct(@PathVariable Long id,@RequestBody Product prd ) {
		prdSer.updateProduct(id, prd);
		return "updated...";
	}
	
	@PostMapping("/addprd")
	public Product addProduct(@RequestBody Product prd){
		return prdSer.addProduct(prd);
	}
	
	@GetMapping("/getprd/{id}")
	public Product getProductById(@PathVariable Long id) {
		return prdSer.getProductById(id);
	}
	
	@DeleteMapping("/deleteprd/{id}")
	public String deleteProductById(@PathVariable Long id) {
		return prdSer.deleteProductById(id);
	}
	
}
