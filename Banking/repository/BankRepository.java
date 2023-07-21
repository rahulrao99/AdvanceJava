package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Bank;

public interface BankRepository extends JpaRepository<Bank,Long>{

}
