package com.param.billing.global.transaction.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@NamedNativeQueries({

		@NamedNativeQuery(name = "GET_COMPANY_INVOICE_REMAINDER_BY_PAYEE_ID", query = "select "
				+ " distinct cim.payee_id  AS \"companyId\", " + " im.company_invoice_id AS \"invoiceId\", "
				+ " cirm.remark as \"remark\", " + " cirm. response as \"response\", "
				+ " cirm.company_invoice_reminder_id AS  \"companyInvoiceReminderId\", "
				+ " to_char(cirm.reminder_date,'dd/MM/yyyy')AS \"createdDate\", "
				+ " cim.payment_entitlement_id AS \"companyTypeId\", " + " im.invoice_number AS \"invoiceNumber\", "
				+ "  im.due_amount  as \"dueAmount\"," + "  im.due_amount AS \"creditBillAmount\", "
				+ " to_char( im.invoice_date,'dd/MM/yyyy')AS \"invoiceDate\" " + " FROM "
				+ "  billing.m_company_invoice_master im"
				+ "  inner join billing.m_company_invoice_master cim on cim.payee_id=im.payee_id "
				+ "  left outer join billing.m_company_invoice_reminder_master cirm on cirm.invoice_id= im.company_invoice_id "
				+ "  where im.organisation_id =:orgId "),

		@NamedNativeQuery(name = "GET_INVOICE_LIST_FOR_COMPANY_SETTLEMENT", query = "select distinct inv.company_invoice_id as \"companyInvoiceId\", "
				+ " 	inv.invoice_number as \"invoiceNumber\", "
				+ "		comp.company_desc as \"companyName\",  "
				+ " 	inv.total_amount as \"totalAmount\", "
				+ "		inv.due_amount as \"dueAmount\", "
				+ "		to_char(inv.invoice_date,'DD-MM-YYYY') as \"invoiceDate\", "
				+ " 	inv.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "		inv.payee_id as \"payeeId\", "
				+ "		coalesce(inv.cr_net_amount,0) as \"crNetAmount\" "
				+ " from billing.m_company_invoice_master inv "
				+ " inner join public.t_company_master comp  "
				+ " 	on comp.company_id = inv.payee_id"
				+ " inner join billing.t_company_invoice_details tcid on"  
				+ " 	inv.company_invoice_id = tcid.company_invoice_id " 
				+ " left join billing.m_billing_master mbm on " 
				+ " 	tcid.bill_id = mbm.billing_master_id " 
				+ " left join pharmacy.t_sale sale on "
				+ "		sale.id = tcid.t_sale_id "
				+ " where "
				+ "		inv.organisation_id = :orgId and inv.unit_id = :unitId  and inv.due_amount > 0 "), //

		@NamedNativeQuery(name = "UPDATE_COMPANY_INVOICE_MASTER_DUE_AMOUNT", query = "update billing.m_company_invoice_master set due_amount = (cast(coalesce(round(cast(due_amount as numeric), 2), 0) as double precision) - :amount) "
				+ "where company_invoice_id =:id"),
		
		@NamedNativeQuery(name = "UPDATE_COMPANY_INVOICE_MASTER_DUE_AMOUNT_BY_DTLS_ID", query = "update billing.m_company_invoice_master set "
				+ "due_amount = (due_amount - :amount) "
				+ "where company_invoice_id =(select company_invoice_id from billing.t_company_invoice_details where company_invoice_detail_id=:id )"),
		
		@NamedNativeQuery(name = "UPDATE_COMPANY_INVOICE_MASTER_SETTLEMENT_DATE", query = "update billing.m_company_invoice_master set updated_date=:updatedDate "
				+ "where company_invoice_id =:id"),
		
		@NamedNativeQuery(name = "GET_INVOICE_LIST_FOR_COMPANY_SETTLEMENT_DETAILS",
		 query ="select "
		+ "	inv.company_invoice_id as \"companyInvoiceId\", "
		+ "	inv.invoice_number as \"invoiceNumber\", "
		+ "	comp.company_desc as \"companyName\", "
		+ "	bm.total_bill_amount as \"totalAmount\", "
		+ "	inv.due_amount as \"dueAmount\", "
		+ "	to_char(inv.invoice_date, 'DD-MM-YYYY') as \"invoiceDate\", "
		+ "	inv.payment_entitlement_id as \"paymentEntitlementId\", "
		+ "	inv.payee_id as \"payeeId\" "
		+ "from "
		+ "	billing.m_company_invoice_master inv "
		+ "inner join public.t_company_master comp on "
		+ "	comp.company_id = inv.payee_id "
		+ "inner join billing.t_company_invoice_details cid on "
		+ "	inv.company_invoice_id = cid.company_invoice_id "
		+ "inner join billing.m_billing_master bm on "
		+ "	bm.billing_master_id = cid.bill_id and bm.bill_cancelled = 'N' "
		+ "where "
		+ "	inv.organisation_id = :orgId "
		+ "	and inv.unit_id = :unitId "
		+ "	and inv.due_amount = 0 "
		+ "union all "
		+ " select "
		+ "	inv.company_invoice_id as \"companyInvoiceId\", "
		+ "	inv.invoice_number as \"invoiceNumber\", "
		+ "	comp.company_desc as \"companyName\", "
		+ "	sale.total_amount as \"totalAmount\", "
		+ "	inv.due_amount as \"dueAmount\", "
		+ "	to_char(inv.invoice_date, 'DD-MM-YYYY') as \"invoiceDate\", "
		+ "	inv.payment_entitlement_id as \"paymentEntitlementId\", "
		+ "	inv.payee_id as \"payeeId\" "
		+ "from "
		+ "	billing.m_company_invoice_master inv "
		+ "inner join public.t_company_master comp on "
		+ "	comp.company_id = inv.payee_id "
		+ "inner join billing.t_company_invoice_details cid on "
		+ "	inv.company_invoice_id = cid.company_invoice_id "
		+ "inner join pharmacy.t_sale sale on "
		+ "	sale.id = cid.t_sale_id "
		+ "where "
		+ "	inv.organisation_id = :orgId "
		+ "	and inv.unit_id = :unitId "
		+ "	and inv.due_amount = 0")

})

@Entity
@Table(name = "m_company_invoice_master", schema = "billing")
@SequenceGenerator(name = "m_seq_company_invoice_master", sequenceName = "billing.m_seq_company_invoice_master", allocationSize = 1)
public class MCompanyInvoiceMaster {

	@Id
	@Column(name = "company_invoice_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_company_invoice_master")
	private int companyInvoiceId;

	@Column(name = "invoice_number")
	private String invoiceNumber;

	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "due_amount")
	private Double dueAmount;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer orgId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "invoice_date")
	@Convert(converter = DateConverter.class)
	private Long invoiceDate;

	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;
	
	@Column(name = "associate_company_id")
	private Integer associateCompanyId;
	
	@Column(name="cr_net_amount")
	private Double crNetAmount;
	
	@Column(name="invoice_number_system_gen")
	private Character invoiceNumberSystemGen;
	
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

	public int getCompanyInvoiceId() {
		return companyInvoiceId;
	}

	public void setCompanyInvoiceId(int companyInvoiceId) {
		this.companyInvoiceId = companyInvoiceId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
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

	public Long getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Long invoiceDate) {
		this.invoiceDate = invoiceDate;
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

	public Character getInvoiceNumberSystemGen() {
		return invoiceNumberSystemGen;
	}

	public void setInvoiceNumberSystemGen(Character invoiceNumberSystemGen) {
		this.invoiceNumberSystemGen = invoiceNumberSystemGen;
	}

	
}
