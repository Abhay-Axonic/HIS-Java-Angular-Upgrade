package com.param.global.unit.dto;

public class DoctorInCabinDto {

	private Integer doctorInCabinId;
	
	private Integer doctorId;
	
	private Integer cabinId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String doctorName;
	
	private String cabinName;
	
	private String qualificationDesc;

	public Integer getDoctorInCabinId() {
		return doctorInCabinId;
	}

	public void setDoctorInCabinId(Integer doctorInCabinId) {
		this.doctorInCabinId = doctorInCabinId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getCabinName() {
		return cabinName;
	}

	public void setCabinName(String cabinName) {
		this.cabinName = cabinName;
	}

	public String getQualificationDesc() {
		return qualificationDesc;
	}

	public void setQualificationDesc(String qualificationDesc) {
		this.qualificationDesc = qualificationDesc;
	}
}
