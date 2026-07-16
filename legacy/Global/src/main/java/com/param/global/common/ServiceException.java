package com.param.global.common;

import java.io.Serial;

public class ServiceException extends RuntimeException{
	@Serial
	private static final long serialVersionUID = -4463339409632912953L;
	private String message;
	public ServiceException(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}