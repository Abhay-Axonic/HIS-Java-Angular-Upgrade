package com.param.global.dto;

public class LoyaltiCardMasterDto {

	private int loyaltyCardId;

	private String cardNo;

	private Integer organisationId;

	private Integer unitId;

	private char status;

	private String createdDate;

	private int createdBy;

	private int updatedBy;

	private String updatedDate;

	private char isDeleted;

	private Integer noOfRecordsPerPage;

	private Integer offset;

	private Character isActive;

	private Integer patientId;
	
	private Character isUserDefined;

	public int getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(int loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		 this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;
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

	public Character getIsActive() {
		return isActive;
	}

	public void setIsActive(Character isActive) {
		 this.isActive = (isActive == null) ? 'N' : isActive;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getIsUserDefined() {
		return isUserDefined;
	}

	public void setIsUserDefined(Character isUserDefined) {
		this.isUserDefined = isUserDefined;
	}

}
