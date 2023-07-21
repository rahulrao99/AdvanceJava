package com.app.service;

import java.util.List;

import com.app.entities.Bank;



public interface BankService {
	List<Bank>getAllAccount();
	String addBankAccount(Bank b);
	Bank getAccountById(Long id);
	String deleteAccountById(Long id);
	
	Bank updateAccount (Bank bt);
	

}
