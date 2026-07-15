package com.param.billing.global.transaction.config.dto;

import java.util.List;

public class MiscellaneousExpenseDto {

	private Integer expenseId;
	
	private String billNumber;

	private String billDateTime;

	private Double amount;
	
	private Integer billType;
	
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

	private List<MiscellaneousExpenseRecieptDetailsDto> miscellaneousExpenseRecieptDetailsDtosList; 
	
	private String accountHeadDesc;
	
	private String remark;

	private String createdDatetime;
	
	private String systemGenBillNumber;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequestSentToEmployee() {
		return requestSentToEmployee;
	}

	public void setRequestSentToEmployee(String requestSentToEmployee) {
		this.requestSentToEmployee = requestSentToEmployee;
	}

	public String getAccountHeadDesc() {
		return accountHeadDesc;
	}

	public void setAccountHeadDesc(String accountHeadDesc) {
		this.accountHeadDesc = accountHeadDesc;
	}

	public List<RequestApprovalDto> getApprovalDtosList() {
		return approvalDtosList;
	}

	public void setApprovalDtosList(List<RequestApprovalDto> approvalDtosList) {
		this.approvalDtosList = approvalDtosList;
	}

	public Integer getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(Integer reqStatus) {
		this.reqStatus = reqStatus;
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

	public String getCancelRemark() {
		return cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}

	public Double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Double getRecieptAmount() {
		return recieptAmount;
	}

	public void setRecieptAmount(Double recieptAmount) {
		this.recieptAmount = recieptAmount;
	}

	public List<MiscellaneousExpenseRecieptDetailsDto> getMiscellaneousExpenseRecieptDetailsDtosList() {
		return miscellaneousExpenseRecieptDetailsDtosList;
	}

	public void setMiscellaneousExpenseRecieptDetailsDtosList(
			List<MiscellaneousExpenseRecieptDetailsDto> miscellaneousExpenseRecieptDetailsDtosList) {
		this.miscellaneousExpenseRecieptDetailsDtosList = miscellaneousExpenseRecieptDetailsDtosList;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
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

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
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

	public String getSystemGenBillNumber() {
		return systemGenBillNumber;
	}

	public void setSystemGenBillNumber(String systemGenBillNumber) {
		this.systemGenBillNumber = systemGenBillNumber;
	}

	public String getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(String createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	
}
