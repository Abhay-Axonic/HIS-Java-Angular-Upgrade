package com.param.billing.global.transaction.model;



import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;


@Entity
@Table(name="t_charge_conversion_details", schema="billing")
@SequenceGenerator(name = "t_charge_conversion_details_seq", sequenceName = "billing.t_charge_conversion_details_seq", allocationSize = 1)
public class ChargeConversionDetails {
	@Id
	@Column(name="conversion_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_charge_conversion_details_seq")
	private Integer conversionDetailsId;
	
	@Column(name = "conversion_master_id")
	private Integer conversionMasterId;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "batch_id")
	private Integer batchId;

	@Column(name = "bed_id")
	private Integer bedId;

	@Column(name = "billing_class_id")
	private Integer billingClassId;


	@Column(name = "drug_id")
	private Integer drugId;

	@Column(name = "is_drug", length = 1)
	private String isDrug;

	@Column(name = "is_outsourced", length = 1)
	private String isOutsourced;

	@Column(name = "net_amount", precision = 15, scale = 2)
	private BigDecimal netAmount;

	@Column(name = "old_ord_dtl_id")
	private Integer oldOrdDtlId;
	
	@Column(name = "ord_cancel_datetime")
	@Convert(converter=DateConverter.class)
	private Long ordCancelDatetime;

	@Column(name = "ord_cancel_reason_id")
	private Integer ordCancelReasonId;

	@Column(name = "ord_cancelled", length = 1)
	private String ordCancelled;

	@Column(name = "ord_concession", precision = 15, scale = 2)
	private BigDecimal ordConcession;

	@Column(name = "ord_discount", precision = 15, scale = 2)
	private BigDecimal ordDiscount;

	@Column(name = "ord_doc_id")
	private Integer ordDocId;

	@Column(name = "ord_remarks", length = 2147483647)
	private String ordRemarks;

	@Column(name = "ord_render_datetime")
	@Convert(converter=DateConverter.class)
	private Long ordRenderDatetime;

	@Column(name = "order_date")
	@Convert(converter=DateConverter.class)
	private Long orderDate;

	@Column(name = "order_emergency_flag", length = 1)
	private String orderEmergencyFlag;

	@Column(name = "order_qty")
	private Integer orderQty;
	
	@Column(name = "order_sch_date")
	@Convert(converter=DateConverter.class)
	private Long orderSchDate;
	
	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "priority_id")
	private Integer priorityId;

	@Column(name = "room_id")
	private Integer roomId;

	@Column(name = "service_amt", precision = 15, scale = 2)
	private BigDecimal serviceAmt;

	@Column(name = "service_bill_id")
	private Integer serviceBillId;

	@Column(name = "service_chargeable")
	private Integer serviceChargeable;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "service_is_billed")
	private Integer serviceIsBilled;

	@Column(name = "service_rendered")
	private Integer serviceRendered;

	@Column(name = "service_rendering_deptid")
	private Integer serviceRenderingDeptid;

	@Column(name = "tariff_id")
	private Integer tariffId;

	@Column(name = "ward_id")
	private Integer wardId;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
    @Column(name="is_appointable_radiology")
    private Character isAppointableRadiology;
	
	
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


	public Integer getTariffId() {
		return tariffId;
	}


	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}


	public Integer getWardId() {
		return wardId;
	}


	public void setWardId(Integer wardId) {
		this.wardId = wardId;
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


	public Character getIsAppointableRadiology() {
		return isAppointableRadiology;
	}


	public void setIsAppointableRadiology(Character isAppointableRadiology) {
		this.isAppointableRadiology = isAppointableRadiology;
	}


	
	
}
