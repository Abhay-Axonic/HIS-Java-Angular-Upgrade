package com.param.global.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NewBornBabyDto {

	private int newBornBabyRegId;
	
	private int patientId;
	
	private int babyPrefix;
	
	private int gestationalAge;

	private String uhidNumberBaby;
	
	private String fatherName;
	
	private String deliveryType;
	
	private int numberOfBabies;
	
	private String babyName;
	
	private Double weight;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String dob;
	
	private String tob;
	
	private Integer sex;
	
	private String outcome;
	
	private String birthTerm;

	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private List<NewBornBabyDto> babyDetails;
	
	private String gender;

	public int getNewBornBabyRegId() {
		return newBornBabyRegId;
	}

	public void setNewBornBabyRegId(int newBornBabyRegId) {
		this.newBornBabyRegId = newBornBabyRegId;
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

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public int getNumberOfBabies() {
		return numberOfBabies;
	}

	public void setNumberOfBabies(int numberOfBabies) {
		this.numberOfBabies = numberOfBabies;
	}

	public String getBabyName() {
		return babyName;
	}

	public void setBabyName(String babyName) {
		this.babyName = babyName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getTob() {
		return tob;
	}

	public void setTob(String tob) {
		this.tob = tob;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
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

	public List<NewBornBabyDto> getBabyDetails() {
		return babyDetails;
	}

	public void setBabyDetails(List<NewBornBabyDto> babyDetails) {
		this.babyDetails = babyDetails;
	}

	public int getBabyPrefix() {
		return babyPrefix;
	}

	public void setBabyPrefix(int babyPrefix) {
		this.babyPrefix = babyPrefix;
	}

	public int getGestationalAge() {
		return gestationalAge;
	}

	public void setGestationalAge(int gestationalAge) {
		this.gestationalAge = gestationalAge;
	}

	public String getBirthTerm() {
		return birthTerm;
	}

	public void setBirthTerm(String birthTerm) {
		this.birthTerm = birthTerm;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	
	
}
