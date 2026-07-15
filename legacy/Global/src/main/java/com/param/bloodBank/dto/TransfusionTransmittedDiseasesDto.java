package com.param.bloodBank.dto;

import java.util.List;

public class TransfusionTransmittedDiseasesDto {

	private Integer transfusionTransmittedDiseasesId;
	
	private String bloodBagNo;
	
	private Integer bloodBagCollectionId;
	
	private Integer bloodGroupId;
	
	private String barcode;
	
	private Integer ttdDoneById;
	
	private Integer transfusionTransmittedDiseasesStatus;
	
	private List<TransfusionTransmittedDiseasesDetailsDto> transfusionTransmittedDiseasesDetails;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer donorId;

	private String collectionDate;
	
	private Integer bagCapacityId;
	
	private String donorNumber;
	
	private String bloodBagCapacity;
	
	private String bloodGroup;
	
	private String barCode;
	
	private String tTDStatusName;
	
	private Integer bloodBankStockId;
	
	private Boolean tTDCheck;
	
	private Integer bloodUnitNumber;
	
	private Integer fromScreen;
	
	private Integer redCellId;
	
	public Integer getTransfusionTransmittedDiseasesId() {
		return transfusionTransmittedDiseasesId;
	}

	public void setTransfusionTransmittedDiseasesId(Integer transfusionTransmittedDiseasesId) {
		this.transfusionTransmittedDiseasesId = transfusionTransmittedDiseasesId;
	}

	public Integer getBloodBagCollectionId() {
		return bloodBagCollectionId;
	}

	public void setBloodBagCollectionId(Integer bloodBagCollectionId) {
		this.bloodBagCollectionId = bloodBagCollectionId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public List<TransfusionTransmittedDiseasesDetailsDto> getTransfusionTransmittedDiseasesDetails() {
		return transfusionTransmittedDiseasesDetails;
	}

	public void setTransfusionTransmittedDiseasesDetails(
			List<TransfusionTransmittedDiseasesDetailsDto> transfusionTransmittedDiseasesDetails) {
		this.transfusionTransmittedDiseasesDetails = transfusionTransmittedDiseasesDetails;
	}

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
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

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}

	public Integer getBagCapacityId() {
		return bagCapacityId;
	}

	public void setBagCapacityId(Integer bagCapacityId) {
		this.bagCapacityId = bagCapacityId;
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

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getTransfusionTransmittedDiseasesStatus() {
		return transfusionTransmittedDiseasesStatus;
	}

	public void setTransfusionTransmittedDiseasesStatus(Integer transfusionTransmittedDiseasesStatus) {
		this.transfusionTransmittedDiseasesStatus = transfusionTransmittedDiseasesStatus;
	}

	public String gettTDStatusName() {
		return tTDStatusName;
	}

	public void settTDStatusName(String tTDStatusName) {
		this.tTDStatusName = tTDStatusName;
	}

	public Integer getBloodBankStockId() {
		return bloodBankStockId;
	}

	public void setBloodBankStockId(Integer bloodBankStockId) {
		this.bloodBankStockId = bloodBankStockId;
	}

	public Boolean gettTDCheck() {
		return tTDCheck;
	}

	public void settTDCheck(Boolean tTDCheck) {
		this.tTDCheck = tTDCheck;
	}

	public Integer getTtdDoneById() {
		return ttdDoneById;
	}

	public void setTtdDoneById(Integer ttdDoneById) {
		this.ttdDoneById = ttdDoneById;
	}

	public Integer getBloodUnitNumber() {
		return bloodUnitNumber;
	}

	public void setBloodUnitNumber(Integer bloodUnitNumber) {
		this.bloodUnitNumber = bloodUnitNumber;
	}

	public Integer getFromScreen() {
		return fromScreen;
	}

	public void setFromScreen(Integer fromScreen) {
		this.fromScreen = fromScreen;
	}

	public Integer getRedCellId() {
		return redCellId;
	}

	public void setRedCellId(Integer redCellId) {
		this.redCellId = redCellId;
	}
}
