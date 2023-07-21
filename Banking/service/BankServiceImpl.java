package com.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.custom_exception.BankHandlingException;
import com.app.entities.Bank;
import com.app.repository.BankRepository;



@Service
@Transactional
public class BankServiceImpl implements BankService{
	@Autowired
	
	private BankRepository bankRepo;
	@Override
	
	public List<Bank>getAllAccount(){
		System.out.println("in the list of bank account");
		return bankRepo.findAll();
		} 
	//
	@Override
	public Bank getAccountById(Long id) {
		// TODO Auto-generated method stub
		return bankRepo.findById(id).orElseThrow(()-> new BankHandlingException("id not found"));
	}
	@Override
	public String deleteAccountById(Long id) {
		String msg="Account Deleted Succesfully";
		if(bankRepo.existsById(id)) {
			bankRepo.deleteById(id);
		}
		return msg;
	}
	@Override
	public Bank updateAccount(Bank bt) {
		// TODO Auto-generated method stub
		return bankRepo.save(bt);
	}
	@Override
	public String addBankAccount(Bank b) {
		 bankRepo.save(b);
		return  "ADDED";
	}

}
