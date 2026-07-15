package com.param.common.dto;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
 

public class ErrorResponse {
	protected String code;

	protected String message;

	protected String summary;

	protected String stackTrace;
	List<CustomException> errors;

	 

	public List<CustomException> getErrors() {
		return errors;
	}
	public void setErrors(List<CustomException> errors) {
		this.errors = errors;
	}
	public ErrorResponse(String code, String message, Exception ex) {
		super();

		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		
		this.code = code;
		this.message = message;
		this.summary = ex.getMessage();
		this.stackTrace = writer.toString(); 
	}
	public ErrorResponse(String code, String message, com.param.common.dto.CustomException ex ) {
		super();

		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		
		this.code = code;
		this.message = message;
		this.summary = ex.getMessage();
		this.stackTrace = writer.toString(); 
		this.errors=ex.getErrors();
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
}