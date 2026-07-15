package com.param.data.dto;

public class ExceptionDto {

	private String exceptionMessage;
	
	private String methodName;
	
	private String className;
	
	private String unitIds;

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(String unitIds) {
		this.unitIds = unitIds;
	}
	
	
}
