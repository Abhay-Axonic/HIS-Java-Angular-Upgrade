package com.param.billing.master.dto;

public class PaymentDepositConsumptionDetailsDto {

	
    private Integer paymentDepoConsumptionDetailsId;
	
	private Integer depConsumptionId;
	
	private Integer depositId;
	
	private Integer depositDetailsId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Double consumedAmount;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Character status;

	public Integer getPaymentDepoConsumptionDetailsId() {
		return paymentDepoConsumptionDetailsId;
	}

	public void setPaymentDepoConsumptionDetailsId(Integer paymentDepoConsumptionDetailsId) {
		this.paymentDepoConsumptionDetailsId = paymentDepoConsumptionDetailsId;
	}

	public Integer getDepConsumptionId() {
		return depConsumptionId;
	}

	public void setDepConsumptionId(Integer depConsumptionId) {
		this.depConsumptionId = depConsumptionId;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public Integer getDepositDetailsId() {
		return depositDetailsId;
	}

	public void setDepositDetailsId(Integer depositDetailsId) {
		this.depositDetailsId = depositDetailsId;
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

	public Double getConsumedAmount() {
		return consumedAmount;
	}

	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}
