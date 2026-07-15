package com.param.billing.unit.dto;

public class DoctorSlabDto {

	private Integer doctorSlabMasterId;
	private Integer doctorId;
	private Integer organizationId;
	private Integer unitId;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate; 
	private String updatedDate;
	private Character status;
	private Double amount;
	private Double slabFrom;
	private String doctorName;
	private Double slabTo;
	private Integer offset;
	private Integer noOfRecordsPerPage;
	
	
	private Double percentage;
	public Integer getDoctorSlabMasterId() {
		return doctorSlabMasterId;
	}
	public void setDoctorSlabMasterId(Integer doctorSlabMasterId) {
		this.doctorSlabMasterId = doctorSlabMasterId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}
	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}
	public Double getSlabFrom() {
		return slabFrom;
	}
	public void setSlabFrom(Double slabFrom) {
		this.slabFrom = slabFrom;
	}
	public Double getSlabTo() {
		return slabTo;
	}
	public void setSlabTo(Double slabTo) {
		this.slabTo = slabTo;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	
	
	
}
