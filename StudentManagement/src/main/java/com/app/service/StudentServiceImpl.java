package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entities.Student;
import com.app.exception.StudentExceptionHandling;
import com.app.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{

	@Autowired
	private StudentRepository stdRepo;
	
	
	@Override
	public List<Student> getAllStudentds() {
		// TODO Auto-generated method stub
		return stdRepo.findAll();
	}

	@Override
	public Student addStudent(Student std) {
		// TODO Auto-generated method stub
		return stdRepo.save(std);
	}

	@Override
	public Student getStudentById(Long id) {
		// TODO Auto-generated method stub
		return stdRepo.findById(id).orElseThrow(()-> new StudentExceptionHandling("id not found") );
	}

	@Override
	public Student updateStudent(Student std, Long id) {
		// TODO Auto-generated method stub
		return stdRepo.save(std);
	}

	@Override
	public String deleteStudentById(Long id) {
		// TODO Auto-generated method stub
		String msg="Student deleted successfully";
		if(stdRepo.existsById(id)) {
			stdRepo.deleteById(id);
		}
		return msg;
	}

	//@Override
//	public Student updateStudent(Student std, Long id) {
//		// TODO Auto-generated method stub
//		Student stud = stdRepo.findById(id).orElseThrow(()-> new StudentExceptionHandling("id not found"));
//		stud.setfname
//		return null;
//	}

}
