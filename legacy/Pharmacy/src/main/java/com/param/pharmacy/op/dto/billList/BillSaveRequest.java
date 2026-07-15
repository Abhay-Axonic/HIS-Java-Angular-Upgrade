package com.param.pharmacy.op.dto.billList;

import java.util.Date;
import java.util.List;

import com.param.global.billing.dto.PaymentDepositDetailsList;

public class BillSaveRequest {

	protected Long billingMasterId;

	protected Long billPayeeDetailsId;

	protected Date dateOfReciept;

	protected Integer payeeId;

	protected List<PaymentReceiptDetailsDtosList> paymentReceiptDetailsDtosList;

	protected Integer orgnizationId;

	protected Integer unitId;

	protected Date createdDate;

	protected Date updatedDate;

	protected Integer updatedBy;

	protected Integer createdBy;

	protected Integer receiptType;

	protected String receivedFrom;

	protected String identificationNumber;

	protected String remark;

	protected Double amountData;

	protected Long totalDepositConsumption;

	protected String isPreOrPostBilling;

	protected String isFinancialClearance;

	protected List<PaymentDepositDetailsList> paymentDepositDetailsList;

	private Integer patientId;

	private Integer saleId;

	private String depositeConsumptionNumber;
	private Boolean cashSettlement;
    protected Integer selfPayeeId;
    protected Double balanceAmount;
    
    protected Double depositConsumption;
	public Boolean getCashSettlement() {
		return cashSettlement;
	}

	public void setCashSettlement(Boolean cashSettlement) {
		this.cashSettlement = cashSettlement;
	}

	public BillSaveRequest() {
		super();
	}

	public Long getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Long billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Long getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Long billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public Date getDateOfReciept() {
		return dateOfReciept;
	}

	public void setDateOfReciept(Date dateOfReciept) {
		this.dateOfReciept = dateOfReciept;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public List<PaymentReceiptDetailsDtosList> getPaymentReceiptDetailsDtosList() {
		return paymentReceiptDetailsDtosList;
	}

	public void setPaymentReceiptDetailsDtosList(List<PaymentReceiptDetailsDtosList> paymentReceiptDetailsDtosList) {
		this.paymentReceiptDetailsDtosList = paymentReceiptDetailsDtosList;
	}

	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getAmountData() {
		return amountData;
	}

	public void setAmountData(Double amount) {
		this.amountData = amount;
	}

	public Long getTotalDepositConsumption() {
		return totalDepositConsumption;
	}

	public void setTotalDepositConsumption(Long totalDepositConsumption) {
		this.totalDepositConsumption = totalDepositConsumption;
	}

	public String getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(String isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public String getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(String isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public List<PaymentDepositDetailsList> getPaymentDepositDetailsList() {
		return paymentDepositDetailsList;
	}

	public void setPaymentDepositDetailsList(List<PaymentDepositDetailsList> paymentDepositDetailsList) {
		this.paymentDepositDetailsList = paymentDepositDetailsList;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getDepositeConsumptionNumber() {
		return depositeConsumptionNumber;
	}

	public void setDepositeConsumptionNumber(String depositeConsumptionNumber) {
		this.depositeConsumptionNumber = depositeConsumptionNumber;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Double getDepositConsumption() {
		return depositConsumption;
	}

	public void setDepositConsumption(Double depositConsumption) {
		this.depositConsumption = depositConsumption;
	}

}
