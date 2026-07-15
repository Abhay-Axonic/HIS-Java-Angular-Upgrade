package com.param.bloodBank.dto;

import java.util.List;

public class BloodBagCollectionDto {
	
	private Integer bloodBagCollectionId;
	
	private Integer campId;
	
	private String donorName;
	
	//private String uhidNumber;
	
	private Integer donorId;
	
	private Integer bloodGroupId;
	 	
	private String collectionDate;
	
	private String fromTime;
	
	private String toTime;
	
	private Integer collectedFrom;
	
	private Integer bloodUnitNumber;
	
	private String segmentNumber;
	
	private String barCode;
	
	private Integer bloodBagTypeId;
	
	private Integer bagCapacityId;
	
	private Integer bloodBankId;
	
	private String batchNo;
	
	private Integer terminationReasonId;
	
	private Integer bloodBagCollectionStatusId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private String fromDate;
	
	private String toDate;
	
	private String bloodBagNo;
	
	private String campName;
	
	private String donorNumber;
	
	private String bloodBagCapacity;
	
	private String bloodBagType;
	
	private String bloodGroup;

	private String bloodBank;
	
	private String gender;
	
	private Integer patientId;
	
	private String statusName;
	
	private String bloodBagCollectionStatus;
	
	private Boolean forwardedToTtdTest;
	
	private Integer transfusionTransmittedDiseasesId;
	
	private String transfusionTransmittedDiseasesStatus;
	
	private boolean isAutologous;
	
	private List<TransfusionTransmittedDiseasesDetailsDto> transfusionTransmittedDiseasesDetails;

	private List<BloodBagCollectionExaminationDetailsDto> examinationDetailsList;

	private Integer bloodBankStockId;
	
	private boolean tTDCheck;
	
	
	public Integer getBloodBagCollectionId() {
		return bloodBagCollectionId;
	}

	public void setBloodBagCollectionId(Integer bloodBagCollectionId) {
		this.bloodBagCollectionId = bloodBagCollectionId;
	}

	public Integer getCampId() {
		return campId;
	}

	public void setCampId(Integer campId) {
		this.campId = campId;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getCollectedFrom() {
		return collectedFrom;
	}

	public void setCollectedFrom(Integer collectedFrom) {
		this.collectedFrom = collectedFrom;
	}

	public Integer getBloodUnitNumber() {
		return bloodUnitNumber;
	}

	public void setBloodUnitNumber(Integer bloodUnitNumber) {
		this.bloodUnitNumber = bloodUnitNumber;
	}

	public String getSegmentNumber() {
		return segmentNumber;
	}

	public void setSegmentNumber(String segmentNumber) {
		this.segmentNumber = segmentNumber;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public String getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
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

	public List<BloodBagCollectionExaminationDetailsDto> getExaminationDetailsList() {
		return examinationDetailsList;
	}

	public void setExaminationDetailsList(List<BloodBagCollectionExaminationDetailsDto> examinationDetailsList) {
		this.examinationDetailsList = examinationDetailsList;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public Integer getBagCapacityId() {
		return bagCapacityId;
	}

	public void setBagCapacityId(Integer bagCapacityId) {
		this.bagCapacityId = bagCapacityId;
	}

	public Integer getBloodBagCollectionStatusId() {
		return bloodBagCollectionStatusId;
	}

	public void setBloodBagCollectionStatusId(Integer bloodBagCollectionStatusId) {
		this.bloodBagCollectionStatusId = bloodBagCollectionStatusId;
	}

	public Integer getBloodBankId() {
		return bloodBankId;
	}

	public void setBloodBankId(Integer bloodBankId) {
		this.bloodBankId = bloodBankId;
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

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
	}

	public String getDonorNumber() {
		return donorNumber;
	}

	public void setDonorNumber(String donorNumber) {
		this.donorNumber = donorNumber;
	}

	public String getBloodBagCapacity() {
		return bloodBagCapacity;
	}

	public void setBloodBagCapacity(String bloodBagCapacity) {
		this.bloodBagCapacity = bloodBagCapacity;
	}

	public String getBloodBagType() {
		return bloodBagType;
	}

	public void setBloodBagType(String bloodBagType) {
		this.bloodBagType = bloodBagType;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getBloodBank() {
		return bloodBank;
	}

	public void setBloodBank(String bloodBank) {
		this.bloodBank = bloodBank;
	}

	public Boolean getForwardedToTtdTest() {
		return forwardedToTtdTest;
	}

	public void setForwardedToTtdTest(Boolean forwardedToTtdTest) {
		this.forwardedToTtdTest = forwardedToTtdTest;
	}

	public Integer getTransfusionTransmittedDiseasesId() {
		return transfusionTransmittedDiseasesId;
	}

	public void setTransfusionTransmittedDiseasesId(Integer transfusionTransmittedDiseasesId) {
		this.transfusionTransmittedDiseasesId = transfusionTransmittedDiseasesId;
	}

	public String getTransfusionTransmittedDiseasesStatus() {
		return transfusionTransmittedDiseasesStatus;
	}

	public void setTransfusionTransmittedDiseasesStatus(String transfusionTransmittedDiseasesStatus) {
		this.transfusionTransmittedDiseasesStatus = transfusionTransmittedDiseasesStatus;
	}

	public List<TransfusionTransmittedDiseasesDetailsDto> getTransfusionTransmittedDiseasesDetails() {
		return transfusionTransmittedDiseasesDetails;
	}

	public void setTransfusionTransmittedDiseasesDetails(
			List<TransfusionTransmittedDiseasesDetailsDto> transfusionTransmittedDiseasesDetails) {
		this.transfusionTransmittedDiseasesDetails = transfusionTransmittedDiseasesDetails;
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
	}
*/
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getBloodBagCollectionStatus() {
		return bloodBagCollectionStatus;
	}

	public void setBloodBagCollectionStatus(String bloodBagCollectionStatus) {
		this.bloodBagCollectionStatus = bloodBagCollectionStatus;
	}

	public Integer getBloodBankStockId() {
		return bloodBankStockId;
	}

	public void setBloodBankStockId(Integer bloodBankStockId) {
		this.bloodBankStockId = bloodBankStockId;
	}

	public boolean gettTDCheck() {
		return tTDCheck;
	}

	public void settTDCheck(boolean tTDCheck) {
		this.tTDCheck = tTDCheck;
	}

	public boolean isAutologous() {
		return isAutologous;
	}

	public void setAutologous(boolean isAutologous) {
		this.isAutologous = isAutologous;
	}

	public Integer getTerminationReasonId() {
		return terminationReasonId;
	}

	public void setTerminationReasonId(Integer terminationReasonId) {
		this.terminationReasonId = terminationReasonId;
	}

	
}
