package com.param.billing.global.transaction.config.dto;

import java.math.BigDecimal;

public class ChargeConversionDetailsDto {

	private BigDecimal convertAmt;
	private Integer conversionDetailsId;
	
	private Character isAppointableRadiology;
	private Integer conversionMasterId;
	
	
	private Integer orderDetailsId;
	
	
	private Integer batchId;

	
	private Integer bedId;

	
	private Integer billingClassId;


	
	private Integer drugId;

	
	private String isDrug;

	
	private String isOutsourced;

	private BigDecimal netAmount;

	private Integer oldOrdDtlId;
	
	private Long ordCancelDatetime;

	
	private Integer ordCancelReasonId;

	
	private String ordCancelled;

	
	private BigDecimal ordConcession;

	
	private BigDecimal ordDiscount;

	
	private Integer ordDocId;

	
	private String ordRemarks;

	
	private Long ordRenderDatetime;


	private Long orderDate;


	private String orderEmergencyFlag;

	
	private Integer orderQty;
	
	
	private Long orderSchDate;
	
	
	private Integer packageId;

	
	private Integer priorityId;

	
	private Integer roomId;

	
	private BigDecimal serviceAmt;

	
	private Integer serviceBillId;

	
	private Integer serviceChargeable;

	
	private Integer serviceId;

	
	private Integer serviceIsBilled;

	
	private Integer serviceRendered;

	
	private Integer serviceRenderingDeptid;

	
	private Integer tariffId;

	
	private Integer wardId;
	
	
	private Integer orgUnitId;

	
	private Integer orgId;

	
	private Character status;

	
	private Long createdDate;

	
	private Integer createdBy;

	
	private Long updatedDate;

	
	private Integer updatedBy;
	public Integer getConversionDetailsId() {
		return conversionDetailsId;
	}
	public void setConversionDetailsId(Integer conversionDetailsId) {
		this.conversionDetailsId = conversionDetailsId;
	}
	public Integer getConversionMasterId() {
		return conversionMasterId;
	}
	public void setConversionMasterId(Integer conversionMasterId) {
		this.conversionMasterId = conversionMasterId;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getBedId() {
		return bedId;
	}
	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}
	public Integer getBillingClassId() {
		return billingClassId;
	}
	public void setBillingClassId(Integer billingClassId) {
		this.billingClassId = billingClassId;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getDrugId() {
		return drugId;
	}
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	public String getIsDrug() {
		return isDrug;
	}
	public void setIsDrug(String isDrug) {
		this.isDrug = isDrug;
	}
	public String getIsOutsourced() {
		return isOutsourced;
	}
	public void setIsOutsourced(String isOutsourced) {
		this.isOutsourced = isOutsourced;
	}
	public BigDecimal getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}
	public Integer getOldOrdDtlId() {
		return oldOrdDtlId;
	}
	public void setOldOrdDtlId(Integer oldOrdDtlId) {
		this.oldOrdDtlId = oldOrdDtlId;
	}
	public Long getOrdCancelDatetime() {
		return ordCancelDatetime;
	}
	public void setOrdCancelDatetime(Long ordCancelDatetime) {
		this.ordCancelDatetime = ordCancelDatetime;
	}
	public Integer getOrdCancelReasonId() {
		return ordCancelReasonId;
	}
	public void setOrdCancelReasonId(Integer ordCancelReasonId) {
		this.ordCancelReasonId = ordCancelReasonId;
	}
	public String getOrdCancelled() {
		return ordCancelled;
	}
	public void setOrdCancelled(String ordCancelled) {
		this.ordCancelled = ordCancelled;
	}
	public BigDecimal getOrdConcession() {
		return ordConcession;
	}
	public void setOrdConcession(BigDecimal ordConcession) {
		this.ordConcession = ordConcession;
	}
	public BigDecimal getOrdDiscount() {
		return ordDiscount;
	}
	public void setOrdDiscount(BigDecimal ordDiscount) {
		this.ordDiscount = ordDiscount;
	}
	public Integer getOrdDocId() {
		return ordDocId;
	}
	public void setOrdDocId(Integer ordDocId) {
		this.ordDocId = ordDocId;
	}
	public String getOrdRemarks() {
		return ordRemarks;
	}
	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
	}
	public Long getOrdRenderDatetime() {
		return ordRenderDatetime;
	}
	public void setOrdRenderDatetime(Long ordRenderDatetime) {
		this.ordRenderDatetime = ordRenderDatetime;
	}
	public Long getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderEmergencyFlag() {
		return orderEmergencyFlag;
	}
	public void setOrderEmergencyFlag(String orderEmergencyFlag) {
		this.orderEmergencyFlag = orderEmergencyFlag;
	}
	public Integer getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}
	public Long getOrderSchDate() {
		return orderSchDate;
	}
	public void setOrderSchDate(Long orderSchDate) {
		this.orderSchDate = orderSchDate;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getOrgUnitId() {
		return orgUnitId;
	}
	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public BigDecimal getServiceAmt() {
		return serviceAmt;
	}
	public void setServiceAmt(BigDecimal serviceAmt) {
		this.serviceAmt = serviceAmt;
	}
	public Integer getServiceBillId() {
		return serviceBillId;
	}
	public void setServiceBillId(Integer serviceBillId) {
		this.serviceBillId = serviceBillId;
	}
	public Integer getServiceChargeable() {
		return serviceChargeable;
	}
	public void setServiceChargeable(Integer serviceChargeable) {
		this.serviceChargeable = serviceChargeable;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getServiceIsBilled() {
		return serviceIsBilled;
	}
	public void setServiceIsBilled(Integer serviceIsBilled) {
		this.serviceIsBilled = serviceIsBilled;
	}
	public Integer getServiceRendered() {
		return serviceRendered;
	}
	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}
	public Integer getServiceRenderingDeptid() {
		return serviceRenderingDeptid;
	}
	public void setServiceRenderingDeptid(Integer serviceRenderingDeptid) {
		this.serviceRenderingDeptid = serviceRenderingDeptid;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Long getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getWardId() {
		return wardId;
	}
	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	public Character getIsAppointableRadiology() {
		return isAppointableRadiology;
	}
	public void setIsAppointableRadiology(Character isAppointableRadiology) {
		this.isAppointableRadiology = isAppointableRadiology;
	}
	public BigDecimal getConvertAmt() {
		return convertAmt;
	}
	public void setConvertAmt(BigDecimal convertAmt) {
		this.convertAmt = convertAmt;
	}
	
}
