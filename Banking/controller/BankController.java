package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entities.Bank;
import com.app.service.BankService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/banks")

public class BankController {
	@Autowired
	
	private BankService bankser;
	
	@GetMapping
	
	public List<Bank>getAllAccount(){
		
		return bankser.getAllAccount();
	}
	
	@PostMapping
	
	public String addBankAccount(@RequestBody Bank b) {
		return bankser.addBankAccount(b);
	}
	
	@GetMapping("/{id}")
	Bank getAccountById(Long id) {
		return bankser.getAccountById(id);
	}
	
	@DeleteMapping("/{id}")
	String deleteAccountById(Long id) {
		return bankser.deleteAccountById(id);
	}
	
	@PutMapping ("/{id}")
	Bank updateAccount (@RequestBody Bank bt) {
		return bankser.updateAccount(bt);
	}
	
	
	

}
