package com.param.global.dto;

import java.util.List;

import com.param.global.dto.sync.PatientMasterDto;

public class BabyStillAssmntDto {

	private int newBornBabyStillId;
	
	private int patientId;

	private String uhidNumberBaby;
	
	private String stillType;
	
	private String weight;

	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private List<BabyStillAssmntDto> babyDetails;
	
	
	public String getStillType() {
		return stillType;
	}

	public void setStillType(String stillType) {
		this.stillType = stillType;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public int getNewBornBabyStillId() {
		return newBornBabyStillId;
	}

	public void setNewBornBabyStillId(int newBornBabyStillId) {
		this.newBornBabyStillId = newBornBabyStillId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getUhidNumberBaby() {
		return uhidNumberBaby;
	}

	public void setUhidNumberBaby(String uhidNumberBaby) {
		this.uhidNumberBaby = uhidNumberBaby;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public List<BabyStillAssmntDto> getBabyDetails() {
		return babyDetails;
	}

	public void setBabyDetails(List<BabyStillAssmntDto> babyDetails) {
		this.babyDetails = babyDetails;
	}

	
	

	

}
