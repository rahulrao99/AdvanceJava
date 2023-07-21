package com.app.custom_exception;

public class BankHandlingException extends RuntimeException{
	public BankHandlingException(String mesg) {
		super(mesg);
	}

}
