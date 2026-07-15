package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TDoctorShareRespDto {
	
	private Integer doctorShareId;
	private Integer billDtlId;
	private Integer billId;
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
	private Double unitRate;
	private Double totalAmount;
	private Double netAmount;
	private String billDateTime;
	private String encounterDate;
	private String encounterNumber;
	private Integer taxType;
	private Integer docId;
	private Integer docSpecId;
	private Double sharePer;
	private Double hospShare;
	private Double docShare;
	private Double processedShareAmt;
	private Double dueShareAmt;
	private Integer isBillCrDr;
	private Integer isActive;
	private Integer isOnBillPayment;
	private Double discount;
	private Double concession;
	private Double totalBillAmount;
	private Double taxAmt;
	private Double taxPer;
	private Double roundOff;
	private String billNumber;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private String uhidNumber;
	private Long processedShareDate;
	private Long dueShareDate;
	private Integer quantity;
	private String voucherDate;
	private String voucherNo;
	private Integer docShareVoucherId;
	private Integer typeId;
	private Integer referralShareId;
	private String remark;
	private Double disallowedAmount;
	private Character isShareSplit;
	private Integer doctorShareSplitId;
	private Double oldsharAmt;
	private Double newsharAmt;
	private Double newSharePercentage;
	private Double oldSharePercentage;
	private Integer retainershipFee;
	private Double oldSharePer;
	private Double oldHospShare;
	private Double shareAmount;
	private Integer priority;
	private Double totalDocShare;
	private Integer parentDocShareId;
	private Integer voucherDtlsId;
	private Double doctorCredit;
	private Double consumedDoctorCredit;
	private Double oldDoctorCredit;
	private Double paidAmount;
	private Double voucherProcessedAmt;
	private Double voucherSharePer;
	private int isDocShare;
	private Integer referralId;
	private String referralName;
	private Integer referralTypeId;
	private String creditNoteNo;
	private List<DoctorShareCrNoteDetailsDto> docShareCrDetailsList;
	private Integer refDocShareId;
	private Double refDocShare;
	private Double refProcessedShareAmt;
	private Double refSharePer;
	private Integer againstCrId;
	private String admissionDate;
	private String admissionNumber;
	private Double oldDocShare;
	private Double oldDueShareAmt;
	private Integer docDocShareId;
	private Integer doctorSlabTransactionId;
	private Integer parentRevenueId;
	private BigDecimal doctorRevenue;
	private Integer splitDocId;
	private BigDecimal oldDoctorRevenue;
	private BigDecimal newDoctorRevenue;
	private BigDecimal newRevenuePercentage;
	private BigDecimal oldRevenuePercentage;
	private BigDecimal revenuePercentage;
	private Double consumedDoctorAdvance;
	private Double doctorAdvance;
	private Double totalReferralCredit;
	private Double totalDoctorCredit;
	private Double retainerShipFee;
	private Integer tdsAmt;
	private Integer serviceRendered;
	private Double crNetAmount;
	private Date dueDate;
	private Double packageConsumptionAmt;
	

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Double getTaxPer() {
		return taxPer;
	}

	public void setTaxPer(Double taxPer) {
		this.taxPer = taxPer;
	}

	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public Integer getTdsAmt() {
		return tdsAmt;
	}

	public void setTdsAmt(Integer tdsAmt) {
		this.tdsAmt = tdsAmt;
	}

	public BigDecimal getRevenuePercentage() {
		return revenuePercentage;
	}

	public void setRevenuePercentage(BigDecimal revenuePercentage) {
		this.revenuePercentage = revenuePercentage;
	}
	
	public BigDecimal getOldDoctorRevenue() {
		return oldDoctorRevenue;
	}

	public void setOldDoctorRevenue(BigDecimal oldDoctorRevenue) {
		this.oldDoctorRevenue = oldDoctorRevenue;
	}

	public BigDecimal getNewDoctorRevenue() {
		return newDoctorRevenue;
	}

	public void setNewDoctorRevenue(BigDecimal newDoctorRevenue) {
		this.newDoctorRevenue = newDoctorRevenue;
	}

	public BigDecimal getNewRevenuePercentage() {
		return newRevenuePercentage;
	}

	public void setNewRevenuePercentage(BigDecimal newRevenuePercentage) {
		this.newRevenuePercentage = newRevenuePercentage;
	}

	public BigDecimal getOldRevenuePercentage() {
		return oldRevenuePercentage;
	}

	public void setOldRevenuePercentage(BigDecimal oldRevenuePercentage) {
		this.oldRevenuePercentage = oldRevenuePercentage;
	}

	public Integer getDoctorSlabTransactionId() {
		return doctorSlabTransactionId;
	}

	public void setDoctorSlabTransactionId(Integer doctorSlabTransactionId) {
		this.doctorSlabTransactionId = doctorSlabTransactionId;
	}

	public Integer getParentRevenueId() {
		return parentRevenueId;
	}

	public void setParentRevenueId(Integer parentRevenueId) {
		this.parentRevenueId = parentRevenueId;
	}

	public BigDecimal getDoctorRevenue() {
		return doctorRevenue;
	}

	public void setDoctorRevenue(BigDecimal doctorRevenue) {
		this.doctorRevenue = doctorRevenue;
	}

	public Integer getSplitDocId() {
		return splitDocId;
	}

	public void setSplitDocId(Integer splitDocId) {
		this.splitDocId = splitDocId;
	}

	public Integer getDocDocShareId() {
		return docDocShareId;
	}

	public void setDocDocShareId(Integer docDocShareId) {
		this.docDocShareId = docDocShareId;
	}

	public Double getOldDueShareAmt() {
		return oldDueShareAmt;
	}

	public void setOldDueShareAmt(Double oldDueShareAmt) {
		this.oldDueShareAmt = oldDueShareAmt;
	}

	public Double getOldDocShare() {
		return oldDocShare;
	}

	public void setOldDocShare(Double oldDocShare) {
		this.oldDocShare = oldDocShare;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public Integer getAgainstCrId() {
		return againstCrId;
	}

	public void setAgainstCrId(Integer againstCrId) {
		this.againstCrId = againstCrId;
	}

	public Integer getRefDocShareId() {
		return refDocShareId;
	}

	public void setRefDocShareId(Integer refDocShareId) {
		this.refDocShareId = refDocShareId;
	}

	public Double getRefDocShare() {
		return refDocShare;
	}

	public void setRefDocShare(Double refDocShare) {
		this.refDocShare = refDocShare;
	}

	public Double getRefProcessedShareAmt() {
		return refProcessedShareAmt;
	}

	public void setRefProcessedShareAmt(Double refProcessedShareAmt) {
		this.refProcessedShareAmt = refProcessedShareAmt;
	}
	
	public Double getRefSharePer() {
		return refSharePer;
	}

	public void setRefSharePer(Double refSharePer) {
		this.refSharePer = refSharePer;
	}

	public String getCreditNoteNo() {
		return creditNoteNo;
	}

	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}

	public Double getVoucherSharePer() {
		return voucherSharePer;
	}

	public void setVoucherSharePer(Double voucherSharePer) {
		this.voucherSharePer = voucherSharePer;
	}

	public Double getVoucherProcessedAmt() {
		return voucherProcessedAmt;
	}

	public void setVoucherProcessedAmt(Double voucherProcessedAmt) {
		this.voucherProcessedAmt = voucherProcessedAmt;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public Integer getReferralTypeId() {
		return referralTypeId;
	}

	public void setReferralTypeId(Integer referralTypeId) {
		this.referralTypeId = referralTypeId;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}


	public int getIsDocShare() {
		return isDocShare;
	}

	public void setIsDocShare(int isDocShare) {
		this.isDocShare = isDocShare;
	}

	public Double getConsumedDoctorCredit() {
		return consumedDoctorCredit;
	}

	public void setConsumedDoctorCredit(Double consumedDoctorCredit) {
		this.consumedDoctorCredit = consumedDoctorCredit;
	}

	public List<DoctorShareCrNoteDetailsDto> getDocShareCrDetailsList() {
		return docShareCrDetailsList;
	}

	public void setDocShareCrDetailsList(List<DoctorShareCrNoteDetailsDto> docShareCrDetailsList) {
		this.docShareCrDetailsList = docShareCrDetailsList;
	}
	
	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getOldDoctorCredit() {
		return oldDoctorCredit;
	}

	public void setOldDoctorCredit(Double oldDoctorCredit) {
		this.oldDoctorCredit = oldDoctorCredit;
	}

	public Double getDoctorCredit() {
		return doctorCredit;
	}

	public void setDoctorCredit(Double doctorCredit) {
		this.doctorCredit = doctorCredit;
	}

	public Integer getVoucherDtlsId() {
		return voucherDtlsId;
	}

	public void setVoucherDtlsId(Integer voucherDtlsId) {
		this.voucherDtlsId = voucherDtlsId;
	}

	public Integer getParentDocShareId() {
		return parentDocShareId;
	}

	public void setParentDocShareId(Integer parentDocShareId) {
		this.parentDocShareId = parentDocShareId;
	}

	public Double getTotalDocShare() {
		return totalDocShare;
	}

	public void setTotalDocShare(Double totalDocShare) {
		this.totalDocShare = totalDocShare;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Double getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(Double shareAmount) {
		this.shareAmount = shareAmount;
	}

	public Double getOldSharePer() {
		return oldSharePer;
	}

	public void setOldSharePer(Double oldSharePer) {
		this.oldSharePer = oldSharePer;
	}

	public Double getOldHospShare() {
		return oldHospShare;
	}

	public void setOldHospShare(Double oldHospShare) {
		this.oldHospShare = oldHospShare;
	}

	public Integer getRetainershipFee() {
		return retainershipFee;
	}

	public void setRetainershipFee(Integer retainershipFee) {
		this.retainershipFee = retainershipFee;
	}

	public Double getOldsharAmt() {
		return oldsharAmt;
	}

	public void setOldsharAmt(Double oldsharAmt) {
		this.oldsharAmt = oldsharAmt;
	}

	public Double getNewsharAmt() {
		return newsharAmt;
	}

	public void setNewsharAmt(Double newsharAmt) {
		this.newsharAmt = newsharAmt;
	}

	public Double getNewSharePercentage() {
		return newSharePercentage;
	}

	public void setNewSharePercentage(Double newSharePercentage) {
		this.newSharePercentage = newSharePercentage;
	}

	public Double getOldSharePercentage() {
		return oldSharePercentage;
	}

	public void setOldSharePercentage(Double oldSharePercentage) {
		this.oldSharePercentage = oldSharePercentage;
	}

	public Integer getDoctorShareSplitId() {
		return doctorShareSplitId;
	}

	public void setDoctorShareSplitId(Integer doctorShareSplitId) {
		this.doctorShareSplitId = doctorShareSplitId;
	}

	public Character getIsShareSplit() {
		return isShareSplit;
	}

	public void setIsShareSplit(Character isShareSplit) {
		this.isShareSplit = isShareSplit;
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

	public Double getUnitRate() {
		return unitRate;
	}

	public void setUnitRate(Double unitRate) {
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

	public Double getSharePer() {
		return sharePer;
	}

	public void setSharePer(Double sharePer) {
		this.sharePer = sharePer;
	}

	public Double getHospShare() {
		return hospShare;
	}

	public void setHospShare(Double hospShare) {
		this.hospShare = hospShare;
	}

	public Double getDocShare() {
		return docShare;
	}

	public void setDocShare(Double docShare) {
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

	public Character getStatus() {
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

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Double getDueShareAmt() {
		return dueShareAmt;
	}

	public void setDueShareAmt(Double dueShareAmt) {
		this.dueShareAmt = dueShareAmt;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Integer getDocShareVoucherId() {
		return docShareVoucherId;
	}

	public void setDocShareVoucherId(Integer docShareVoucherId) {
		this.docShareVoucherId = docShareVoucherId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public String getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(String billDateTime) {
		this.billDateTime = billDateTime;
	}

	public String getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}

	public Double getConsumedDoctorAdvance() {
		return consumedDoctorAdvance;
	}

	public void setConsumedDoctorAdvance(Double consumedDoctorAdvance) {
		this.consumedDoctorAdvance = consumedDoctorAdvance;
	}

	public Double getDoctorAdvance() {
		return doctorAdvance;
	}

	public void setDoctorAdvance(Double doctorAdvance) {
		this.doctorAdvance = doctorAdvance;
	}

	public Double getTotalReferralCredit() {
		return totalReferralCredit;
	}

	public void setTotalReferralCredit(Double totalReferralCredit) {
		this.totalReferralCredit = totalReferralCredit;
	}

	public Double getTotalDoctorCredit() {
		return totalDoctorCredit;
	}

	public void setTotalDoctorCredit(Double totalDoctorCredit) {
		this.totalDoctorCredit = totalDoctorCredit;
	}

	public Double getRetainerShipFee() {
		return retainerShipFee;
	}

	public void setRetainerShipFee(Double retainerShipFee) {
		this.retainerShipFee = retainerShipFee;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
}
