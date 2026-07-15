package com.param.billing.cashDesk.dto;

public class DenominationMasterDto {
	
	private Integer denominationId;
	
	private Integer countryId;
	
	private String countryName;
	
	private Double denomination;
	
	private String status;
	
	private String currencyCode;
	
	private String currencySymbol;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String createdDate;
	
	private String updatedDate;
	
	public Integer getDenominationId() {
		return denominationId;
	}

	public void setDenominationId(Integer denominationId) {
		this.denominationId = denominationId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Double getDenomination() {
		return denomination;
	}

	public void setDenomination(Double denomination) {
		this.denomination = denomination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	
}
