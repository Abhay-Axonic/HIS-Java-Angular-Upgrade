package com.param.global.billing.dto;

public class SelfTriageVitalDto {
private Integer appointmentId;
	
	private Integer hospitalId;
	
	private Integer unitId;
	
	private Integer globalUhid;
	
	private String vitalName;
	
	private String value;
	
	private char status;
	
	private char isSelfTriage;
	
	private Integer vitalId;
	private String localUhid;
	private String organisationName;
	private Integer pkAppointmentVitalMapperId;
	
	private String createdDate;
	private Integer applicationId;

	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getGlobalUhid() {
		return globalUhid;
	}
	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}
	public String getVitalName() {
		return vitalName;
	}
	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public char getIsSelfTriage() {
		return isSelfTriage;
	}
	public void setIsSelfTriage(char isSelfTriage) {
		this.isSelfTriage = isSelfTriage;
	}
	public Integer getVitalId() {
		return vitalId;
	}
	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}
	public String getLocalUhid() {
		return localUhid;
	}
	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public Integer getPkAppointmentVitalMapperId() {
		return pkAppointmentVitalMapperId;
	}
	public void setPkAppointmentVitalMapperId(Integer pkAppointmentVitalMapperId) {
		this.pkAppointmentVitalMapperId = pkAppointmentVitalMapperId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
	
	

}
