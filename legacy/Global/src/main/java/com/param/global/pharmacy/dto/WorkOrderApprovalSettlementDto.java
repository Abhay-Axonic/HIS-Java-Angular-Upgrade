package com.param.global.pharmacy.dto;

import java.util.List;

public class WorkOrderApprovalSettlementDto {
	
	private Integer workorderSettlementId;
	
	private String workorderSettlementNumber;
	
	
	private Integer workorderRequestApprovalId;
	
	private Integer workorderRequestId;

	private String workorderRequestNumber;

	private Integer billTypeId;

	private Integer storeId;

	private Integer vendorId;

	private String vendorName;
	
	private String storeName;

	private String deliveryDate;

	private Integer contractTypeId;

	private Double totalAmount;

	private Double totalDiscountAmount;

	private Double totalTaxAmount;

	private Double otherChargeAmount;

	private Double totalNetAmount;

	private Integer workorderRequestStatusId;

	private String workorderRequestDate;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private Integer unitId;

	private Integer orgId;

	private String status;

	private String paymentTerms;

	private String guaranteeWarranty;

	private Boolean selectedToPendingstatus;

	private Boolean isReject;

	private Integer cancelReasonId;

	private String rejectNote;

	private String fromDate;

	private String toDate;

	private String userName;

	private Integer againstTypeId;

	private String againstTypeName;

	private Integer approvedBy;

	private String approvedName;

	private String approvalRemark;
	private String approvalStatus;
	private Integer workOrderCompletionId;
	
	private boolean isReverseTax;
	
	public boolean getIsReverseTax(){
		return isReverseTax;
	}
	
	public void setIsReverseTax(boolean isReverseTax){
		this.isReverseTax = isReverseTax;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	private Double roundoff;
	
	private Double paidAmount;

	private Double balanceAmount;
	
	private Double settlementAmount;
	

	private Integer paymentStatusId;

	private List<WorkOrderCreationRequestApprovalDetailsDto> requestApprovalDetailsDto;

	private List<WorkOrderCreationOtherChargeDetailsDto> requestTaxDetailsDto;

	private List<WorkOrderCreationRequestDocumentDto> requestDocumentDto;

	private List<WorkOrderCreationApprovalDto> workOrderRequestList; 
	  
	private List<WOSettlementDetailsDto> settlementDetailsDto;

	public Integer getWorkorderRequestApprovalId() {
		return workorderRequestApprovalId;
	}

	public void setWorkorderRequestApprovalId(Integer workorderRequestApprovalId) {
		this.workorderRequestApprovalId = workorderRequestApprovalId;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}

	public String getWorkorderRequestNumber() {
		return workorderRequestNumber;
	}

	public void setWorkorderRequestNumber(String workorderRequestNumber) {
		this.workorderRequestNumber = workorderRequestNumber;
	}

	public Integer getBillTypeId() {
		return billTypeId;
	}

	public void setBillTypeId(Integer billTypeId) {
		this.billTypeId = billTypeId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(Double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getOtherChargeAmount() {
		return otherChargeAmount;
	}

	public void setOtherChargeAmount(Double otherChargeAmount) {
		this.otherChargeAmount = otherChargeAmount;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public Integer getWorkorderRequestStatusId() {
		return workorderRequestStatusId;
	}

	public void setWorkorderRequestStatusId(Integer workorderRequestStatusId) {
		this.workorderRequestStatusId = workorderRequestStatusId;
	}

	public String getWorkorderRequestDate() {
		return workorderRequestDate;
	}

	public void setWorkorderRequestDate(String workorderRequestDate) {
		this.workorderRequestDate = workorderRequestDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getGuaranteeWarranty() {
		return guaranteeWarranty;
	}

	public void setGuaranteeWarranty(String guaranteeWarranty) {
		this.guaranteeWarranty = guaranteeWarranty;
	}

	public Boolean getSelectedToPendingstatus() {
		return selectedToPendingstatus;
	}

	public void setSelectedToPendingstatus(Boolean selectedToPendingstatus) {
		this.selectedToPendingstatus = selectedToPendingstatus;
	}

	public Boolean getIsReject() {
		return isReject;
	}

	public void setIsReject(Boolean isReject) {
		this.isReject = isReject;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAgainstTypeId() {
		return againstTypeId;
	}

	public void setAgainstTypeId(Integer againstTypeId) {
		this.againstTypeId = againstTypeId;
	}

	public String getAgainstTypeName() {
		return againstTypeName;
	}

	public void setAgainstTypeName(String againstTypeName) {
		this.againstTypeName = againstTypeName;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovedName() {
		return approvedName;
	}

	public void setApprovedName(String approvedName) {
		this.approvedName = approvedName;
	}

	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	public Double getRoundoff() {
		return roundoff;
	}

	public void setRoundoff(Double roundoff) {
		this.roundoff = roundoff;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Integer getPaymentStatusId() {
		return paymentStatusId;
	}

	public void setPaymentStatusId(Integer paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}

	public List<WorkOrderCreationRequestApprovalDetailsDto> getRequestApprovalDetailsDto() {
		return requestApprovalDetailsDto;
	}

	public void setRequestApprovalDetailsDto(List<WorkOrderCreationRequestApprovalDetailsDto> requestApprovalDetailsDto) {
		this.requestApprovalDetailsDto = requestApprovalDetailsDto;
	}

	public List<WorkOrderCreationOtherChargeDetailsDto> getRequestTaxDetailsDto() {
		return requestTaxDetailsDto;
	}

	public void setRequestTaxDetailsDto(List<WorkOrderCreationOtherChargeDetailsDto> requestTaxDetailsDto) {
		this.requestTaxDetailsDto = requestTaxDetailsDto;
	}

	public List<WorkOrderCreationRequestDocumentDto> getRequestDocumentDto() {
		return requestDocumentDto;
	}

	public void setRequestDocumentDto(List<WorkOrderCreationRequestDocumentDto> requestDocumentDto) {
		this.requestDocumentDto = requestDocumentDto;
	}

	public List<WorkOrderCreationApprovalDto> getWorkOrderRequestList() {
		return workOrderRequestList;
	}

	public void setWorkOrderRequestList(List<WorkOrderCreationApprovalDto> workOrderRequestList) {
		this.workOrderRequestList = workOrderRequestList;
	}

	public List<WOSettlementDetailsDto> getSettlementDetailsDto() {
		return settlementDetailsDto;
	}

	public void setSettlementDetailsDto(List<WOSettlementDetailsDto> settlementDetailsDto) {
		this.settlementDetailsDto = settlementDetailsDto;
	}

	public Double getSettlementAmount() {
		return settlementAmount;
	}

	public void setSettlementAmount(Double settlementAmount) {
		this.settlementAmount = settlementAmount;
	}

	public Integer getWorkorderSettlementId() {
		return workorderSettlementId;
	}

	public void setWorkorderSettlementId(Integer workorderSettlementId) {
		this.workorderSettlementId = workorderSettlementId;
	}

	public String getWorkorderSettlementNumber() {
		return workorderSettlementNumber;
	}

	public void setWorkorderSettlementNumber(String workorderSettlementNumber) {
		this.workorderSettlementNumber = workorderSettlementNumber;
	}

	public Integer getWorkOrderCompletionId() {
		return workOrderCompletionId;
	}

	public void setWorkOrderCompletionId(Integer workOrderCompletionId) {
		this.workOrderCompletionId = workOrderCompletionId;
	}
	
	
	

}
