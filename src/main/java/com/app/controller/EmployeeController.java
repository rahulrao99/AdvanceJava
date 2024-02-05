package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entities.Employee;
import com.app.service.EmployeeSerImpl;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {

	@Autowired
	private EmployeeSerImpl empServ;
	
	@GetMapping
	public List<Employee> getEmployee(){
		System.out.println("In list Employee Service");
		return empServ.getAllDetails();
		
	}
	
	@PostMapping
	public ResponseEntity<Employee> saveEmpDetails(@RequestBody Employee emp){
		System.out.println("In add emp details controller....");
		return new ResponseEntity<>(empServ.addEmployee(emp),HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public String deleteById(@PathVariable Long id) {
		System.out.println("in delete employee service method....");
		return empServ.deleteById(id);
		
	}
	
	@PutMapping("/{id}")
	public Employee updateEmployee(@RequestBody Employee emps) {
		System.out.println("in update controller method");
		return empServ.updateEmployee(emps);
	}
	
	@GetMapping("/{id}")
	public Employee getEmployeeById(Long id) {
		return empServ.getEmployeeById(id);
		
	}
	
}
