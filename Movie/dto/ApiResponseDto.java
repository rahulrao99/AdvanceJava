package com.app.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto {

	private String message;
	private LocalDateTime timestamp;
	
	public ApiResponseDto(String message) {
		this.message=message;
		this.timestamp=LocalDateTime.now();
	}
}
