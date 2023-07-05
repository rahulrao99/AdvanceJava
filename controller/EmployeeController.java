package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Employee;
import com.app.service.EmployeeService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
	
	@Autowired
	private EmployeeService empService;
	
	public EmployeeController() {
		System.out.println("in def ctor of " + getClass());
	}
	
	@GetMapping
	public List<Employee> listEmps(){
		System.out.println("in list emps");
		return empService.getAllEmployees();
	}
	
	@PostMapping
	public ResponseEntity<?> saveEmpDetails(@RequestBody Employee transientEmp){
		System.out.println("in save " + transientEmp);
		return new ResponseEntity<>(empService.addEmpDetails(transientEmp),HttpStatus.CREATED);
	}
	
	@GetMapping("/{empId}")
	public Employee getEmpDetails(@PathVariable Long empId) {
		System.out.println("in get emp dtls " + empId);
		return empService.getEmpDetails(empId);
	}
	
	@PutMapping
	public Employee updateEmpDetails(@RequestBody Employee detachedEmp) {
		System.out.println("in update emp " + detachedEmp);
		return empService.updateEmpDetails(detachedEmp);
	}
	
}













