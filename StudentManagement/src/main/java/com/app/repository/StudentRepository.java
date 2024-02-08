package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entities.Student;


public interface StudentRepository extends JpaRepository<Student,Long>{

}
