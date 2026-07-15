package com.param.global.billing.dto;


public class PinPadDetailsDto {

	private Integer pinpadTransectionId;
	
	private Integer patientId;
	
	private Integer organizationId;
	
	private Integer unitId;
	  
	private Integer tillId;
	
	private String response;
	
	private Character status;

	private String createdDate;

	private Integer createdBy;
	
	private String uniqueNumber;
	
	private String IpAddress;
	
	private String ip;
	
	
	
	
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}

	public String getUniqueNumber() {
		return uniqueNumber;
	}

	public void setUniqueNumber(String uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}

	public Integer getPinpadTransectionId() {
		return pinpadTransectionId;
	}

	public void setPinpadTransectionId(Integer pinpadTransectionId) {
		this.pinpadTransectionId = pinpadTransectionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	
	

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getTillId() {
		return tillId;
	}

	public void setTillId(Integer tillId) {
		this.tillId = tillId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	
	
}
