package com.app.service;

import java.util.List;

import com.app.entities.Student;

public interface StudentService {

	List<Student> getAllStudentds();
	
	Student addStudent(Student std);
	
	Student getStudentById(Long id);
	
	Student updateStudent(Student std, Long id);
	
	String deleteStudentById(Long id);
	
}
