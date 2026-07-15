package com.param.bloodBank.dto;

import java.util.List;

import javax.persistence.Column;

public class DonorScreeningDetailDto {
	
	private Integer donorScreeningId;
	
	private Integer donorId;
	
	private Integer genderId;
	
	private String dob;
	
	private String mobileNumber;
	
	private Integer identificationTypeId;
	
	private String identificationName;
	
	private String identificationNumber;
	
	private Boolean isAutologous;
	
	private Integer patientId;
	
	private Float donorHeight;
	
	private Float donorWeight;
	
	private Float donorTemperature;
	
	private Float donorPulse;
	
	private Float donorHB;
	
	private Float donorBPSystolic;
	
	private Float donorBpDiastolic;
	
	private Integer hbDoneById;
	
	private Integer bleedingDoneById;
	
	private String deferralDays;
	
	private Integer screeningResultStatusId;
	
	private String screeningResultStatus;
	
	private String screeningRemarks;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Float heightInFt;
	
	private Float heightInInches;
	
	private String fromDate;
	
	private String toDate;
	
	private String donorName;
	
	//private String uhidNumber;
	
	private String donorNumber;
	
	private String gender;
	
	private String screeningResult;
	
	private String bloodGroup;
	
	private Integer bloodGroupId;
	
	private Boolean exportedToBagCollection;
	
	private Integer age;
	
	private String temperatureType;
	
	private String weightType;
	
	private String hBType;
	
	private List<DonorScreeningMapperDto> donorScreeningMapperList;

	public Integer getDonorScreeningId() {
		return donorScreeningId;
	}

	public void setDonorScreeningId(Integer donorScreeningId) {
		this.donorScreeningId = donorScreeningId;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public Float getDonorHeight() {
		return donorHeight;
	}

	public void setDonorHeight(Float donorHeight) {
		this.donorHeight = donorHeight;
	}

	public Float getDonorWeight() {
		return donorWeight;
	}

	public void setDonorWeight(Float donorWeight) {
		this.donorWeight = donorWeight;
	}

	public Float getDonorTemperature() {
		return donorTemperature;
	}

	public void setDonorTemperature(Float donorTemperature) {
		this.donorTemperature = donorTemperature;
	}

	public Float getDonorPulse() {
		return donorPulse;
	}

	public void setDonorPulse(Float donorPulse) {
		this.donorPulse = donorPulse;
	}

	public Float getDonorHB() {
		return donorHB;
	}

	public void setDonorHB(Float donorHB) {
		this.donorHB = donorHB;
	}

	public Float getDonorBPSystolic() {
		return donorBPSystolic;
	}

	public void setDonorBPSystolic(Float donorBPSystolic) {
		this.donorBPSystolic = donorBPSystolic;
	}

	public Float getDonorBpDiastolic() {
		return donorBpDiastolic;
	}

	public void setDonorBpDiastolic(Float donorBpDiastolic) {
		this.donorBpDiastolic = donorBpDiastolic;
	}

	public String getDeferralDays() {
		return deferralDays;
	}

	public void setDeferralDays(String deferralDays) {
		this.deferralDays = deferralDays;
	}
	public String getScreeningRemarks() {
		return screeningRemarks;
	}

	public void setScreeningRemarks(String screeningRemarks) {
		this.screeningRemarks = screeningRemarks;
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

	public List<DonorScreeningMapperDto> getDonorScreeningMapperList() {
		return donorScreeningMapperList;
	}

	public void setDonorScreeningMapperList(List<DonorScreeningMapperDto> donorScreeningMapperList) {
		this.donorScreeningMapperList = donorScreeningMapperList;
	}

	public Float getHeightInFt() {
		return heightInFt;
	}

	public void setHeightInFt(Float heightInFt) {
		this.heightInFt = heightInFt;
	}

	public Float getHeightInInches() {
		return heightInInches;
	}

	public void setHeightInInches(Float heightInInches) {
		this.heightInInches = heightInInches;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	/*public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}*/
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getScreeningResult() {
		return screeningResult;
	}

	public void setScreeningResult(String screeningResult) {
		this.screeningResult = screeningResult;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getScreeningResultStatusId() {
		return screeningResultStatusId;
	}

	public void setScreeningResultStatusId(Integer screeningResultStatusId) {
		this.screeningResultStatusId = screeningResultStatusId;
	}

	public String getScreeningResultStatus() {
		return screeningResultStatus;
	}

	public void setScreeningResultStatus(String screeningResultStatus) {
		this.screeningResultStatus = screeningResultStatus;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationName() {
		return identificationName;
	}

	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public Boolean getIsAutologous() {
		return isAutologous;
	}

	public void setIsAutologous(Boolean isAutologous) {
		this.isAutologous = isAutologous;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Boolean getExportedToBagCollection() {
		return exportedToBagCollection;
	}

	public void setExportedToBagCollection(Boolean exportedToBagCollection) {
		this.exportedToBagCollection = exportedToBagCollection;
	}

	public String getDonorNumber() {
		return donorNumber;
	}

	public void setDonorNumber(String donorNumber) {
		this.donorNumber = donorNumber;
	}

	public Integer getHbDoneById() {
		return hbDoneById;
	}

	public void setHbDoneById(Integer hbDoneById) {
		this.hbDoneById = hbDoneById;
	}

	public Integer getBleedingDoneById() {
		return bleedingDoneById;
	}

	public void setBleedingDoneById(Integer bleedingDoneById) {
		this.bleedingDoneById = bleedingDoneById;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getTemperatureType() {
		return temperatureType;
	}

	public void setTemperatureType(String temperatureType) {
		this.temperatureType = temperatureType;
	}

	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	public String gethBType() {
		return hBType;
	}

	public void sethBType(String hBType) {
		this.hBType = hBType;
	}
}
