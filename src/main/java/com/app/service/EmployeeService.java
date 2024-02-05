package com.app.service;

import java.util.List;

import com.app.entities.Employee;

public interface EmployeeService {

	List<Employee>getAllDetails();
	Employee addEmployee(Employee emp);
	String deleteById(Long id);
	Employee updateEmployee(Employee emps);
	Employee getEmployeeById(Long id);
	
}
