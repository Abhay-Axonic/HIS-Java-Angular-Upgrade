package com.param.procurement.common.exception;

/**
 * @author Nilesh
 */
public class CustomException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	public CustomException(String message) {
		this.message = message;
	}

	public CustomException(Throwable cause) {
		String msg = cause.getMessage();

		if (msg == null) {
			this.message = cause.getClass().getName();
		}
	}

	public String getMessage() {
		return message;
	}

}
