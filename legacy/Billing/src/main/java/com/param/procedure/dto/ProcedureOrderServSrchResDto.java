package com.param.procedure.dto;

public class ProcedureOrderServSrchResDto {
	private String serviceName;
	private String serviceCode;
	private Integer specialityId;
	private Integer serviceId;
	private String speciality;
	private Double rate;
	private String procedureType;
	private Integer subSpecialityId;
	private Integer procedureTypeId;
	private Integer procedureDuration;
	private Integer surgeryGradeId;
	private String surgeryGrade;

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}
	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public String getProcedureType() {
		return procedureType;
	}
	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getProcedureDuration() {
		return procedureDuration;
	}
	public void setProcedureDuration(Integer procedureDuration) {
		this.procedureDuration = procedureDuration;
	}
	
	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}
	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}
	public String getSurgeryGrade() {
		return surgeryGrade;
	}
	public void setSurgeryGrade(String surgeryGrade) {
		this.surgeryGrade = surgeryGrade;
	}
	
}
