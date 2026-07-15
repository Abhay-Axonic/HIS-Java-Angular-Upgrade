package com.param.billing.global.transaction.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_PAYEE_DETAILS_BY_BILL_ID",
					 query = "select "
							 + "	billpy.bill_payee_details_id as \"billPayeeDetailsId\", "
							 + "	billpy.payee_id as \"payeeId\", "
							 + "	coalesce(comp.company_code, '-') as \"companyCode\", "
							 + "	comp.company_desc as \"companyDesc\", "
							 + "	billpy.associate_company_id as \"associateCompanyId\", "
							 + "	billpy.is_invoice_generated as \"isInvoiceGenerated\", "
							 + "	insurance.insurance_desc as \"associateCompanyDesc\", "
							 + "	billpy.bill_id as \"billId\", "
							 + "	billpy.bill_amount as \"billAmount\", "
							 + "	billpy.due_amount as \"dueAmount\", "
							 + "	billpy.disallowed_amount as \"disallowedAmount\", "
							 + "	billpy.payment_entitlement_id as \"paymentEntitlementId\", "
							 + "	billpy.other_deduction as \"otherDeduction\", "
							 + "	coalesce((billpy.paid_amount), 0) as \"paidAmount\", "
							 + "	coalesce((billpy.cr_net_amt), 0) as \"crNetAmt\", "
							 + "	coalesce((select sum(coalesce(crnotemst.cr_deu_amount, 0)) from billing.m_credit_note_master crnotemst where crnotemst.bill_payee_details_id = billpy.bill_payee_details_id  and crnotemst.status ='A'), 0) as \"crDeuAmount\", "
							 + "	coalesce(( select sum(amount_refunded) from billing.t_payment_refund_master where credit_note_id in ( select credit_note_id from billing.m_credit_note_master mcnm where mcnm.bill_payee_details_id = billpy.bill_payee_details_id and mcnm.status = 'A')), 0) as \"refundedAmount\" "
							 + "from "
							 + "	billing.t_bill_payee_details billpy "
							 + "inner join public.t_company_master comp on "
							 + "	comp.company_id = billpy.payee_id "
							 + "left join adt.t_insurance insurance on "
							 + "	insurance.insurance_id = billpy.associate_company_id "
							 + "where "
							 + "	billpy.bill_id = :billId "
							 + "	and billpy.status = 'A' "
							 + "order by "
							 + "	comp.company_desc ASC"),
	
	@NamedNativeQuery(name = "UPDATE_CR_AMOUNT_BY_BILL_AND_PAYEE",
					 query = "update billing.t_bill_payee_details "
					 		+" set cr_amount = coalesce(cr_amount,0) + :crAmount "
					 		+" where bill_id=:billId and payee_id=:payeeId "),

	@NamedNativeQuery(name = "UPDATE_DUE_AMOUNT_BY_BILL_AND_PAYEE",
					 query = "update billing.t_bill_payee_details "
					 		+" set due_amount =:dueAmount, paid_amount = (case when (:paidAmount + coalesce(paid_amount,0)) > bill_amount then bill_amount else (:paidAmount + coalesce(paid_amount,0)) end) "
					 		+" where bill_id=:billId and payee_id=:payeeId "),
	
	 @NamedNativeQuery(name = "GET_BILL_DETAILS_BY_PAYEE_ID", query = "select "
			 + "	payee.bill_payee_details_id as \"billPayeeDetailsId\", "
			 + "	payee.bill_id as \"billId\", "
			 + "	false as \"isPharamcy\", "
			 + "	payee.due_amount as \"dueAmount\", "
			 + "	om.order_no as \"orderNo\", "
			 + "	concat(doc.first_name, ' ', doc.last_name) as \"doctorName\", "
			 + "	bd.quantity as \"quantity\", "
			 + "	od.service_amt as \"unitRate\", "
			 + "	bd.tax_amount as \"taxAmount\", "
			 + "	bd.net_amt as \"netAmount\", "
			 + "	bd.concession as \"concession\", "
			 + "	bd.gross_amount as \"totalAmount\", "
			 + "	bd.discount as \"discount\", "
			 + "	bd.tax_per as \"taxPer\", "
			 + "	service.service_standard_name as \"serviceStandardName\", "
			 + "	mpr.payee_amount as \"credtPayable\", "
			 + "	bd.self_payable as \"selfPayable\", "
			 + "	coalesce(mpr.cr_net_amt,0) as \"crNetAmt\" "
			 + "from "
			 + "	billing.t_bill_details_payee_mapper mpr "
			 + "inner join billing.t_billing_details bd on "
			 + "	mpr.bill_details_id = bd.billing_details_id "
			 + "inner join billing.m_billing_master bm on "
			 + "	bm.billing_master_id = bd.billing_master_id "
			 + "inner join public.t_order_details od on "
			 + "	od.order_details_id = bd.order_details_id "
			 + "inner join public.t_order_details_package_mapper odpm on "
			 + "	od.order_details_id = odpm.order_details_id and odpm.status  = 'A' and odpm.is_package_service ='N' "
			 + "inner join public.t_order_master om on "
			 + "	om.order_id = od.order_id "
			 + "inner join service.m_service_master service on "
			 + "	service.service_master_id = bd.service_id "
			 + "left join doctor.m_doctor_master doc on "
			 + "	doc.doctor_id = bd.ord_doc_id "
			 + "inner join billing.t_bill_payee_details payee on "
			 + "	payee.bill_id = bm.billing_master_id "
			 + "where "
			 + "	bm.billing_master_id =:billId "
			 + "	and bm.bill_cancelled = 'N' "
			 + "	and payee.payee_id = :payeeId "
			 + "	and mpr.payee_id = :payeeId and od.status = 'A' "
			 + "union all "
			 + "select "
			 + "	payee.payee_details_id as \"billPayeeDetailsId\", "
			 + "	payee.t_sale_id as \"billId\", "
			 + "	true as \"isPharamcy\", "
			 + "	payee.due_amount as \"dueAmount\", "
			 + "	'-' as \"orderNo\", "
			 + "	concat(doc.first_name, ' ', doc.last_name) as \"doctorName\", "
			 + "	sd.quantity as \"quantity\", "
			 + "	cast(batch.rate as numeric(15, 2)) as \"unitRate\", "
			 + "	sd.tax_amount as \"taxAmount\", "
			 + "	sd.net_amount as \"netAmount\", "
			 + "	0 as \"concession\", "
			 + "	sd.total_amount as \"totalAmount\", "
			 + "	sd.discount_amount as \"discount\", "
			 + "	sd.tax_percentage as \"taxPer\", "
			 + "	item.\"name\" as \"serviceStandardName\", "
			 + "	sum(cast(sdpm.payee_amount as double precision)) as \"credtPayable\", "
			 + "	sd.self_payable as \"selfPayable\", "
			 + "	0 as \"crNetAmt\" "
			 + "from "
			 + "	pharmacy.t_sale_detail_payee_mapper sdpm "
			 + " inner join pharmacy.t_sale_detail sd on "
			 + "	sdpm.t_sale_details_id = sd.id "
//			 + " inner join public.t_sale_detail_package_mapper sdpkgm  on "
//			 + "	sdpkgm.t_sale_details_id = sd.id and sdpkgm.status  = 'A' and sdpkgm.is_package_item ='N'"
			 + " inner join pharmacy.t_sale sm on "
			 + "	sd.sale_id = sm.id "
			 + " inner join inventory.t_batch batch on "
			 + "	sd.batch_id = batch.id "
			 + " inner join public.m_item item on "
			 + "	batch.item_id = item.id "
			 + "	and batch.is_active = true "
			 + "	and batch.is_deleted = false "
			 + "inner join pharmacy.t_pharmacy_bill_payee_details payee on "
			 + "	payee.t_sale_id = sm.id "
			 + "left join doctor.m_doctor_master doc on "
			 + "	doc.doctor_id = sm.doctor_id "
			 + "where "
			 + "	sm.id =:billId "
			 + "	and sm.is_active = true "
			 + "	and sm.is_deleted = false "
			 + "	and payee.payee_id =:payeeId "
			 + "	and sdpm.payee_id =:payeeId and sd.is_active = true "
			 + "    and coalesce(sd.package_consumption_amt)<sd.net_amount  "
			 + "group by "
			 + "	payee.payee_details_id , "
			 + "	payee.t_sale_id , "
			 + "	\"isPharamcy\", "
			 + "	payee.due_amount, "
			 + "	\"orderNo\", "
			 + "	\"doctorName\", "
			 + "	sd.quantity, "
			 + "	batch.rate, "
			 + "	sd.tax_amount , "
			 + "	sd.net_amount , "
			 + "	sd.total_amount, "
			 + "	sd.discount_amount, "
			 + "	sd.tax_percentage , "
			 + "	item.\"name\" , "
			 + "	sd.self_payable" ),
		
	@NamedNativeQuery(name = "UPDATE_BILL_PAYEE_DETAILS_FOR_COMPANY_INVOICE",
					query = "update billing.t_bill_payee_details "
					 		+ " set due_amount = (due_amount - :amount), "
					 		+ " disallowed_amount = coalesce(disallowed_amount,0) + :disallowedAmount, "
					 		+ " other_deduction= coalesce(other_deduction,0) + :otherDeduction "
					 		+ " where bill_id=:billId "
					 		+ " and payee_id=:payeeId "
					 		+ " and status='A' "),
	
	@NamedNativeQuery(name = "UPDATE_PHARAMCY_BILL_PAYEE_DETAILS_FOR_COMPANY_INVOICE",
	query = "update " + 
			"	pharmacy.t_pharmacy_bill_payee_details " + 
			" set" + 
			"	due_amount = (due_amount - :amount) " + 
			" where " + 
			"	t_sale_id =:saleId " + 
			"	and payee_id =:payeeId")
	
	
})
@Entity
@Table(name="t_bill_payee_details",schema="billing")
@SequenceGenerator(name = "bill_payee_details_seq", sequenceName = "billing.bill_payee_details_seq", allocationSize = 1)
public class BillPayeeDetails {

