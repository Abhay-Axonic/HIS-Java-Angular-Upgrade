package com.param.billing.global.transaction.model;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;
import com.param.global.model.DoctorMaster;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.model.ServiceMaster;
import com.param.global.model.SpecialityMaster;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_DATA_FROM_BILLING_DTLS",
					 query 	="SELECT"
							 + "   bilmst.billing_master_id AS \"billId\","
							 + "   bilmst.bill_number AS \"billNumber\","
							 + "   EXTRACT("
							 + "     EPOCH"
							 + "     FROM"
							 + "       bilmst.bill_date_time"
							 + "   ) * 1000 AS \"billDateTime\","
							 + "   bilmst.net_amount AS \"netAmount\","
							 + "   bilmst.discount_amount AS \"discount\","
							 + "   bilmst.organisation_id AS \"orgId\","
							 + "   bilmst.concession_amount AS \"concession\","
							 + "   bilmst.gst AS \"taxAmt\","
							 + "   bilmst.admission_id AS \"admissionId\","
							 + "   bilmst.total_bill_amount AS \"totalAmount\","
							 + "   bilmst.unit_id AS \"orgUnitId\","
							 + "   trefd.referral_id AS \"referralDocId\""
							 + " FROM"
							 + "   billing.m_billing_master bilmst"
							 + "   INNER JOIN opd.t_encounter_master en on en.encounter_id = bilmst.encounter_id"
							 + "   INNER JOIN public.t_referal_details trefd on trefd.encounter_id = en.encounter_id"
						/*	 + " WHERE bilmst.bill_date_time <=':nextDate' and bilmst.bill_date_time >=':todayDate' "*/
							/* + " WHERE "
							 + "   bilmst.organisation_id =:orgId "
							 + "  AND bilmst.unit_id=:orgUnitId "*/),
	
	@NamedNativeQuery(name = "GET_DATA_FROM_BILLING_DTLS_INSERT_TO_T_DOCTOR_SHARE",
	 query 	="SELECT "
			 + "   bdtls.billing_details_id AS \"billDtlId\", "
			 + "   bdtls.order_details_id AS \"ordDtlId\", "
			 + "   bdtls.rate AS \"totalAmount\", "
			 + "   bdtls.rate AS \"unitRate\", "
			 + "   bdtls.quantity AS \"quantity\", "
			 /*+ "   bdtls.gross_amount AS \"grossAmount\", "*/
			 + "   bdtls.concession AS \"concession\", "
			 + "   bdtls.service_id AS \"serviceId\", "
			 + "   bdtls.unit_id AS \"orgUnitId\", "
			 + "   bdtls.organization_id AS \"orgId\", "
			 + "   bdtls.discount AS \"discount\", "
			 + "   bdtls.tax_id AS \"taxId\", "
			/* + "   bdtls.tax_per AS \"taxPer\", "*/
			 + "   bdtls.net_amt AS \"netAmount\", "
			 + "   bm.encounter_id AS \"visitId\", "
			 + "   bm.admission_id AS \"admissionId\", "
			 + "    bdtls.tax_amount AS \"taxAmt\", "
			 + "   bm.visit_type_id AS \"visitTypeId\", "
			 + "   bdtls.ord_doc_id AS \"docId\", "
			 + "   bdtls.ord_doc_spl_id AS \"docSpecId\", "
			 + "   cast(od.doc_share_per as double precision) AS \"sharePer\" "
			 + " FROM "
			 + "   billing.t_billing_details bdtls "
			 + "   INNER JOIN billing.m_billing_mASter bm on bm.billing_mASter_id = bdtls.billing_mASter_id "
			 +"    INNER JOIN public.t_order_details od on od.order_details_id=bdtls.order_details_id"
			/* + " WHERE "
			 + "   bm.bill_date_time <= '2018-07-31'  "
			 + "   AND bm.bill_date_time >= '2018-07-24'  "*/),
			 
	@NamedNativeQuery(name= "GET_ORDER_DETAILS_BY_BILL_ID",
						query="select billdt.billing_details_id as \"billingDetailsId\", "
								+ "billdt.order_details_id \"orderDetailsId\","
								+ " billdt.service_id as \"serviceId\" ,"
								+ " CAST (ord.credit_payable AS DOUBLE PRECISION) as \"creditPayable\" "
								+ " from billing.t_billing_details billdt "
								+ " inner join public.t_order_details ord "
								+ " on ord.order_details_id = billdt.order_details_id "
								+ " where billing_master_id = :billingMasterId "),
	
	
	@NamedNativeQuery(name= "GET_ORDER_DETAILS_BY_BILL_ID_FOR_COMAPNY_INVOICE",
	query=	" select "
			+ " 	billdt.billing_details_id as \"billingDetailsId\", "
			+ " 	billdt.order_details_id \"orderDetailsId\", "
			+ " 	billdt.service_id as \"serviceId\" , "
			+ " 	cast(mpr.payee_amount as double precision) as \"creditPayable\" "
			+ " from "
			+ " 	billing.t_bill_details_payee_mapper mpr "
			+ " inner join billing.t_billing_details billdt on "
			+ " 	mpr.bill_details_id = billdt.billing_details_id "
			+ " where "
			+ " 	billdt.billing_master_id = :billingMasterId "
			+ " 	and mpr.payee_id =:payeeId "
			),
								
	//get transaction logs from view v_transaction_log
	@NamedNativeQuery(name="GET_TRANSACTION_LOG_BY_RANGE",
						query="select vlog.transtype as \"transtype\" , vlog.transno as \"transno\" , "
								+ "to_char(vlog.created_date ,'DD-MM-YYYY HH24:MI:SS') as \"txnCreatedDate\" , vlog.createdUserId as \"createdUserId\" , "
								+ "vlog.createdUserName as \"createdUserName\" , to_char(vlog.updated_date , 'DD-MM-YYYY HH24:MI:SS') as \"txnUpdatedDate\"  , "
								+ "vlog.updatedUserId as \"updatedUserId\" , vlog.updatedUserName as \"updatedUserName\" "
								+ " from public.v_transation_log vlog "
								+ "where "
								+ "(vlog.crdate between :fromDate and :toDate  "
								+ "or "
								+ "vlog.udate between :fromDate and :toDate) "
								+ "and (vlog.createdUserId = :userId  or vlog.updatedUserId = :userId) "
								+ " order by vlog.created_date desc "),
	
	@NamedNativeQuery(name="GET_BILLING_DETAILS_BY_CREDIT_NOTE_ID",query="select "
			+ "billing_details_id as \"billingDetailsId\","
			+ "billing_master_id as \"billingMasterId\", "
			+ "service_id as \"serviceId\", "
			+ "rate as \"rate\", "
			+ "CAST(quantity AS DOUBLE PRECISION) as \"quantity\", "
			+ "concession as \"concession\", "
			+ "gross_amount as \"grossAmount\", "
			+ "co_pay_percentage as \"coPayPercentage\", "
			+ "unit_id as \"unitId\", "
			+ "organization_id as \"organizationId\", "
			+ "discount as \"discount\", "
			+ "tax_id as \"taxId\", "
			+ "tax_per as \"taxPer\", "
			+ "tax_amount as \"taxAmount\", "
			+ "net_amt as \"netAmt\", "
			+ "ord_doc_id as \"ordDocId\", "
			+ "ord_doc_spl_id as \"ordDocSplId\", "
			+ "drug_id as \"drugId\", "
			+ "batch_id as \"batchId\", "
			+ "order_date as \"orderDate\", "
			+ "order_sch_date as \"orderSchDate\", "
			+ "self_payable as \"selfPayable\", "
			+ "credit_payable as \"creditPayable\", "
			+ "payee_id as \"payeeId\", "
			+ "order_details_id as \"orderDetailsId\", "
			+ "package_id as \"packageId\", "
			+ "package_consumption_amt as \"packageConsumptionAmt\", "
			+ "co_pay_per as \"coPayPer\", "
			+ "is_doctor_share_posted as \"isDoctorSharePosted\" "
			+ "FROM billing.t_billing_details "
			+ "WHERE billing_details_id in (select billing_details_id from billing.t_credit_note_detail where credit_note_id=:creditNoteId) "),
	@NamedNativeQuery(name = "GET_DATA_FROM_BILLING_DTLS_FROM_GIVEN_DURATION",
	 query 	="SELECT"
			 +"    bilmst.billing_master_id AS \"billId\","
			 +"    bilmst.bill_number AS \"billNumber\","
			 +"    EXTRACT("
			 +"      EPOCH"
			 +"      FROM"
			 +"        bilmst.bill_date_time"
			 +"    ) * 1000 AS \"billDateTime\","
			 +"    bilmst.net_amount AS \"netAmount\","
			 +"    bilmst.discount_amount AS \"discount\","
			 +"    bilmst.organisation_id AS \"orgId\","
			 +"    bilmst.concession_amount AS \"concession\","
			 +"    bilmst.gst AS \"taxAmt\","
			 +"    bilmst.admission_id AS \"admissionId\","
			 +"    bilmst.total_bill_amount AS \"totalAmount\","
			 +"    bilmst.unit_id AS \"orgUnitId\","
			 +"     bilmst.bill_status_id as \"billStatusId\","
			 +"    bilmst.outstanding as \"outstanding\","
			 +"  trefd.referral_id AS \"referralDocId\","
			 +"   trefd.referal_details_id as \"referalDetailsId\","
			 +"  trefd.visit_type_id as \"visitTypeId\","
			 +"  trefd.referral_type_id as \"referralTypeId\","
			 +"  trefd.share as \"share\""
			 +"    FROM"
			 +"    billing.m_billing_master bilmst"
			 +"  left JOIN opd.t_encounter_master en on en.encounter_id = bilmst.encounter_id"
			 +"  left join adt.t_admission ad on ad.admission_id=bilmst.admission_id"
			 +"  left JOIN public.t_referal_details trefd on trefd.encounter_id = en.encounter_id "
			 +"  left join  public.t_referal_details trefdAd on trefdAd.admission_id=ad.admission_id"
//			 +"   where  bilmst.admission_id in (2706,2773)"
		/*	 + " WHERE bilmst.bill_date_time <=':nextDate' and bilmst.bill_date_time >=':todayDate' "*/
			/* + " WHERE "
			 + "   bilmst.organisation_id =:orgId "
			 + "  AND bilmst.unit_id=:orgUnitId "*/),
})
@Entity
@Table(name = "t_billing_details", schema = "billing")
@SequenceGenerator(name = "billing_details_seq", sequenceName = "billing.billing_details_seq",
    allocationSize = 1)
