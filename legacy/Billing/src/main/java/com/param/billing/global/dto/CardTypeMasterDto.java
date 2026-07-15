package com.param.billing.global.dto;



public class CardTypeMasterDto {
	private int cardTypeId;
	
	private String cardTypeName;

	private Integer unitId;

	private Integer orgnisationId;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private String pinpadScheme;
	
	private String cardTypeCode;
	

	
	
	

	public String getCardTypeCode() {
		return cardTypeCode;
	}

	public void setCardTypeCode(String cardTypeCode) {
		this.cardTypeCode = cardTypeCode;
	}

	public String getPinpadScheme() {
		return pinpadScheme;
	}

	public void setPinpadScheme(String pinpadScheme) {
		this.pinpadScheme = pinpadScheme;
	}

	public int getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(int cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
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



	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
	
	

}
