package com.param.billing.global.transaction.dto;

import java.util.List;

public class TDoctorShareReqDto {
	
	private Integer doctorShareId;
	private Integer billDtlId;
	private Integer visitTypeId;
	private Integer patientId;
	private Integer billingMasterId;
	private String  visitTypeCode;
	private String doctorName;
	private String specialityName;
	private String  patientName;
	private String orderNo;
	private Long orderDate;
	private Long  orderSchDate;
	private String serviceStandardCode;
	private String serviceStandardName;
	private Integer ordDocSplId;
	private Integer taxId;
	private Integer ordDtlId;
	private Integer visitId;
	private Integer admissionId;
	private Integer serviceId;
	private Integer unitRate;
	private Double totalAmount;
	private Double netAmount;
	private Long billDateTime;
	private Long encounterDate;
	private String encounterNumber;
	private Integer taxType;
	private Integer docId;
	private Integer docSpecId;
	private Integer sharePer;
	private Integer hospShare;
	private Integer docShare;
	private Double processedShareAmt;
	private Integer isBillCrDr;
	private Integer isActive;
	private Integer isOnBillPayment;
	private Double discount;
	private Double concession;
	private Double totalBillAmount;
	private Double taxAmt;
	private Double roundOff;
	private String billNumber;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private Long processedShareDate;
	private Long dueShareDate;
	private Integer docShareVoucherId;
	private Integer referralShareId;
	private String fromDate;
	private String toDate;
	private String remark;
	private Integer doctorId;
	private Integer selectedServiceType;
	private Integer selectedSubServiceType;
	private Integer orderServiceId;
	private Integer referralId;
	private Character isProcessShareAgainstSettledBill;
	private Integer billStatusId;
	private List<Integer> referralIds;
	private List<Integer> doctorIds;
	private Integer companyTypeId;
	private Integer companyId;
	private Integer associateCompanyId;
	private Integer offset;
	private Integer recordPerPage;
	private Integer serviceRendered;
	private Integer settlementOrInvoice;
	
	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public List<Integer> getReferralIds() {
		return referralIds;
	}

	public void setReferralIds(List<Integer> referralIds) {
		this.referralIds = referralIds;
	}

	public List<Integer> getDoctorIds() {
		return doctorIds;
	}

	public void setDoctorIds(List<Integer> doctorIds) {
		this.doctorIds = doctorIds;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Character getIsProcessShareAgainstSettledBill() {
		return isProcessShareAgainstSettledBill;
	}

	public void setIsProcessShareAgainstSettledBill(Character isProcessShareAgainstSettledBill) {
		this.isProcessShareAgainstSettledBill = isProcessShareAgainstSettledBill;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Integer getDoctorShareId() {
		return doctorShareId;
	}

	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}

	public Integer getBillDtlId() {
		return billDtlId;
	}

	public void setBillDtlId(Integer billDtlId) {
		this.billDtlId = billDtlId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getOrdDtlId() {
		return ordDtlId;
	}

	public void setOrdDtlId(Integer ordDtlId) {
		this.ordDtlId = ordDtlId;
	}

	public Integer getVisitId() {
		return visitId;
	}

	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(Integer unitRate) {
		this.unitRate = unitRate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Long getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Long billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getTaxType() {
		return taxType;
	}

	public void setTaxType(Integer taxType) {
		this.taxType = taxType;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getDocSpecId() {
		return docSpecId;
	}

	public void setDocSpecId(Integer docSpecId) {
		this.docSpecId = docSpecId;
	}

	public Integer getSharePer() {
		return sharePer;
	}

	public void setSharePer(Integer sharePer) {
		this.sharePer = sharePer;
	}

	public Integer getHospShare() {
		return hospShare;
	}

	public void setHospShare(Integer hospShare) {
		this.hospShare = hospShare;
	}

	public Integer getDocShare() {
		return docShare;
	}

	public void setDocShare(Integer docShare) {
		this.docShare = docShare;
	}

	public Double getProcessedShareAmt() {
		return processedShareAmt;
	}

	public void setProcessedShareAmt(Double processedShareAmt) {
		this.processedShareAmt = processedShareAmt;
	}

	public Integer getIsBillCrDr() {
		return isBillCrDr;
	}

	public void setIsBillCrDr(Integer isBillCrDr) {
		this.isBillCrDr = isBillCrDr;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsOnBillPayment() {
		return isOnBillPayment;
	}

	public void setIsOnBillPayment(Integer isOnBillPayment) {
		this.isOnBillPayment = isOnBillPayment;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(Double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}


	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public String getVisitTypeCode() {
		return visitTypeCode;
	}

	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}

	public Long getOrderSchDate() {
		return orderSchDate;
	}

	public void setOrderSchDate(Long orderSchDate) {
		this.orderSchDate = orderSchDate;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}

	public Long getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Long encounterDate) {
		this.encounterDate = encounterDate;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Long getProcessedShareDate() {
		return processedShareDate;
	}

	public void setProcessedShareDate(Long processedShareDate) {
		this.processedShareDate = processedShareDate;
	}

	public Long getDueShareDate() {
		return dueShareDate;
	}

	public void setDueShareDate(Long dueShareDate) {
		this.dueShareDate = dueShareDate;
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

	public Integer getDocShareVoucherId() {
		return docShareVoucherId;
	}

	public void setDocShareVoucherId(Integer docShareVoucherId) {
		this.docShareVoucherId = docShareVoucherId;
	}

	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSelectedServiceType() {
		return selectedServiceType;
	}

	public void setSelectedServiceType(Integer selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}

	public Integer getSelectedSubServiceType() {
		return selectedSubServiceType;
	}

	public void setSelectedSubServiceType(Integer selectedSubServiceType) {
		this.selectedSubServiceType = selectedSubServiceType;
	}

	public Integer getOrderServiceId() {
		return orderServiceId;
	}

	public void setOrderServiceId(Integer orderServiceId) {
		this.orderServiceId = orderServiceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public Integer getSettlementOrInvoice() {
		return settlementOrInvoice;
	}

	public void setSettlementOrInvoice(Integer settlementOrInvoice) {
		this.settlementOrInvoice = settlementOrInvoice;
	}


	
}
