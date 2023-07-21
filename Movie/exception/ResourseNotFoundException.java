package com.app.custome_exception;


@SuppressWarnings("serial")
public class ResourseNotFoundException extends RuntimeException{

	public ResourseNotFoundException(String msg) {
					super(msg);
	}
}
