package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.entities.Employee;
import com.app.exception.EmployeeException;
import com.app.repository.EmpRepo;

public class EmployeeSerImpl implements EmployeeService{

	@Autowired
	
	private EmpRepo empRepo;
	
	@Override
	public List<Employee> getAllDetails() {
		// TODO Auto-generated method stub
		return empRepo.findAll();
	}

	@Override
	public Employee addEmployee(Employee emp) {
		// TODO Auto-generated method stub
		return empRepo.save(emp);
	}

	@Override
	public String deleteById(Long id) {
		// TODO Auto-generated method stub
		String msg="Employee deleted sucessfully";
		if(empRepo.existsById(id)) {
			empRepo.deleteById(id);
			System.out.println("Deleted by id :"+id);
		}
		return msg;
	}

	@Override
	public Employee updateEmployee(Employee emps) {
		// TODO Auto-generated method stub
		return empRepo.save(emps);
	}

	@Override
	public Employee getEmployeeById(Long id) {
		// TODO Auto-generated method stub
		return empRepo.findById(id).orElseThrow(()->new EmployeeException("Enter valid emp id"));
	}

}
