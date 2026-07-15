package com.param.billing.master.dto;

import java.util.List;

public class PaymentDepositConsumptionDto {

	private Integer depConsumptionId;

	private Integer organizationId;

	private Integer unitId;

	private String depConsumptionDate;

	private String depositeConsumptionNumber;

	private Integer billId;

	private Integer billPayeeDetailsId;

	private Double consumedAmount;

	private Integer consumptionAgainst;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private Character status;

	private Integer counterId;

	private List<PaymentDepositConsumptionDetailsDto> paymentDepositConsumptionDetailsDtoList;

	public List<PaymentDepositConsumptionDetailsDto> getPaymentDepositConsumptionDetailsDtoList() {
		return paymentDepositConsumptionDetailsDtoList;
	}

	public void setPaymentDepositConsumptionDetailsDtoList(
			List<PaymentDepositConsumptionDetailsDto> paymentDepositConsumptionDetailsDtoList) {
		this.paymentDepositConsumptionDetailsDtoList = paymentDepositConsumptionDetailsDtoList;
	}

	public String getDepConsumptionDate() {
		return depConsumptionDate;
	}

	public void setDepConsumptionDate(String depConsumptionDate) {
		this.depConsumptionDate = depConsumptionDate;
	}

	public String getDepositeConsumptionNumber() {
		return depositeConsumptionNumber;
	}

	public void setDepositeConsumptionNumber(String depositeConsumptionNumber) {
		this.depositeConsumptionNumber = depositeConsumptionNumber;
	}

	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getDepConsumptionId() {
		return depConsumptionId;
	}

	public void setDepConsumptionId(Integer depConsumptionId) {
		this.depConsumptionId = depConsumptionId;
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

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getConsumedAmount() {
		return consumedAmount;
	}

	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}

	public Integer getConsumptionAgainst() {
		return consumptionAgainst;
	}

	public void setConsumptionAgainst(Integer consumptionAgainst) {
		this.consumptionAgainst = consumptionAgainst;
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

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

}
