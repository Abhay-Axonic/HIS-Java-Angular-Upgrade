package com.param.procedure.dto;

public class OTServiceDto {
	private Integer otServiceId;
	private Integer procedureOrderId;
	private Integer serviceId;//service id against procedure
	private Double amount;
	private Double rate;
	private char status;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Integer procedureId;
	private Integer proReqId;
	private String serviceName;
	private Integer specialityId;
	private Integer subSpecialityId;
	private String speciality;
	private Integer procedureTypeId;
	private Integer isEditable;
	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}
	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}
	public Integer getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}
	public Integer getOtServiceId() {
		return otServiceId;
	}
	public void setOtServiceId(Integer otServiceId) {
		this.otServiceId = otServiceId;
	}
	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}
	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public Integer getProReqId() {
		return proReqId;
	}
	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}
	public Integer getIsEditable() {
		return isEditable;
	}
	public void setIsEditable(Integer isEditable) {
		this.isEditable = isEditable;
	}
	
	
}
