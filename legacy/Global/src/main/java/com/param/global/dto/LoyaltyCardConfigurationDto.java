package com.param.global.dto;

import java.math.BigDecimal;

public class LoyaltyCardConfigurationDto {

	private Integer cardConfigurationId;

	private BigDecimal minStartAmount;

	private BigDecimal thresholdAmount;
	
	private BigDecimal minRechargeAmount;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Character isDeleted;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organisationId;
	
	private Integer noOfRecordsPerPage;

	private Integer offset;
	

	public Integer getCardConfigurationId() {
		return cardConfigurationId;
	}

	public void setCardConfigurationId(Integer cardConfigurationId) {
		this.cardConfigurationId = cardConfigurationId;
	}

	public BigDecimal getMinStartAmount() {
		return minStartAmount;
	}

	public void setMinStartAmount(BigDecimal minStartAmount) {
		this.minStartAmount = minStartAmount;
	}

	public BigDecimal getThresholdAmount() {
		return thresholdAmount;
	}

	public void setThresholdAmount(BigDecimal thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}

	public BigDecimal getMinRechargeAmount() {
		return minRechargeAmount;
	}

	public void setMinRechargeAmount(BigDecimal minRechargeAmount) {
		this.minRechargeAmount = minRechargeAmount;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
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

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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
	
	
}
