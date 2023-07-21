package com.app.exception_handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import com.app.custom_exceptions.ResourceNotFoundException;
//import com.app.dto.ApiResponse;


@RestControllerAdvice // mandatory !
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		
		Map<String, String> map = new HashMap<>();
		for (FieldError f : e.getFieldErrors())
			map.put(f.getField(), f.getDefaultMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		
	}

	// add another exc handling method to handle Resource not found exc
//	@ExceptionHandler(ResourceNotFoundException.class)
//	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
//		System.out.println("handle res not found "+e);
//		return ResponseEntity
//				.status(HttpStatus.NOT_FOUND)
//				.body(new ApiResponse(e.getMessage()));		
//	}


}
