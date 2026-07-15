package com.param.lis.unit.dto;

public class LabMachineInterfaceDto {
	
	private Integer interfaceId;

	private String sampleNo;
	
	private String parameterName;
	
	private String parameterValue;
	
	private String parameterUnit;
	
	private double minValue;
	
	private double maxValue;
	
	private double highValue;
	
	private double lowValue;

	private String paramResult;
	
	private String note;
	
	private char status;

	private String createdDate;
	
	private String machineName;
	
	private String resultDateTime;

	public Integer getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getParameterUnit() {
		return parameterUnit;
	}

	public void setParameterUnit(String parameterUnit) {
		this.parameterUnit = parameterUnit;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getHighValue() {
		return highValue;
	}

	public void setHighValue(double highValue) {
		this.highValue = highValue;
	}

	public double getLowValue() {
		return lowValue;
	}

	public void setLowValue(double lowValue) {
		this.lowValue = lowValue;
	}

	public String getParamResult() {
		return paramResult;
	}

	public void setParamResult(String paramResult) {
		this.paramResult = paramResult;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getResultDateTime() {
		return resultDateTime;
	}

	public void setResultDateTime(String resultDateTime) {
		this.resultDateTime = resultDateTime;
	}
	
	

}
