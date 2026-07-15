package com.param.global.billing.dto;

import java.util.List;

import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;

public class BillingDetailsDto {

	private Integer billingDetailsId;

	private Integer billingMasterId;

	private Integer serviceId;

	private Double rate;

	private Double quantity;

	private Double concession;

	private Double totalAmount;

	private Double coPayPercentage;

	private Integer unitId;

	private Integer organizationId;

	private Character status;

	private Integer createdBy;

	private Integer updatedBy;

	private Long createdDate;

	private Long updatedDate;

	private Integer serviceMasterId;

	private String serviceStandardName;

	private String serviceStandardCode;

	private Integer specialityId;

	private String specialityName;

	private Integer subSpecialityId;

	private String subSpecialityName;

	private Double unitPrice;

	private Double basePrice;

	private Double netPayable;

	private Double netAmount;

	private Integer orderDetailsId;

	private Double discount;

	private Double discountPercentage;

	private Integer taxId;

	private Double taxPer;

	private Double taxAmount;

	private Double netAmt;

	private Integer ordDocId;

	private Integer ordDocSplId;

	private Integer drugId;

	private Integer batchId;

	private Long orderDate;

	private Long orderSchDate;

	private Double selfPayable;

	private Double creditPayable;

	private Integer payeeId;
	
	private Integer associateCompanyId;

	private Integer packageId;

	private Double packageConsumptionAmt;

	private Double coSharePer;

	private Character ordCancelled;

	private Character orderEmergencyFlag;

	private Character isDrug;

	private String cancelRemark;

	private Double grossAmount;

	private Character isDoctorSharePosted;

	// added for transaction log
	private String transtype;
	private String transno;
	private Integer createdUserId;
	private Integer updatedUserId;
	private String createdUserName;
	private String updatedUserName;
	private String fromDateTransaction;
	private String toDateTransaction;
	private String txnCreatedDate;
	private String txnUpdatedDate;
	private Integer offset;
	private Integer noOfRecords;
	private Integer oldBillDetailId;
	private Integer crNoteId;
	private Character isCancelled;
	private Double coPayPer;
	private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;
	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;
	private Double crNetAmount;
	
	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}

	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}

	public Double getCoPayPer() {
		return coPayPer;
	}

	public void setCoPayPer(Double coPayPer) {
		this.coPayPer = coPayPer;
	}

	public Character getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Character isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Integer getOldBillDetailId() {
		return oldBillDetailId;
	}

	public void setOldBillDetailId(Integer oldBillDetailId) {
		this.oldBillDetailId = oldBillDetailId;
	}

	public Integer getCrNoteId() {
		return crNoteId;
	}

	public void setCrNoteId(Integer crNoteId) {
		this.crNoteId = crNoteId;
	}

	public Character getIsDoctorSharePosted() {
		return isDoctorSharePosted;
	}

	public void setIsDoctorSharePosted(Character isDoctorSharePosted) {
		this.isDoctorSharePosted = (isDoctorSharePosted == null || isDoctorSharePosted == '\u0000') ? 'N'
				: isDoctorSharePosted;
	}

	public Double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public String getCancelRemark() {
		return cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
	}

	public Character getOrdCancelled() {
		return ordCancelled;
	}

	public void setOrdCancelled(Character ordCancelled) {
		this.ordCancelled = ordCancelled;
	}

	public Character getOrderEmergencyFlag() {
		return orderEmergencyFlag;
	}

	public void setOrderEmergencyFlag(Character orderEmergencyFlag) {
		this.orderEmergencyFlag = orderEmergencyFlag;
	}

	public Character getIsDrug() {
		return isDrug;
	}

	public void setIsDrug(Character isDrug) {
		this.isDrug = isDrug;
	}

	public Integer getBillingDetailsId() {
		return billingDetailsId;
	}

	public void setBillingDetailsId(Integer billingDetailsId) {
		this.billingDetailsId = billingDetailsId;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getCoPayPercentage() {
		return coPayPercentage;
	}

	public void setCoPayPercentage(Double coPayPercentage) {
		this.coPayPercentage = coPayPercentage;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getNetPayable() {
		return netPayable;
	}

	public void setNetPayable(Double netPayable) {
		this.netPayable = netPayable;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPer() {
		return taxPer;
	}

	public void setTaxPer(Double taxPer) {
		this.taxPer = taxPer;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(Double netAmt) {
		this.netAmt = netAmt;
	}

	public Integer getOrdDocId() {
		return ordDocId;
	}

	public void setOrdDocId(Integer ordDocId) {
		this.ordDocId = ordDocId;
	}

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}

	public Integer getDrugId() {
		return drugId;
	}

	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Double getCoSharePer() {
		return coSharePer;
	}

	public void setCoSharePer(Double coSharePer) {
		this.coSharePer = coSharePer;
	}

	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	public String getTransno() {
		return transno;
	}

	public void setTransno(String transno) {
		this.transno = transno;
	}

	public Integer getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Integer createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Integer getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Integer updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public String getUpdatedUserName() {
		return updatedUserName;
	}

	public void setUpdatedUserName(String updatedUserName) {
		this.updatedUserName = updatedUserName;
	}

	public String getFromDateTransaction() {
		return fromDateTransaction;
	}

	public void setFromDateTransaction(String fromDateTransaction) {
		this.fromDateTransaction = fromDateTransaction;
	}

	public String getToDateTransaction() {
		return toDateTransaction;
	}

	public void setToDateTransaction(String toDateTransaction) {
		this.toDateTransaction = toDateTransaction;
	}

	public String getTxnCreatedDate() {
		return txnCreatedDate;
	}

	public void setTxnCreatedDate(String txnCreatedDate) {
		this.txnCreatedDate = txnCreatedDate;
	}

	public String getTxnUpdatedDate() {
		return txnUpdatedDate;
	}

	public void setTxnUpdatedDate(String txnUpdatedDate) {
		this.txnUpdatedDate = txnUpdatedDate;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecords() {
		return noOfRecords;
	}

	public void setNoOfRecords(Integer noOfRecords) {
		this.noOfRecords = noOfRecords;
	}

	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		return orderDetailsPackageMapperDtosList;
	}

	public void setOrderDetailsPackageMapperDtosList(
			List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}

}
