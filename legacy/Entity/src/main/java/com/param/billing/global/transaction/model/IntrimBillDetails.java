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
@Table(name = "t_intrim_bill_details", schema = "billing")
@SequenceGenerator(name = "t_intrim_bill_details_seq", sequenceName = "billing.t_intrim_bill_details_seq", allocationSize = 1)
public class IntrimBillDetails {

	@Id
	@Column(name = "intrim_bill_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_intrim_bill_details_seq")
	private Integer intrimBillDetailsId;
  
	@Column(name = "order_details_id")
	private Integer orderDetailsId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "order_id")
	private Integer orderId;
  
	@Column(name = "intrim_bill_id")
	private Integer intrimBillId;

	@Column(name = "order_qty")
	private Integer orderQty;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "priority_id")
	private Integer priorityId;

	@Column(name = "is_outsourced")
	private Character isOutsourced;

	@Column(name = "order_date")
	@Convert(converter = DateConverter.class)
	private Long orderDate;

	@Column(name = "order_sch_date")
	@Convert(converter = DateConverter.class)
	private Long orderSchDate;

	@Column(name = "service_amt")
	private BigDecimal serviceAmt;

	@Column(name = "ord_concession")
	private BigDecimal ordConcession;

	@Column(name = "ord_discount")
	private BigDecimal ordDiscount;

	@Column(name = "net_amount")
	private BigDecimal netAmount;
  
	@Column(name="ord_doc_spl_id")
	private Integer ordDocSplId;

	@Column(name="tax_id")
	private Integer taxId;
  
  @Column(name="tax_per")
  private BigDecimal taxPer;
  
  @Column(name="tax_amount")
  private BigDecimal taxAmount;
  
  @Column(name="self_payable")
  private BigDecimal selfPayable;
  
  @Column(name="credit_payable")
  private BigDecimal creditPayable;
  
  @Column(name="payee_id")
  private Integer payeeId;
  
  @Column(name="package_consumption_amt")
  private BigDecimal packageConsumptionAmt;
  
  @Column(name="order_set_id")
  private Integer orderSetId;
  
  @Column(name="contract_id")
  private Integer contractId;
  
  @Column(name="bed_category_id")
  private Integer bedCategoryId;
  
  @Column(name="co_pay_per")
  private BigDecimal coPayPer;
  
  @Column(name="doc_share_per")
  private BigDecimal docSharePer;
  
  @Column(name="ord_total_amt")
  private BigDecimal ordTotalAmt;
  
  @Column(name="ord_cancel_by")
  private Integer ordCancelBy;
  
  @Column(name="ord_cancel_remark")
  private String ordCancelRemark;
  
  @Column(name = "tariff_id")
  private Integer tariffId;

  @Column(name = "billing_class_id")
  private Integer billingClassId;

  @Column(name = "bed_id")
  private Integer bedId;

  @Column(name = "room_id")
  private Integer roomId;

  @Column(name = "ward_id")
  private Integer wardId;

  @Column(name = "order_emergency_flag")
  private Character orderEmergencyFlag;

  @Column(name = "package_id")
  private Integer packageId;

  @Column(name = "ord_doc_id")
  private Integer ordDocId;

  @Column(name = "drug_id")
  private Integer drugId;

  @Column(name = "batch_id")
  private Integer batchId;

  @Column(name = "old_ord_dtl_id")
  private Integer oldOrdDtlId;

  @Column(name = "service_rendered")
  private Integer serviceRendered;

  @Column(name = "service_chargeable")
  private Integer serviceChargeable;

  @Column(name = "service_rendering_deptid")
  private Integer serviceRenderingDeptId;

  @Column(name = "ord_render_datetime")
  @Convert(converter = DateConverter.class)
  private Long ordRenderDatetime;

  @Column(name = "service_is_billed")
  private Integer serviceIsBilled;

  @Column(name = "service_bill_id")
  private Integer serviceBillId;

  @Column(name = "ord_remarks")
  private String ordRemarks;

  @Column(name = "created_date")
  @Convert(converter = DateConverter.class)
  private Long createdDate;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "ord_cancelled")
  private Character ordCancelled;

  @Column(name = "ord_cancel_reason_id")
  private Integer ordCancelReasonId;

  @Column(name = "ord_cancel_datetime")
  @Convert(converter = DateConverter.class)
  private Long ordCancelDatetime;
  
  @Column(name = "status")
  private Character status;

  @Column(name = "updated_by")
  private Integer updatedBy;

  @Column(name = "updated_date")
  @Convert(converter = DateConverter.class)
  private Long updatedDate;
  
  @Column(name="is_drug")
  private Character isDrug;
  
  @Column(name = "is_package_service")
  private char isPackageService;
  
  @Column(name="is_investigation_order")
  private Character isInvestigationOrder;

  @Column(name = "is_package")
  private boolean isPackage;
  
  @Column(name = "is_cap_service")
  private char isCapService;
  
  @Column(name="is_procedure")
  private char isProcedure;
  
  @Column(name="is_procedure_service")
  private char isProcedureService;
  
  @Column(name="procedure_order_mst_id")
  private Integer procedureOrderMstId;
  
  @Column(name="is_appointable_radiology")
  private Character isAppointableRadiology;
  
  @Column(name = "package_either_or_group_id")
  private Integer packageEitherOrGroupId;
  
  @Column(name = "sale_detail_id")
  private Integer saleDetailId;
  
  @Column(name = "debit_note_service")
  private boolean debitNoteService;
  
  @Column(name = "patient_class_percentage")
  private BigDecimal patientClassPercentage;
  
  	@Column(name = "markup_status")
  	private Boolean markupStatus;

	public Integer getIntrimBillId() {
		return intrimBillId;
	}

	public void setIntrimBillId(Integer intrimBillId) {
		this.intrimBillId = intrimBillId;
	}

	public Integer getIntrimBillDetailsId() {
		return intrimBillDetailsId;
	}

	public void setIntrimBillDetailsId(Integer intrimBillDetailsId) {
		this.intrimBillDetailsId = intrimBillDetailsId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Character getIsOutsourced() {
		return isOutsourced;
	}

	public void setIsOutsourced(Character isOutsourced) {
		this.isOutsourced = isOutsourced;
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

	public BigDecimal getServiceAmt() {
		return serviceAmt;
	}

	public void setServiceAmt(BigDecimal serviceAmt) {
		this.serviceAmt = serviceAmt;
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

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public BigDecimal getTaxPer() {
		return taxPer;
	}

	public void setTaxPer(BigDecimal taxPer) {
		this.taxPer = taxPer;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(BigDecimal selfPayable) {
		this.selfPayable = selfPayable;
	}

	public BigDecimal getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(BigDecimal creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public BigDecimal getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(BigDecimal packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Integer getOrderSetId() {
		return orderSetId;
	}

	public void setOrderSetId(Integer orderSetId) {
		this.orderSetId = orderSetId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public BigDecimal getCoPayPer() {
		return coPayPer;
	}

	public void setCoPayPer(BigDecimal coPayPer) {
		this.coPayPer = coPayPer;
	}

	public BigDecimal getDocSharePer() {
		return docSharePer;
	}

	public void setDocSharePer(BigDecimal docSharePer) {
		this.docSharePer = docSharePer;
	}

	public BigDecimal getOrdTotalAmt() {
		return ordTotalAmt;
	}

	public void setOrdTotalAmt(BigDecimal ordTotalAmt) {
		this.ordTotalAmt = ordTotalAmt;
	}

	public Integer getOrdCancelBy() {
		return ordCancelBy;
	}

	public void setOrdCancelBy(Integer ordCancelBy) {
		this.ordCancelBy = ordCancelBy;
	}

	public String getOrdCancelRemark() {
		return ordCancelRemark;
	}

	public void setOrdCancelRemark(String ordCancelRemark) {
		this.ordCancelRemark = ordCancelRemark;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getBillingClassId() {
		return billingClassId;
	}

	public void setBillingClassId(Integer billingClassId) {
		this.billingClassId = billingClassId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Character getOrderEmergencyFlag() {
		return orderEmergencyFlag;
	}

	public void setOrderEmergencyFlag(Character orderEmergencyFlag) {
		this.orderEmergencyFlag = orderEmergencyFlag;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getOrdDocId() {
		return ordDocId;
	}

	public void setOrdDocId(Integer ordDocId) {
		this.ordDocId = ordDocId;
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

	public Integer getOldOrdDtlId() {
		return oldOrdDtlId;
	}

	public void setOldOrdDtlId(Integer oldOrdDtlId) {
		this.oldOrdDtlId = oldOrdDtlId;
	}

	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public Integer getServiceChargeable() {
		return serviceChargeable;
	}

	public void setServiceChargeable(Integer serviceChargeable) {
		this.serviceChargeable = serviceChargeable;
	}

	public Integer getServiceRenderingDeptId() {
		return serviceRenderingDeptId;
	}

	public void setServiceRenderingDeptId(Integer serviceRenderingDeptId) {
		this.serviceRenderingDeptId = serviceRenderingDeptId;
	}

	public Long getOrdRenderDatetime() {
		return ordRenderDatetime;
	}

	public void setOrdRenderDatetime(Long ordRenderDatetime) {
		this.ordRenderDatetime = ordRenderDatetime;
	}

	public Integer getServiceIsBilled() {
		return serviceIsBilled;
	}

	public void setServiceIsBilled(Integer serviceIsBilled) {
		this.serviceIsBilled = serviceIsBilled;
	}

	public Integer getServiceBillId() {
		return serviceBillId;
	}

	public void setServiceBillId(Integer serviceBillId) {
		this.serviceBillId = serviceBillId;
	}

	public String getOrdRemarks() {
		return ordRemarks;
	}

	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
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

	public Character getOrdCancelled() {
		return ordCancelled;
	}

	public void setOrdCancelled(Character ordCancelled) {
		this.ordCancelled = ordCancelled;
	}

	public Integer getOrdCancelReasonId() {
		return ordCancelReasonId;
	}

	public void setOrdCancelReasonId(Integer ordCancelReasonId) {
		this.ordCancelReasonId = ordCancelReasonId;
	}

	public Long getOrdCancelDatetime() {
		return ordCancelDatetime;
	}

	public void setOrdCancelDatetime(Long ordCancelDatetime) {
		this.ordCancelDatetime = ordCancelDatetime;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Character getIsDrug() {
		return isDrug;
	}

	public void setIsDrug(Character isDrug) {
		this.isDrug = isDrug;
	}

	public char getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(char isPackageService) {
		this.isPackageService = isPackageService;
	}

	public Character getIsInvestigationOrder() {
		return isInvestigationOrder;
	}

	public void setIsInvestigationOrder(Character isInvestigationOrder) {
		this.isInvestigationOrder = isInvestigationOrder;
	}

	public boolean isPackage() {
		return isPackage;
	}

	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public char getIsCapService() {
		return isCapService;
	}

	public void setIsCapService(char isCapService) {
		this.isCapService = isCapService;
	}

	public char getIsProcedure() {
		return isProcedure;
	}

	public void setIsProcedure(char isProcedure) {
		this.isProcedure = isProcedure;
	}

	public char getIsProcedureService() {
		return isProcedureService;
	}

	public void setIsProcedureService(char isProcedureService) {
		this.isProcedureService = isProcedureService;
	}

	public Integer getProcedureOrderMstId() {
		return procedureOrderMstId;
	}

	public void setProcedureOrderMstId(Integer procedureOrderMstId) {
		this.procedureOrderMstId = procedureOrderMstId;
	}

	public Character getIsAppointableRadiology() {
		return isAppointableRadiology;
	}

	public void setIsAppointableRadiology(Character isAppointableRadiology) {
		this.isAppointableRadiology = isAppointableRadiology;
	}

	public Integer getPackageEitherOrGroupId() {
		return packageEitherOrGroupId;
	}

	public void setPackageEitherOrGroupId(Integer packageEitherOrGroupId) {
		this.packageEitherOrGroupId = packageEitherOrGroupId;
	}

	public Integer getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Integer saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public boolean isDebitNoteService() {
		return debitNoteService;
	}

	public void setDebitNoteService(boolean debitNoteService) {
		this.debitNoteService = debitNoteService;
	}

	public BigDecimal getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public void setPatientClassPercentage(BigDecimal patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage;
	}

	public Boolean getMarkupStatus() {
		return markupStatus;
	}

	public void setMarkupStatus(Boolean markupStatus) {
		this.markupStatus = markupStatus;
	}
	  
}
