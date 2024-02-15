package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Product;
import com.app.exception.ProductException;
import com.app.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository prdRepo;
	
	
	@Override
	public List<Product> getAllProduct() {
		// TODO Auto-generated method stub
		return prdRepo.findAll();
	}

	@Override
	public Product addProduct(Product prd) {
		// TODO Auto-generated method stub
		return prdRepo.save(prd);
	}

//	@Override
//	public String updateProduct(Long id, String prd) {
//		// TODO Auto-generated method stub
//		prdRepo.save(prd);
//		return "updated sucssfully....";
//				
//	}

	@Override
	public Product getProductById(Long id) {
		// TODO Auto-generated method stub
		return prdRepo.findById(id).orElseThrow(()->new ProductException("id not found"));
	}

	@Override
	public String deleteProductById(Long id) {
		String msg="Product deleted Successfully...";
		if(prdRepo.existsById(id)) {
			prdRepo.deleteById(id);
			System.out.println("deleted by id :"+id);
		}
		
		return msg;
	}

	@Override
	public String updateProduct(Long id, Product prd) {
		// TODO Auto-generated method stub
		prdRepo.save(prd);
		return "Updated Sucessfully,...";
	}

}