	@Id
	@Column(name="bill_payee_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_payee_details_seq")
	private int billPayeeDetailsId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="bill_id")
	private Integer billId;
	
	@Column(name="bill_amount")
	private Double billAmount;
	
	@Column(name="due_amount")
	private Double dueAmount;
	
	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name = "other_deduction")
	private Double otherDeduction;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;
	
	@Column(name="updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name = "cr_amount")
	private Double crAmount;
	
	@Column(name = "settlement_date")
	@Convert(converter=DateConverter.class)
	private Long settlementDate;
	
	@Column(name="associate_company_id")
	private Integer associateCompanyId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bill_id", insertable = false, nullable = false, updatable = false)
	private BillingMaster billingMaster;
	
	@Column(name="priority_id")
	private Integer priorityId;
	
	@Column(name="payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name="cr_net_amt")
	private Double crNetAmt;
	
	@Column(name="paid_amount")
	private Double paidAmount;
	
	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	public BillingMaster getBillingMaster() {
		return billingMaster;
	}

	public void setBillingMaster(BillingMaster billingMaster) {
		this.billingMaster = billingMaster;
	}

	public Double getCrNetAmt() {
		return crNetAmt;
	}

	public void setCrNetAmt(Double crNetAmt) {
		this.crNetAmt = crNetAmt;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Double getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public Double getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}

	public Long getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Long settlementDate) {
		this.settlementDate = settlementDate;
	}

	public int getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(int billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
}
