package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;

public class OutsideHospitalDoctorMapperDto {
	private Integer outsideHosDocId;
	
	private Integer outsideHospitalId;
	
	private String hospDoctorName;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	private String searchDoctor;
	private String outsideHospitalDoctorName;
	private String outsideHospitalDesc;

	public Integer getOutsideHosDocId() {
		return outsideHosDocId;
	}

	public void setOutsideHosDocId(Integer outsideHosDocId) {
		this.outsideHosDocId = outsideHosDocId;
	}

	public Integer getOutsideHospitalId() {
		return outsideHospitalId;
	}

	public void setOutsideHospitalId(Integer outsideHospitalId) {
		this.outsideHospitalId = outsideHospitalId;
	}

	public String getHospDoctorName() {
		return hospDoctorName;
	}

	public void setHospDoctorName(String hospDoctorName) {
		this.hospDoctorName = hospDoctorName;
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

	public String getSearchDoctor() {
		return searchDoctor;
	}

	public void setSearchDoctor(String searchDoctor) {
		this.searchDoctor = searchDoctor;
	}

	public String getOutsideHospitalDoctorName() {
		return outsideHospitalDoctorName;
	}

	public void setOutsideHospitalDoctorName(String outsideHospitalDoctorName) {
		this.outsideHospitalDoctorName = outsideHospitalDoctorName;
	}

	public String getOutsideHospitalDesc() {
		return outsideHospitalDesc;
	}

	public void setOutsideHospitalDesc(String outsideHospitalDesc) {
		this.outsideHospitalDesc = outsideHospitalDesc;
	}
	
	
}
