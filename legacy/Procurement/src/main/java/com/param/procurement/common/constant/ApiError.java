package com.param.procurement.common.constant;

public enum ApiError {

	SUCCESS(200,"SUCCESS"),
	APPLICATION_ERROR(400, "APPLICATION_ERROR"),
	//Client SIed Error 
	UNAUTHORISED_USER(401, "UNAUTHORISED_USER"),
	FORBIDDEN(403, "FORBIDDEN"),
	EXCEPTION(1000, "EXCEPTION"),
	HIBERNATE_EXCEPTION(1001 , "HIBERNATE_ERROR");
	
	
	private int Code;
	private String message;
	private String summary;
	private String stackTrace;
	
	
	private ApiError(int Code, String message) {
		this.Code = Code;
		this.message = message;
		//this.summary = summary;
		//this.stackTrace = stackTrace
	}

	public int getCode() {
		return Code;
	}

	 public void setCode(int Code) {
		this.Code = Code;
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
