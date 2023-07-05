package com.app.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import com.app.dto.AuthRequestDTO;
import com.app.dto.AuthRespDTO;
import com.app.entity.Employee;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.EmployeeRepository;

import io.swagger.v3.oas.annotations.servers.Server;

@Server
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository empRepo;
	
	@Override
	public List<Employee> getAllEmployees(){
		return empRepo.findAll();
		
	}
	
	@Override
	public Employee addEmpDetails(Employee transientEmp) {
		return empRepo.save(transientEmp);
	}
	@Override
	public String deleteEmpDetails(Long empId) {
		String mesg = "Emp id invalid";
		if(empRepo.existsById(empId)) {
			empRepo.deleteById(empId);
			mesg="emp with ID=" +empId + " deleted !";
		}
		return mesg;
	}
	
	@Override
	public Employee getEmpDetails(Long empId) {
		return empRepo.findById(empId)
				.orElseThrow(()-> new ResourceNotFoundException("Invalid Emp ID , Can't get emp details!!!!"));
	}
	
	@Override
	public Employee updateEmpDetails(Employee detachedEmp) {
		return empRepo.save(detachedEmp);
	}
	
	@Override
	public AuthRespDTO authenticateEmp(AuthRequestDTO request) {
		Employee emp = empRepo.findByEmailAndPassword(request.getEmail(),request.getPassword())
				.orElseThrow(()->new ResourceNotFoundException("Emp not found : Invalid Email or password"));
				
		AuthRespDTO authRespDTO = mapper.map(emp, AuthRespDTO.class);
		return authRespDTO;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
