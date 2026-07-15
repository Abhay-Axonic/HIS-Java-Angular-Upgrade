package com.param.global.billing.dto;

import java.math.BigDecimal;
import java.util.List;

public class RequestDiscountDto {

	private Integer reqDiscId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer orderId;
	
	private Integer orderDetailsId;
	
	private String requestDate;
	
	private Integer visitTypeId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer patientId;
	
	private BigDecimal netAmount;
	
	private BigDecimal reqDisAmt;
	
	private BigDecimal approvedDiscAmt;
	
	private Integer requestSentBy;
	
	private Integer requestSentTo;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Integer reqStatus;
	
	private String status;

	private Integer tRefundCnId;

	private String depositNo;

	private Integer depositId;

	private BigDecimal refundAmount;

	private Integer discType;
	
	private String createdDate;
	
	private String updatedDate;
	
	private String encounterNumber;
	
	private String admissionNumber;
	
	private String patientName;
	
	private String requestSentByEmployee;
	
	private String requestSentToEmployee;
	
	private String requestNumber;
	
	private Integer requestType;
	
	private Integer creditNoteId;
	
	private String creditNoteNo;
	
	private Double crNetAmount;
	
	private Integer billId;
	
    private Integer expenseId;
     
	private String expenseBillNumber;
	
	private Double expenseAmount;
	
	private Integer payeeId;
	
	private String billNumber; 
	
	private Integer tSaleId;
	
	private String discTypeDesc; 
	
	private String fromDate;
	
	private String toDate;
	
	private Integer requestBy;
	
	private String uhidNumber;
	
	private Integer companyReceiptsExpenseId;
	
	private String companyReceiptsExpenseBillNumber;
	
	private Double companyReceiptsExpenseAmount;
	
	private List<RequestDiscountDetailsDto> requestDiscountDetailsDtosList;
	
	private String creditNoteRemark;
	
	private Integer selfPayeeId;
	
	private String creditReason;
	
	private String remark;
	
	private String refundRemark;
	
	private String expenseRemark;
	
	private String companyReciptRemark;
	
	private Double netBillAmount;
	
	private Character isFinancialClearance;
	
	private Integer billStatusId;
	
	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public String getCreditReason() {
		return creditReason;
	}

	public void setCreditReason(String creditReason) {
		this.creditReason = creditReason;
	}

	public String getCreditNoteRemark() {
		return creditNoteRemark;
	}

	public void setCreditNoteRemark(String creditNoteRemark) {
		this.creditNoteRemark = creditNoteRemark;
	}
		
	private String requestTo;
	
	private String requestFrom;
	
	private Integer toPatientId;
	
	private Integer fromPatientId;
	
	private String topatientName;
	
	private String frompatientName;
	
	public String getToPatientName() {
		return topatientName;
	}

	public void setToPatientName(String topatientName) {
		this.topatientName = topatientName;
	}

	public String getFromPatientName() {
		return frompatientName;
	}

	public void setFromPatientName(String fromPatientName) {
		this.frompatientName = fromPatientName;
	}

	private String username;
	
	private Integer billPayeeDetailsId;
	
	public String getRequestTo() {
		return requestTo;
	}

	public void setRequestTo(String requestTo) {
		this.requestTo = requestTo;
	}

	public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

	public Integer getToPatientId() {
		return toPatientId;
	}

	public void setToPatientId(Integer toPatientId) {
		this.toPatientId = toPatientId;
	}

	public Integer getFromPatientId() {
		return fromPatientId;
	}

	public void setFromPatientId(Integer fromPatientId) {
		this.fromPatientId = fromPatientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;

	}

	public String getCompanyReceiptsExpenseBillNumber() {
		return companyReceiptsExpenseBillNumber;
	}

	public void setCompanyReceiptsExpenseBillNumber(String companyReceiptsExpenseBillNumber) {
		this.companyReceiptsExpenseBillNumber = companyReceiptsExpenseBillNumber;
	}

