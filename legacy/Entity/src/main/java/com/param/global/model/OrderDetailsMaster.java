package com.param.global.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.billing.packages.model.TPackageConsumptionMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.model.master.Tax;
import com.param.global.common.DateConverter;
import com.param.service.global.model.MPackageMaster;
import com.param.service.global.model.OrderDetailsPackageMapper;

@NamedQueries({

		@NamedQuery(name = "LAB_UPDATE_ORDER_DETAILS_SERVICE", query = "UPDATE"
				+ " OrderDetailsMaster  orderDetailsMaster " + " SET "
				+ " orderDetailsMaster.serviceRendered = :serviceRendered ,orderDetailsMaster.ordRenderDatetime = :date  "
				+ " WHERE " + " orderDetailsMaster.orderDetailsId = :orderDetailsId "),

		@NamedQuery(name = "UPDATE_IS_BILLED_STATUS", query = " UPDATE OrderDetailsMaster SET serviceIsBilled=1, "
				+ "serviceBillId=:serviceBillId " + " WHERE orderDetailsId IN(:listOrderDetailsId)"),

		@NamedQuery(name = "GET_ORDER_DETAILS_BY_ID", query = "SELECT	ord.orderDetailsId as orderDetailsId, "
				+ "ord.orgId as orgId, " 
				+ "ord.orgUnitId as orgUnitId, " 
				+ "ord.orderId as orderId, "
				+ "ord.orderQty as orderQty, " 
				+ "ord.serviceId as serviceId, " 
				+ "ord.priorityId as priorityId, "
				+ "ord.isOutsourced as isOutsourced, " 
				+ "ord.orderSchDate as orderSchDate, "
				+ "ord.serviceAmt as serviceAmt, " 
				+ "ord.ordConcession as ordConcession, "
				+ "ord.ordDiscount as ordDiscount, " 
				+ "ord.netAmount as netAmount, "
				+ "ord.ordDocSplId as ordDocSplId, " 
				+ "ord.taxId as taxId, " 
				+ "ord.taxPer as taxPer, "
				+ "ord.taxAmount as taxAmount, " 
				+ "ord.selfPayable as selfPayable, "
				+ "ord.creditPayable as creditPayable, " 
				+ "ord.payeeId as payeeId, "
				+ "ord.packageConsumptionAmt as packageConsumptionAmt, " 
				+ "ord.isPostedWhileDischarge as isPostedWhileDischarge, " 
				+ "ord.orderSetId as orderSetId, "
				+ "ord.contractId as contractId, " 
				+ "ord.bedCategoryId as bedCategoryId, "
				+ "ord.coPayPer as coPayPer, " 
				+ "ord.docSharePer as docSharePer, "
				+ "ord.refDocSharePer as refDocSharePer, " 
				+ "ord.ordTotalAmt as ordTotalAmt, "
				+ "ord.ordCancelBy as ordCancelBy, " 
				+ "ord.ordCancelRemark as ordCancelRemark, "
				+ "ord.tariffId as tariffId, "
				/*
				 * + "ord.billingClassId as billingClassId, " +
				 * "ord.bedId as bedId, " + "ord.roomId as roomId, " +
				 * "ord.wardId as wardId, "
				 */
				+ "ord.orderEmergencyFlag as orderEmergencyFlag, " + "ord.packageId as packageId, "
				+ "ord.ordDocId as ordDocId," + "ord.drugId as drugId, " + "ord.batchId as batchId, "
				+ "ord.oldOrdDtlId as oldOrdDtlId, " + "ord.serviceRendered as serviceRendered, "
				+ "ord.serviceChargeable as serviceChargeable, "
				+ "ord.serviceRenderingDeptId as serviceRenderingDeptId, "
				+ "ord.ordRenderDatetime as ordRenderDatetime, " + "ord.serviceIsBilled as serviceIsBilled, "
				+ "ord.serviceBillId as serviceBillId, " + "ord.ordRemarks as ordRemarks, "
				+ "ord.createdDate as createdDate, " + "ord.createdBy as createdBy, "
				+ "ord.ordCancelled as ordCancelled, " + "ord.ordCancelReasonId as ordCancelReasonId, "
				+ "ord.ordCancelDatetime as ordCancelDatetime, " + "ord.status as status, "
				+ "ord.updatedBy as updatedBy, " + "ord.updatedDate as updatedDate," + "ord.isDrug as isDrug, "
				+ "ord.isPackageService as isPackageService, " + "ord.isPackage as isPackage,"
				+ "ord.isCapService as isCapService " + "FROM	OrderDetailsMaster ord "
				+ "WHERE	ord.orderDetailsId =:orderDetailsId "),

		@NamedQuery(name = "GET_ORDER_DTLS_LIST_BY_ORDER_DTLS_ID", query = " FROM OrderDetailsMaster orderDetailsMaster"
				+ " WHERE orderDetailsMaster.orderDetailsId = :orderDetailsId") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_ORDERED_PACKAGE_SERVICES_FOR_CANCELLATION_BY_PACKAGE_ENCOUNTER_ID", query = "select "
				+ "	ordrdtls.order_details_id as \"orderDetailsId\", " + "	ordrdtls.order_id as \"orderId\", "
				+ "	ordrdtls.order_qty as \"orderQty\", " + "	ordrdtls.service_id as \"serviceId\", "
				+ "	ordrdtls.service_amt as \"serviceAmt\", " + "	ordrdtls.net_amount as \"netAmount\", "
				+ "	ordrdtls.package_id as \"packageId\", " + "	ordrdtls.ord_cancelled as \"ordCancelled\", "
				+ "	ordrdtls.is_package_service as \"isPackageService\", " + "	ordrdtls.is_package as \"isPackage\", "
				+ "	ordrdtls.is_cap_service as \"isCapService\", "
				+ "	ordrdtls.service_rendered as \"serviceRendered\", " + "	pm.package_type_id as \"packageTypeId\" "
				+ "from " + "	t_order_details ordrdtls " + "inner join public.t_order_master ordrmst on "
				+ "	ordrmst.order_id = ordrdtls.order_id " + "inner join service.m_package_master pm on "
				+ "	ordrdtls.package_id = pm.package_master_id " + "where " + "	package_id = :packageId "
				+ "	and is_package_service = 'Y' " + "	and ordrdtls.ord_cancelled = 'N' "
				+ "	and ordrdtls.is_package = false " + "	and (ordrmst.encounter_id = :encounterId "
				+ "	and ordrmst.visit_type_id =:visitTypeId) " + "	or (ordrmst.admission_id = :encounterId "
				+ "	and ordrmst.visit_type_id =:visitTypeId)"),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_SERVICE_AS_CANCELLED", query = "update public.t_order_details "
				+ "set ord_cancelled = 'Y', " + "status='I' " + "where order_details_id=:orderDetailsId "),

		@NamedNativeQuery(name = "UPDATE_PACKAGE_DETAILS_SERVICE", query = "update public.t_order_details "
				+ "set ord_cancelled = 'Y' " + "where order_details_id=:orderDetailsId ") })