public class BillingDetails {
  @Id
  @Column(name = "billing_details_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_details_seq")
  private Integer billingDetailsId;

  @Column(name = "billing_master_id")
  private Integer billingMasterId;

  @Column(name = "service_id")
  private Integer serviceId;

  @Column(name = "rate")
  private Double rate;

  @Column(name = "quantity")
  private Double quantity;

  @Column(name = "concession")
  private Double concession;

  @Column(name = "gross_amount")
  private Double grossAmount;

  @Column(name = "co_pay_percentage")
  private Double coPayPercentage;

  @Column(name = "unit_id")
  private Integer unitId;

  @Column(name = "organization_id")
  private Integer organizationId;

  @Column(name = "status")
  private Character status;
  
  @Column(name = "is_doctor_share_posted")
  private Character isDoctorSharePosted;
  
  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "updated_by")
  private Integer updatedBy;

  @Column(name = "created_date")
  @Convert(converter = DateConverter.class)
  private Long createdDate;

  @Column(name = "updated_date")
  @Convert(converter = DateConverter.class)
  private Long updatedDate;

  @Column(name = "discount")
  private Double discount;

  @Column(name = "tax_id")
  private Integer taxId;

  @Column(name = "tax_per")
  private Double taxPer;

  @Column(name = "tax_amount")
  private Double taxAmount;

  @Column(name = "net_amt")
  private Double netAmt;

  @Column(name = "ord_doc_spl_id")
  private Integer ordDocSplId;

  @Column(name = "ord_doc_id")
  private Integer ordDocId;

  @Column(name = "drug_id")
  private Integer drugId;

  @Column(name = "batch_id")
  private Integer batchId;

  @Column(name = "order_date")
  @Convert(converter = DateConverter.class)
  private Long orderDate;

  @Column(name = "order_sch_date")
  @Convert(converter = DateConverter.class)
  private Long orderSchDate;

  @Column(name = "self_payable")
  private Double selfPayable;

  @Column(name = "credit_payable")
  private Double creditPayable;

  @Column(name = "payee_id")
  private Integer payeeId;

  @Column(name = "co_pay_per")
  private Double coPayPer;

  @Column(name = "order_details_id")
  private Integer orderDetailsId;

  @Column(name = "package_id")
  private Integer packageId;

  @Column(name = "package_consumption_amt")
  private Double packageConsumptionAmt;
  
  @Column(name = "old_bill_detail_id")
  private Integer oldBillDetailId;
  
  @Column(name = "cr_note_id")
  private Integer crNoteId;
  
  @Column(name="is_cancelled")
  private Character isCancelled;
  
  @Column(name = "disallowed_amount")
  private Double disallowedAmount;
	
  @Column(name="disallowed_against_invoice_service_dtl_id")
  private Integer disallowedAgainstInvoiceServiceDtlsId;
  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_master_id", insertable = false, nullable = false, updatable = false)
	private BillingMaster billingMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_details_id", insertable = false, nullable = false, updatable = false)
	private OrderDetailsMaster orderDetailsMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ord_doc_id", insertable = false, nullable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ord_doc_spl_id", insertable = false, nullable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billingDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CreditNoteDetails> creditNoteDetailsList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billingDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillDetailsPayeeMapper> billDetailsPayeeMappersList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billingDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillDetailsPackageMapper> billDetailsPackageMappersList;
	
	public BillingMaster getBillingMaster() {
		return billingMaster;
	}

	public void setBillingMaster(BillingMaster billingMaster) {
		this.billingMaster = billingMaster;
	}

	public OrderDetailsMaster getOrderDetailsMaster() {
		return orderDetailsMaster;
	}

	public void setOrderDetailsMaster(OrderDetailsMaster orderDetailsMaster) {
		this.orderDetailsMaster = orderDetailsMaster;
	}

	public List<BillDetailsPayeeMapper> getBillDetailsPayeeMappersList() {
		return billDetailsPayeeMappersList;
	}

	public void setBillDetailsPayeeMappersList(List<BillDetailsPayeeMapper> billDetailsPayeeMappersList) {
		this.billDetailsPayeeMappersList = billDetailsPayeeMappersList;
	}

	public List<BillDetailsPackageMapper> getBillDetailsPackageMappersList() {
		return billDetailsPackageMappersList;
	}

	public void setBillDetailsPackageMappersList(List<BillDetailsPackageMapper> billDetailsPackageMappersList) {
		this.billDetailsPackageMappersList = billDetailsPackageMappersList;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public Integer getDisallowedAgainstInvoiceServiceDtlsId() {
		return disallowedAgainstInvoiceServiceDtlsId;
	}

	public void setDisallowedAgainstInvoiceServiceDtlsId(Integer disallowedAgainstInvoiceServiceDtlsId) {
		this.disallowedAgainstInvoiceServiceDtlsId = disallowedAgainstInvoiceServiceDtlsId;
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

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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

	public Double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
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
		this.status = status;
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
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

	public Double getCoPayPer() {
		return coPayPer;
	}

	public void setCoPayPer(Double coPayPer) {
		this.coPayPer = coPayPer;
	}

	public Character getIsDoctorSharePosted() {
		return isDoctorSharePosted;
	}

	public void setIsDoctorSharePosted(Character isDoctorSharePosted) {
		this.isDoctorSharePosted = (isDoctorSharePosted == null || isDoctorSharePosted == '\u0000') ? 'N': isDoctorSharePosted;
	}

}