	public Double getCompanyReceiptsExpenseAmount() {
		return companyReceiptsExpenseAmount;
	}

	public void setCompanyReceiptsExpenseAmount(Double companyReceiptsExpenseAmount) {
		this.companyReceiptsExpenseAmount = companyReceiptsExpenseAmount;
	}

	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}

	public String getExpenseBillNumber() {
		return expenseBillNumber;
	}

	public void setExpenseBillNumber(String expenseBillNumber) {
		this.expenseBillNumber = expenseBillNumber;
	}

	public Double getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(Double expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
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

	public Integer getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(Integer requestBy) {
		this.requestBy = requestBy;
	}

	public String getDiscTypeDesc() {
		return discTypeDesc;
	}

	public void setDiscTypeDesc(String discTypeDesc) {
		this.discTypeDesc = discTypeDesc;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public List<RequestDiscountDetailsDto> getRequestDiscountDetailsDtosList() {
		return requestDiscountDetailsDtosList;
	}

	public void setRequestDiscountDetailsDtosList(List<RequestDiscountDetailsDto> requestDiscountDetailsDtosList) {
		this.requestDiscountDetailsDtosList = requestDiscountDetailsDtosList;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getRequestSentByEmployee() {
		return requestSentByEmployee;
	}

	public void setRequestSentByEmployee(String requestSentByEmployee) {
		this.requestSentByEmployee = requestSentByEmployee;
	}

	public String getRequestSentToEmployee() {
		return requestSentToEmployee;
	}

	public void setRequestSentToEmployee(String requestSentToEmployee) {
		this.requestSentToEmployee = requestSentToEmployee;
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

	public Integer getReqDiscId() {
		return reqDiscId;
	}

	public void setReqDiscId(Integer reqDiscId) {
		this.reqDiscId = reqDiscId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public BigDecimal getApprovedDiscAmt() {
		return approvedDiscAmt;
	}

	public void setApprovedDiscAmt(BigDecimal approvedDiscAmt) {
		this.approvedDiscAmt = approvedDiscAmt;
	}

	public Integer getRequestSentBy() {
		return requestSentBy;
	}

	public void setRequestSentBy(Integer requestSentBy) {
		this.requestSentBy = requestSentBy;
	}

	public Integer getRequestSentTo() {
		return requestSentTo;
	}

	public void setRequestSentTo(Integer requestSentTo) {
		this.requestSentTo = requestSentTo;
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

	public Integer getReqStatus() {
		return reqStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setReqStatus(Integer reqStatus) {
		this.reqStatus = reqStatus;
	}

	public Integer getDiscType() {
		return discType;
	}

	public void setDiscType(Integer discType) {
		this.discType = discType;
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

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public String getCreditNoteNo() {
		return creditNoteNo;
	}

	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}

	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer gettSaleId() {
		return tSaleId;
	}

	public void settSaleId(Integer tSaleId) {
		this.tSaleId = tSaleId;
	}

	public Integer gettRefundCnId() {
		return tRefundCnId;
	}

	public void settRefundCnId(Integer tRefundCnId) {
		this.tRefundCnId = tRefundCnId;
	}

	public String getDepositNo() {
		return depositNo;
	}

	public void setDepositNo(String depositNo) {
		this.depositNo = depositNo;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRefundRemark() {
		return refundRemark;
	}

	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}

	public String getExpenseRemark() {
		return expenseRemark;
	}

	public void setExpenseRemark(String expenseRemark) {
		this.expenseRemark = expenseRemark;
	}

	public String getCompanyReciptRemark() {
		return companyReciptRemark;
	}

	public void setCompanyReciptRemark(String companyReciptRemark) {
		this.companyReciptRemark = companyReciptRemark;
	}

	public Double getNetBillAmount() {
		return netBillAmount;
	}

	public void setNetBillAmount(Double netBillAmount) {
		this.netBillAmount = netBillAmount;
	}

	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	
}