@Entity
@Table(name = "t_order_details", schema = "public")
@SequenceGenerator(name = "m_seq_order_dtls", sequenceName = "public.m_seq_order_dtls", allocationSize = 1)
public class OrderDetailsMaster {
	@Id
	@Column(name = "order_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_order_dtls")
	private int orderDetailsId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "order_id")
	private Integer orderId;

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

	@Column(name = "ord_doc_spl_id")
	private Integer ordDocSplId;

	@Column(name = "tax_id")
	private Integer taxId;

	@Column(name = "tax_per")
	private BigDecimal taxPer;

	@Column(name = "tax_amount")
	private BigDecimal taxAmount;

	@Column(name = "self_payable")
	private BigDecimal selfPayable;

	@Column(name = "credit_payable")
	private BigDecimal creditPayable;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "package_consumption_amt")
	private BigDecimal packageConsumptionAmt;

	@Column(name = "order_set_id")
	private Integer orderSetId;

	@Column(name = "contract_id")
	private Integer contractId;

	@Column(name = "bed_category_id")
	private Integer bedCategoryId;

	@Column(name = "co_pay_per")
	private BigDecimal coPayPer;

	@Column(name = "doc_share_per")
	private BigDecimal docSharePer;

	@Column(name = "ref_doc_share_per")
	private BigDecimal refDocSharePer;

	@Column(name = "ord_total_amt")
	private BigDecimal ordTotalAmt;

	@Column(name = "ord_cancel_by")
	private Integer ordCancelBy;

	@Column(name = "ord_cancel_remark")
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

	@Column(name = "is_drug")
	private Character isDrug;

	@Column(name = "is_package_service")
	private char isPackageService;

	@Column(name = "is_investigation_order")
	private Character isInvestigationOrder;

	@Column(name = "is_package")
	private boolean isPackage;

	@Column(name = "is_cap_service")
	private char isCapService;

	@Column(name = "is_procedure")
	private char isProcedure;

	@Column(name = "is_procedure_service")
	private char isProcedureService;

	@Column(name = "procedure_order_mst_id")
	private Integer procedureOrderMstId;

	@Column(name = "is_appointable_radiology")
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

	@Column(name = "accession_number")
	private Boolean accessionNumber;

	@Column(name = "disallowed_amount")
	private Double disallowedAmount;

	@Column(name = "is_posted_while_discharge")
	private Character isPostedWhileDischarge;

	@Column(name = "is_from_addservice")
	private Character isFromAddservice;

	@Column(name = "is_service_reviewed")
	private boolean isServiceReviewed;

	@Column(name = "dummy_service_amt")
	private BigDecimal dummyServiceAmt;

	@Column(name = "is_consultation_service")
	private Character isConsultationService;

	@Column(name = "force_add_removed")
	private Integer forceAddRemoved;

	@Column(name = "is_consumed")
	private Boolean isConsumed;

	@Column(name = "is_order_visible_dept")
	private Character isOrderVisibleDepartment;

	@Column(name = "review_applicable_date")
	private Date reviewApplicableDate;
	
	@Column(name = "nursing_service_order_flag")
	private Character nursingServiceOrderFlag;
	
	@Column(name = "discount_category_id")
	private Integer discountCategoryId;
	
	@Column(name = "discount_ownership")
	private Integer discountOwnership;
	
	@Column(name = "hospital_share")
	private BigDecimal hospitalShare;
	
	@Column(name = "p_image")
	private String pImage;
	
	@Column(name = "p_image_name")
	private String pImageName;
	
	@Column(name = "patient_coupon_template_mapper_id")
	private Integer patientCouponTemplateMapperId;
	
	@Column(name = "is_charged_half_day")
	private Character isChargedHalfDay;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;

	@Column(name = "is_dummy_service")
	private Boolean isDummyService;
	
	@Column(name = "is_hidden_service")
	private Boolean isHiddenService;
	
	@Column(name="pharmacy_discount_override")
	private Integer pharmacyDiscountOverride;

	@Column(name="redo_count")
	private Integer redoCount;

	@Column(name="redo_reason")
	private String redoReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", insertable = false, nullable = false, updatable = false)
	private OrderMaster orderMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id", insertable = false, nullable = false, updatable = false)
	private Tax tax;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", insertable = false, updatable = false, nullable = false)
	private MPackageMaster mPackageMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ord_doc_spl_id", insertable = false, updatable = false, nullable = false)
	private SpecialityMaster specialityMaster;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderDetailsMaster")
	private List<LabSampleDetailsMaster> listLabSampleDetailsMaster;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderDetailsMaster")
	private List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderDetailsMaster")
	private List<BillingDetails> billingDetailsList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderDetailsMaster")
	private List<TemplateTransaction> templateTransactionList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ord_doc_id", insertable = false, updatable = false, nullable = false)
	private DoctorMaster doctorMaster;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderDetailsMaster")
	private List<OrderDetailsPackageMapper> listOrderDetailsPackageMapper;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderDetailsMaster")
	private List<TPackageConsumptionMaster> listTPackageConsumptionMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> userMasterList;
	

	public Boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(Boolean isDummyService) {
		this.isDummyService = isDummyService;
	}

	public Boolean getIsHiddenService() {
		return isHiddenService;
	}

	public void setIsHiddenService(Boolean isHiddenService) {
		this.isHiddenService = isHiddenService;
	}

	public Character getIsChargedHalfDay() {
		return isChargedHalfDay;
	}

	public void setIsChargedHalfDay(Character isChargedHalfDay) {
		this.isChargedHalfDay = isChargedHalfDay!=null ? isChargedHalfDay : 'N';
	}

	public Integer getDiscountCategoryId() {
		return discountCategoryId;
	}

	public void setDiscountCategoryId(Integer discountCategoryId) {
		this.discountCategoryId = discountCategoryId;
	}

	public List<BillingDetails> getBillingDetailsList() {
		return billingDetailsList;
	}

	public void setBillingDetailsList(List<BillingDetails> billingDetailsList) {
		this.billingDetailsList = billingDetailsList;
	}

	public Character getNursingServiceOrderFlag() {
		return nursingServiceOrderFlag==null?'N':nursingServiceOrderFlag;
	}

	public void setNursingServiceOrderFlag(Character nursingServiceOrderFlag) {
		this.nursingServiceOrderFlag = nursingServiceOrderFlag==null?'N':nursingServiceOrderFlag;
	}

	public Character getIsConsultationService() {
		return isConsultationService;
	}

	public Date getReviewApplicableDate() {
		return reviewApplicableDate;
	}

	public void setReviewApplicableDate(Date reviewApplicableDate) {
		this.reviewApplicableDate = reviewApplicableDate;
	}

	public void setIsConsultationService(Character isConsultationService) {
		this.isConsultationService = isConsultationService;
	}

	public BigDecimal getRefDocSharePer() {
		return refDocSharePer;
	}

	public void setRefDocSharePer(BigDecimal refDocSharePer) {
		this.refDocSharePer = refDocSharePer;
	}

	public List<OrderDetailsPackageMapper> getListOrderDetailsPackageMapper() {
		return listOrderDetailsPackageMapper;
	}

	public void setListOrderDetailsPackageMapper(List<OrderDetailsPackageMapper> listOrderDetailsPackageMapper) {
		this.listOrderDetailsPackageMapper = listOrderDetailsPackageMapper;
	}

	public BigDecimal getDummyServiceAmt() {
		return dummyServiceAmt;
	}

	public void setDummyServiceAmt(BigDecimal dummyServiceAmt) {
		this.dummyServiceAmt = dummyServiceAmt;
	}

	public Character getIsFromAddservice() {
		return isFromAddservice;
	}

	public void setIsFromAddservice(Character isFromAddservice) {
		this.isFromAddservice = isFromAddservice;
	}

	public Character getIsPostedWhileDischarge() {
		return isPostedWhileDischarge;
	}

	public void setIsPostedWhileDischarge(Character isPostedWhileDischarge) {
		this.isPostedWhileDischarge = isPostedWhileDischarge;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public Boolean getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(Boolean accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
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

	public List<TPeriodicityServiceOrders> getListTPeriodicityServiceOrders() {
		return listTPeriodicityServiceOrders;
	}

	public void setListTPeriodicityServiceOrders(List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders) {
		this.listTPeriodicityServiceOrders = listTPeriodicityServiceOrders;
	}

	public Character getIsAppointableRadiology() {
		return isAppointableRadiology;
	}

	public void setIsAppointableRadiology(Character isAppointableRadiology) {
		this.isAppointableRadiology = (isAppointableRadiology != '\u0000' ? isAppointableRadiology : 'N');
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

	public Character getIsDrug() {
		return isDrug;
	}

	public void setIsDrug(Character isDrug) {
		this.isDrug = isDrug;
	}

	public int getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(int orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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

	public OrderMaster getOrderMaster() {
		return orderMaster;
	}

	public void setOrderMaster(OrderMaster orderMaster) {
		this.orderMaster = orderMaster;
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

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<LabSampleDetailsMaster> getListLabSampleDetailsMaster() {
		return listLabSampleDetailsMaster;
	}

	public void setListLabSampleDetailsMaster(List<LabSampleDetailsMaster> listLabSampleDetailsMaster) {
		this.listLabSampleDetailsMaster = listLabSampleDetailsMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public char getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(char isPackageService) {
		this.isPackageService = (isPackageService != '\u0000' ? isPackageService : 'N');
	}

	public Character getIsInvestigationOrder() {
		return isInvestigationOrder;
	}

	public void setIsInvestigationOrder(Character isInvestigationOrder) {
		this.isInvestigationOrder = (isInvestigationOrder != '\u0000' ? isInvestigationOrder : 'N');
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
		this.isCapService = (isCapService != '\u0000' ? isCapService : 'N');
	}

	public char getIsProcedure() {
		return isProcedure;
	}

	public void setIsProcedure(char isProcedure) {
		this.isProcedure = (isProcedure != '\u0000' ? isProcedure : 'N');
	}

	public char getIsProcedureService() {
		return isProcedureService;
	}

	public void setIsProcedureService(char isProcedureService) {
		this.isProcedureService = (isProcedureService != '\u0000' ? isProcedureService : 'N');
	}

	public Integer getProcedureOrderMstId() {
		return procedureOrderMstId;
	}

	public void setProcedureOrderMstId(Integer procedureOrderMstId) {
		this.procedureOrderMstId = procedureOrderMstId;
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

	public boolean getDebitNoteService() {
		return debitNoteService;
	}

	public void setDebitNoteService(boolean debitNoteService) {
		this.debitNoteService = debitNoteService;
	}

	public boolean isServiceReviewed() {
		return isServiceReviewed;
	}

	public void setServiceReviewed(boolean isServiceReviewed) {
		this.isServiceReviewed = isServiceReviewed;
	}

	public List<TPackageConsumptionMaster> getListTPackageConsumptionMaster() {
		return listTPackageConsumptionMaster;
	}

	public void setListTPackageConsumptionMaster(List<TPackageConsumptionMaster> listTPackageConsumptionMaster) {
		this.listTPackageConsumptionMaster = listTPackageConsumptionMaster;
	}

	public Boolean getIsConsumed() {
		return isConsumed != null ? isConsumed : false;
	}

	public void setIsConsumed(Boolean isConsumed) {
		this.isConsumed = isConsumed != null ? isConsumed : false;
	}

	public Integer getForceAddRemoved() {

		return forceAddRemoved != null ? forceAddRemoved : 0;
	}

	public void setForceAddRemoved(Integer forceAddRemoved) {
		this.forceAddRemoved = forceAddRemoved != null ? forceAddRemoved : 0;
	}

	public Character getIsOrderVisibleDepartment() {
		return isOrderVisibleDepartment!=null?isOrderVisibleDepartment:'A';
	}

	public void setIsOrderVisibleDepartment(Character isOrderVisibleDepartment) {
		this.isOrderVisibleDepartment = isOrderVisibleDepartment!=null?isOrderVisibleDepartment:'A';
	}

	public Integer getDiscountOwnership() {
		return discountOwnership;
	}

	public void setDiscountOwnership(Integer discountOwnership) {
		this.discountOwnership = discountOwnership;
	}

	public BigDecimal getHospitalShare() {
		return hospitalShare;
	}

	public void setHospitalShare(BigDecimal hospitalShare) {
		this.hospitalShare = hospitalShare;
	}

	public String getpImage() {
		return pImage;
	}

	public void setpImage(String pImage) {
		this.pImage = pImage;
	}

	public String getpImageName() {
		return pImageName;
	}

	public void setpImageName(String pImageName) {
		this.pImageName = pImageName;
	}

	public Integer getPatientCouponTemplateMapperId() {
		return patientCouponTemplateMapperId;
	}

	public void setPatientCouponTemplateMapperId(Integer patientCouponTemplateMapperId) {
		this.patientCouponTemplateMapperId = patientCouponTemplateMapperId;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Integer getPharmacyDiscountOverride() {
		return pharmacyDiscountOverride;
	}

	public void setPharmacyDiscountOverride(Integer pharmacyDiscountOverride) {
		this.pharmacyDiscountOverride = pharmacyDiscountOverride;
	}

	public Integer getRedoCount() {
		return redoCount;
	}

	public void setRedoCount(Integer redoCount) {
		this.redoCount = redoCount;
	}

	public String getRedoReason() {
		return redoReason;
	}

	public void setRedoReason(String redoReason) {
		this.redoReason = redoReason;
	}
}
