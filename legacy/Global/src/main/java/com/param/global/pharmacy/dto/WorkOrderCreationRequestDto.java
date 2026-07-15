package com.param.global.pharmacy.dto;

import java.util.Date;
import java.util.List;

public class WorkOrderCreationRequestDto {

	private Integer workorderRequestId;

	private String workorderRequestNumber;

	private Integer billTypeId;

	private Integer storeId;

	private Integer vendorId;

	private String vendorName;
	
	private String storeName;

	private String deliveryDate;

	private Integer contractTypeId;
	
	private String contractType;

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
	
	private Integer statusId;

	private boolean workOrderCompletion;
	
	private Integer percentageAmountPaid;
	
	private Integer workOrderCompletionId;
	
	private Integer completionCount;
	
	private boolean updateStaus;
	
	private boolean isSettled;
	
	private Integer admissionId;

	private Integer encounterId;

	private Integer billId;
	
	private String patientDetails;
	
	private String uhidNumber;
	
	private String billDetails;
	
	private String visitDetails;
	
	private Date dueDate;
	
	private Integer bucketFlag;
	
	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(String billDetails) {
		this.billDetails = billDetails;
	}

	public String getVisitDetails() {
		return visitDetails;
	}

	public void setVisitDetails(String visitDetails) {
		this.visitDetails = visitDetails;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

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
	private Double roundoff;
	private Integer approvalStatusId;
	private String approvalStatus;

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	private List<WorkOrderCreationRequestDetailsDto> requestDetailsDto;

	private List<WorkOrderCreationOtherChargeDetailsDto> requestTaxDetailsDto;

	private List<WorkOrderCreationRequestDocumentDto> requestDocumentDto;

	private List<WorkOrderCreationRequestDto> workOrderRequestList;

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

	public List<WorkOrderCreationRequestDetailsDto> getRequestDetailsDto() {
		return requestDetailsDto;
	}

	public void setRequestDetailsDto(List<WorkOrderCreationRequestDetailsDto> requestDetailsDto) {
		this.requestDetailsDto = requestDetailsDto;
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

	public String getWorkorderRequestDate() {
		return workorderRequestDate;
	}

	public void setWorkorderRequestDate(String workorderRequestDate) {
		this.workorderRequestDate = workorderRequestDate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Boolean getSelectedToPendingstatus() {
		return selectedToPendingstatus;
	}

	public void setSelectedToPendingstatus(Boolean selectedToPendingstatus) {
		this.selectedToPendingstatus = selectedToPendingstatus;
	}

	public List<WorkOrderCreationRequestDto> getWorkOrderRequestList() {
		return workOrderRequestList;
	}

	public void setWorkOrderRequestList(List<WorkOrderCreationRequestDto> workOrderRequestList) {
		this.workOrderRequestList = workOrderRequestList;
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

	public Double getRoundoff() {
		return roundoff;
	}

	public void setRoundoff(Double roundoff) {
		this.roundoff = roundoff;
	}

	private boolean isReverseTax;
	
	public boolean getIsReverseTax(){
		return isReverseTax;
	}
	
	public void setIsReverseTax(boolean isReverseTax){
		this.isReverseTax = isReverseTax;
	}

	
	
	public boolean getWorkOrderCompletion() {
		return workOrderCompletion;
	}

	public void setWorkOrderCompletion(boolean workOrderCompletion) {
		this.workOrderCompletion = workOrderCompletion;
	}

	public Integer getPercentageAmountPaid() {
		return percentageAmountPaid;
	}

	public void setPercentageAmountPaid(Integer percentageAmountPaid) {
		this.percentageAmountPaid = percentageAmountPaid;
	}

	public Integer getWorkOrderCompletionId() {
		return workOrderCompletionId;
	}

	public void setWorkOrderCompletionId(Integer workOrderCompletionId) {
		this.workOrderCompletionId = workOrderCompletionId;
	}

	public Integer getCompletionCount() {
		return completionCount;
	}

	public void setCompletionCount(Integer completionCount) {
		this.completionCount = completionCount;
	}

	public boolean getUpdateStaus() {
		return updateStaus;
	}

	public void setUpdateStaus(boolean updateStaus) {
		this.updateStaus = updateStaus;
	}

	public boolean getIsSettled() {
		return isSettled;
	}

	public void setIsSettled(boolean isSettled) {
		this.isSettled = isSettled;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

}
