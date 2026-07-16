package com.param.global.billing.controller;

import java.io.Serial;

public class BillingException extends RuntimeException{
	@Serial
	private static final long serialVersionUID = -4463339409632912953L;
	private String message;
	public BillingException(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
