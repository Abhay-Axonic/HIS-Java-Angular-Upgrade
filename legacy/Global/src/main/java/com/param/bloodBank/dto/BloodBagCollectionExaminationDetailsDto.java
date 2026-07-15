package com.param.bloodBank.dto;

public class BloodBagCollectionExaminationDetailsDto {

	private Integer examinationId;
	
	private Integer bloodBagCollectionId;
	
	private Double pulse;
	
	private Double bpSystolic;
	 	
	private Double bpDiastolic;
	
	private String examinationDate;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;

	public Integer getBloodBagCollectionId() {
		return bloodBagCollectionId;
	}

	public void setBloodBagCollectionId(Integer bloodBagCollectionId) {
		this.bloodBagCollectionId = bloodBagCollectionId;
	}

	public Double getPulse() {
		return pulse;
	}

	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}

	public Double getBpSystolic() {
		return bpSystolic;
	}

	public void setBpSystolic(Double bpSystolic) {
		this.bpSystolic = bpSystolic;
	}

	public Double getBpDiastolic() {
		return bpDiastolic;
	}

	public void setBpDiastolic(Double bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Integer examinationId) {
		this.examinationId = examinationId;
	}

	public String getExaminationDate() {
		return examinationDate;
	}

	public void setExaminationDate(String examinationDate) {
		this.examinationDate = examinationDate;
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
	
	
}
