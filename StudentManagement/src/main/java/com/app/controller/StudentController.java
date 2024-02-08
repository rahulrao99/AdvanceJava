package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entities.Student;
import com.app.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentService stdSer;
	
	@GetMapping("/getstd")
	public List<Student> getAllStudents(){
		return stdSer.getAllStudentds();
	}
	
	@PostMapping("/addstd")
	public Student addStudent(@RequestBody Student std) {
		return stdSer.addStudent(std);
	}
	
	@GetMapping("/getstd/{id}")
	public Student getStudentById(@PathVariable Long id) {
		return stdSer.getStudentById(id);
	}
	
	@PutMapping("/updatestd/{id}")
	public Student updateStudent(@RequestBody Student std, Long id) {
		return stdSer.updateStudent(std,id);
	}
	
	@DeleteMapping("/delstd/{id}")
	public String deleteStudent(@PathVariable Long id) {
		return stdSer.deleteStudentById(id);
	}
	
}
