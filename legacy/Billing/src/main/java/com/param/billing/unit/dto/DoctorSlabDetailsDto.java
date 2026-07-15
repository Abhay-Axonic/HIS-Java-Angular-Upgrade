package com.param.billing.unit.dto;

public class DoctorSlabDetailsDto {

	
	private Integer doctorSlabDetailsId;
	private Integer doctorSlabMasterId;
	private Double slabFrom;
	private Double slanbTo;
	private Double percentage;
	private Integer organizationId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate; 
	private String updatedDate;
	private Character status;
	
	
	public Integer getDoctorSlabDetailsId() {
		return doctorSlabDetailsId;
	}
	public void setDoctorSlabDetailsId(Integer doctorSlabDetailsId) {
		this.doctorSlabDetailsId = doctorSlabDetailsId;
	}
	public Integer getDoctorSlabMasterId() {
		return doctorSlabMasterId;
	}
	public void setDoctorSlabMasterId(Integer doctorSlabMasterId) {
		this.doctorSlabMasterId = doctorSlabMasterId;
	}
	public Double getSlabFrom() {
		return slabFrom;
	}
	public void setSlabFrom(Double slabFrom) {
		this.slabFrom = slabFrom;
	}
	public Double getSlanbTo() {
		return slanbTo;
	}
	public void setSlanbTo(Double slanbTo) {
		this.slanbTo = slanbTo;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
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
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	
	
	
	
}
