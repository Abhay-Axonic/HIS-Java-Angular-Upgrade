package com.param.billing.global.transaction.config.dto;

import java.util.List;

public class CompanyReceiptsDto {
	
private Integer companyReceiptsExpenseId;
	
	private String billNumber;

	private String billDateTime;

	private Double amount;
	
	private Integer companyTypeId;
	
	private Integer billStatus;
	
	private String name;
	
	private Integer accountHead;
	
	private Character isCancelled;
	
	private Integer cancelledReasonId;

	private String billCancelDatetime;

	private Integer cancelledBy;

	private String billCancelRemark;
	
	private Integer unitId;

	private Integer orgId;

	private Character status;

	private String fromDate;

	private Integer createdBy;

	private String toDate;

	private Integer updatedBy;
	
	private Double recieptAmount;
	
	private Double receivedAmount;
	
	private String cancelRemark;
	
	private Integer requestSentTo;
	
	private Integer requestSentBy;
	
	private Integer reqStatus;
	
	private String requestSentToEmployee;
	
	private List<RequestApprovalDto> approvalDtosList;

	private List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDto; 
	
	private String remark;
	
	private Integer companyId;
	
	private Integer insuranceCompanyId;
	
	private String companyName;
	
	private Double balanceAmount;
	
	private Double consumedAmount;
	
	private Double oldBalanceAmount;
	
	private Double oldConsumedAmount;
	
	private Integer creditNoteId;
	
	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Double getOldConsumedAmount() {
		return oldConsumedAmount;
	}

	public void setOldConsumedAmount(Double oldConsumedAmount) {
		this.oldConsumedAmount = oldConsumedAmount;
	}

	public Double getOldBalanceAmount() {
		return oldBalanceAmount;
	}

	public void setOldBalanceAmount(Double oldBalanceAmount) {
		this.oldBalanceAmount = oldBalanceAmount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Double getConsumedAmount() {
		return consumedAmount;
	}

	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}
	

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(String billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public Integer getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAccountHead() {
		return accountHead;
	}

	public void setAccountHead(Integer accountHead) {
		this.accountHead = accountHead;
	}

	public Character getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Character isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Integer getCancelledReasonId() {
		return cancelledReasonId;
	}

	public void setCancelledReasonId(Integer cancelledReasonId) {
		this.cancelledReasonId = cancelledReasonId;
	}

	public String getBillCancelDatetime() {
		return billCancelDatetime;
	}

	public void setBillCancelDatetime(String billCancelDatetime) {
		this.billCancelDatetime = billCancelDatetime;
	}

	public Integer getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public String getBillCancelRemark() {
		return billCancelRemark;
	}

	public void setBillCancelRemark(String billCancelRemark) {
		this.billCancelRemark = billCancelRemark;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Double getRecieptAmount() {
		return recieptAmount;
	}

	public void setRecieptAmount(Double recieptAmount) {
		this.recieptAmount = recieptAmount;
	}

	public Double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public String getCancelRemark() {
		return cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}

	public Integer getRequestSentTo() {
		return requestSentTo;
	}

	public void setRequestSentTo(Integer requestSentTo) {
		this.requestSentTo = requestSentTo;
	}

	public Integer getRequestSentBy() {
		return requestSentBy;
	}

	public void setRequestSentBy(Integer requestSentBy) {
		this.requestSentBy = requestSentBy;
	}

	public Integer getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(Integer reqStatus) {
		this.reqStatus = reqStatus;
	}

	public String getRequestSentToEmployee() {
		return requestSentToEmployee;
	}

	public void setRequestSentToEmployee(String requestSentToEmployee) {
		this.requestSentToEmployee = requestSentToEmployee;
	}

	public List<RequestApprovalDto> getApprovalDtosList() {
		return approvalDtosList;
	}

	public void setApprovalDtosList(List<RequestApprovalDto> approvalDtosList) {
		this.approvalDtosList = approvalDtosList;
	}

	public List<CompanyRecieptsReceiptDetailsDto> getCompanyRecieptsReceiptDetailsDto() {
		return companyRecieptsReceiptDetailsDto;
	}

	public void setCompanyRecieptsReceiptDetailsDto(
			List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDto) {
		this.companyRecieptsReceiptDetailsDto = companyRecieptsReceiptDetailsDto;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getInsuranceCompanyId() {
		return insuranceCompanyId;
	}

	public void setInsuranceCompanyId(Integer insuranceCompanyId) {
		this.insuranceCompanyId = insuranceCompanyId;
	}
	
	
	
	
	
}
