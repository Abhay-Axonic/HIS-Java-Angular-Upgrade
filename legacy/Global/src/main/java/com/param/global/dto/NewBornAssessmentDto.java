package com.param.global.dto;

import java.util.List;

public class NewBornAssessmentDto {

	private int assessmentId;

	private String uhidNumberBaby;
	
	private String breastFeeding;
	
	private String transferOfBabyTo;
	
	private String cordClamping;
	
	private String remarks;

	private String bloodPressureSystolic;
	
	private String pulseRate;
	
	private String weight;
	
	private String height;
	
	private String headCircumference;
	
	private String temperature;
	
	private String bloodPressureDiastolic;
	
	private String respiration;
	
	private List<NewBornApgarDto> apgarArrList ;
	
	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
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

	public int getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getBreastFeeding() {
		return breastFeeding;
	}

	public void setBreastFeeding(String breastFeeding) {
		this.breastFeeding = breastFeeding;
	}

	public String getTransferOfBabyTo() {
		return transferOfBabyTo;
	}

	public void setTransferOfBabyTo(String transferOfBabyTo) {
		this.transferOfBabyTo = transferOfBabyTo;
	}

	public String getCordClamping() {
		return cordClamping;
	}

	public void setCordClamping(String cordClamping) {
		this.cordClamping = cordClamping;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUhidNumberBaby() {
		return uhidNumberBaby;
	}

	public void setUhidNumberBaby(String uhidNumberBaby) {
		this.uhidNumberBaby = uhidNumberBaby;
	}

	public String getBloodPressureSystolic() {
		return bloodPressureSystolic;
	}

	public void setBloodPressureSystolic(String bloodPressureSystolic) {
		this.bloodPressureSystolic = bloodPressureSystolic;
	}

	public String getPulseRate() {
		return pulseRate;
	}

	public void setPulseRate(String pulseRate) {
		this.pulseRate = pulseRate;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHeadCircumference() {
		return headCircumference;
	}

	public void setHeadCircumference(String headCircumference) {
		this.headCircumference = headCircumference;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getBloodPressureDiastolic() {
		return bloodPressureDiastolic;
	}

	public void setBloodPressureDiastolic(String bloodPressureDiastolic) {
		this.bloodPressureDiastolic = bloodPressureDiastolic;
	}

	public String getRespiration() {
		return respiration;
	}

	public void setRespiration(String respiration) {
		this.respiration = respiration;
	}

	public List<NewBornApgarDto> getApgarArrList() {
		return apgarArrList;
	}

	public void setApgarArrList(List<NewBornApgarDto> apgarArrList) {
		this.apgarArrList = apgarArrList;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}



}
