package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedNativeQueries({
	
/*	@NamedNativeQuery(name="GET_COMP_INVOICE_SERVICE_DTLS_BY_INVOICE",
						query="select "
								+" 	distinct comServDtls.comp_inv_ser_detls_id as \"compInvSerDetlsId\", "
								+" 	comServDtls.invoice_detail_id as \"invoiceDetailId\" , "
								+" 	comServDtls.invoice_id as \"invoiceId\" , "
								+" 	comServDtls.service_id as \"serviceId\" , "
								+" 	cast(comServDtls.credit_payable_amount as double precision ) as \"creditPayableAmount\", "
								+" 	comServDtls.due_amount as \"dueAmount\", "
								+" 	comServDtls.status as \"status\" , "
								+" 	billDtls.billing_master_id as \"billingMasterId\" , "
								+" 	billDtls.order_details_id as \"orderDetailsId\", "
								+" 	cast(billDtls.rate as double precision)as \"unitPrice\", "
								+" 	cast(billDtls.quantity as double precision) as \"quantity\" , "
								+" 	billDtls.concession as \"concession\" , "
								+" 	billDtls.discount as \"discount\", "
								+" 	to_char(orderDtls.order_date, "
								+" 	'DD-MM-YYYY') as \"orderDate\", "
								+" 	orderDtls.ord_doc_id as \"doctorId\" , "
								+" 	doc.first_name as \"firstName\" , "
								+" 	doc.middle_name as \"middleName\" , "
								+" 	doc.last_name as \"lastName\", "
								+" 	om.order_no as \"orderNo\" , "
								+" 	serv.service_standard_name as \"serviceName\", "
								+" 	billDtls.tax_amount as \"gstAmount\" , "
								+" 	billDtls.tax_per as \"gstPercentage\", "
								+" 	billDtls.billing_details_id as \"billDetailId\" "
								+" from "
								+" 	billing.t_company_invoice_service_details comServDtls "
								+" inner join billing.t_company_invoice_details compInvDtls on "
								+" 	compInvDtls.company_invoice_detail_id = comServDtls.invoice_detail_id "
								+" inner join billing.m_billing_master bill on "
								+" 	bill.billing_master_id = compInvDtls.bill_id "
								+" inner join billing.t_billing_details billDtls on "
								+" 	billDtls.billing_master_id = bill.billing_master_id "
								+" 	and billDtls.billing_details_id = comServDtls.bill_detail_id "
								+" inner join public.t_order_details orderDtls on "
								+" 	orderDtls.order_details_id = billDtls.order_details_id "
								+" left join doctor.m_doctor_master doc on "
								+" 	orderDtls.ord_doc_id = doc.doctor_id "
								+" inner join public.t_order_master om on "
								+" 	om.order_id = orderDtls.order_id "
								+" inner join service.m_service_master serv on "
								+" 	serv.service_master_id = billDtls.service_id "
								+" where "
								+" 	compInvDtls.company_invoice_detail_id = :invoiceDetailId "
								+" 	and comServDtls.due_amount > 0 "),
	*/
	
	@NamedNativeQuery(name="GET_COMP_INVOICE_SERVICE_DTLS_BY_INVOICE",
			query=   " select " 
					 + " 	distinct comServDtls.comp_inv_ser_detls_id as \"compInvSerDetlsId\", " 
					 + " 	comServDtls.invoice_detail_id as \"invoiceDetailId\" , " 
					 + " 	comServDtls.invoice_id as \"invoiceId\" , " 
					 + " 	comServDtls.service_id as \"serviceId\" , " 
					 + " 	cast(comServDtls.due_amount as double precision ) as \"creditPayableAmount\", " 
					 + " 	comServDtls.due_amount as \"dueAmount\", " 
					 + " 	comServDtls.status as \"status\" , " 
					 + " 	billDtls.billing_master_id as \"billingMasterId\" , " 
					 + " 	0 as \"saleId\" , " 
					 + " 	billDtls.order_details_id as \"orderDetailsId\", " 
					 + " 	cast(billDtls.rate as double precision)as \"unitPrice\", " 
					 + " 	cast(billDtls.quantity as double precision) as \"quantity\" , " 
					 + " 	billDtls.concession as \"concession\" , " 
					 + " 	billDtls.discount as \"discount\", " 
					 + " 	coalesce(comServDtls.cr_net_amount,0) as \"crNetAmount\", " 
					 + " 	to_char(orderDtls.order_date, 'DD-MM-YYYY') as \"orderDate\", " 
					 + " 	orderDtls.ord_doc_id as \"doctorId\" , " 
					 + " 	doc.first_name as \"firstName\" , " 
					 + " 	doc.middle_name as \"middleName\" , " 
					 + " 	doc.last_name as \"lastName\", " 
					 + " 	om.order_no as \"orderNo\" , " 
					 + " 	dtls.is_procedure as \"isProcedure\", " 
					 + " 	dtls.is_bed_side as \"isBedSide\", " 
					 + " 	dtls.procedure_type_id as \"procedureTypeId\", " 
					 + " 	serv.speciality_id as \"specialityId\", " 
					 + " 	serv.service_standard_name as \"serviceName\", " 
					 + " 	billDtls.tax_amount as \"gstAmount\" , " 
					 + " 	billDtls.tax_per as \"gstPercentage\", " 
					 + " 	billDtls.billing_details_id as \"billDetailId\", " 
					 + " 	billDtls.self_payable as \"selfPayable\", " 
					 + " 	billDtls.net_amt as \"netPayable\", " 
					 + " 	0 as \"tSaleDetailsId\", " 
					 + " 	cast(0 as double precision) as \"receivedAmount\", " 
					 + " 	cast(0 as double precision) as \"disallowedAmount\", " 
					 + " 	cast(0 as double precision) as \"tdsAmount\", " 
					 + " 	cast(0 as double precision) as \"otherDeduction\", " 
					 + " 	0 as \"tdsPercentage\", " 
					 + " 	0 as \"itemId\" " 
					 + " from " 
					 + " 	billing.t_company_invoice_service_details comServDtls " 
					 + " inner join billing.t_company_invoice_details compInvDtls on " 
					 + " 	compInvDtls.company_invoice_detail_id = comServDtls.invoice_detail_id " 
					 + " inner join billing.m_billing_master bill on " 
					 + " 	bill.billing_master_id = compInvDtls.bill_id " 
					 + " 	and bill.bill_cancelled = 'N' " 
					 + " inner join billing.t_billing_details billDtls on " 
					 + " 	billDtls.billing_master_id = bill.billing_master_id " 
					 + " 	and billDtls.billing_details_id = comServDtls.bill_detail_id " 
					 + " inner join public.t_order_details orderDtls on " 
					 + " 	orderDtls.order_details_id = billDtls.order_details_id " 
					 + " inner join public.t_order_details_package_mapper odpm on " 
					 + " 	orderDtls.order_details_id = odpm.order_details_id " 
					 + " 	and odpm.status = 'A' " 
					 + " 	and odpm.is_package_service = 'N' " 
					 + " left join doctor.m_doctor_master doc on " 
					 + " 	orderDtls.ord_doc_id = doc.doctor_id " 
					 + " inner join public.t_order_master om on " 
					 + " 	om.order_id = orderDtls.order_id " 
					 + " inner join service.m_service_master serv on " 
					 + " 	serv.service_master_id = billDtls.service_id " 
					 + " left join service.t_unit_service_master_details dtls on " 
					 + " 	dtls.service_master_id = serv.service_master_id " 
					 + " 	and dtls.organization_id = :organisationId " 
					 + " 	and dtls.unit_id = :unitId " 
					 + " where " 
					 + " 	compInvDtls.company_invoice_detail_id = :invoiceDetailId " 
					 + " 	and comServDtls.due_amount <> 0 " 
					 + " 	and orderDtls.status = 'A' " 
					 + " union " 
					 + " select " 
					 + " 	distinct comServDtls.comp_inv_ser_detls_id as \"compInvSerDetlsId\", " 
					 + " 	comServDtls.invoice_detail_id as \"invoiceDetailId\" , " 
					 + " 	comServDtls.invoice_id as \"invoiceId\" , " 
					 + " 	comServDtls.service_id as \"serviceId\" , " 
					 + " 	cast(comServDtls.due_amount as double precision ) as \"creditPayableAmount\", " 
					 + " 	comServDtls.due_amount as \"dueAmount\", " 
					 + " 	comServDtls.status as \"status\" , " 
					 + " 	0 as \"billingMasterId\" , " 
					 + " 	cast(sale.id as integer) as \"saleId\" , " 
					 + " 	0 as \"orderDetailsId\", " 
					 + " 	cast(batch.rate as double precision)as \"unitPrice\", " 
					 + " 	cast(saledetails.quantity as double precision) as \"quantity\" , " 
					 + " 	0 as \"concession\" , " 
					 + " 	saledetails.discount_amount as \"discount\", " 
					 + " 	coalesce(comServDtls.cr_net_amount,0) as \"crNetAmount\", " 
					 + " 	to_char(sale.added_date, 'DD-MM-YYYY') as \"orderDate\", " 
					 + " 	sale.doctor_id as \"doctorId\" , " 
					 + " 	doc.first_name as \"firstName\" , " 
					 + " 	doc.middle_name as \"middleName\" , " 
					 + " 	doc.last_name as \"lastName\", " 
					 + " 	'-' as \"orderNo\" , " 
					 + " 	'N' as \"isProcedure\", " 
					 + " 	'N' as \"isBedSide\", " 
					 + " 	0 as \"procedureTypeId\", " 
					 + " 	0 as \"specialityId\", " 
					 + " 	item.name as \"serviceName\", " 
					 + " 	saledetails.tax_amount as \"gstAmount\" , " 
					 + " 	saledetails.tax_percentage as \"gstPercentage\", " 
					 + " 	0 as \"billDetailId\", " 
					 + " 	saledetails.self_payable as \"selfPayable\", " 
					 + " 	saledetails.net_amount as \"netPayable\", " 
					 + " 	cast(saledetails.id as integer) as \"tSaleDetailsId\", " 
					 + " 	cast(0 as double precision) as \"receivedAmount\", " 
					 + " 	cast(0 as double precision) as \"disallowedAmount\", " 
					 + " 	cast(0 as double precision) as \"tdsAmount\", " 
					 + " 	cast(0 as double precision) as \"otherDeduction\", " 
					 + " 	0 as \"tdsPercentage\", " 
					 + " 	item.id as \"itemId\" " 
					 + " from " 
					 + " 	billing.t_company_invoice_service_details comServDtls " 
					 + " inner join billing.t_company_invoice_details compInvDtls on " 
					 + " 	compInvDtls.company_invoice_detail_id = comServDtls.invoice_detail_id " 
					 + " inner join pharmacy.t_sale sale on " 
					 + " 	sale.id = compInvDtls.t_sale_id " 
					 + " inner join pharmacy.t_sale_detail saledetails on " 
					 + " 	saledetails.sale_id = sale.id " 
					 + " 	and saledetails.id = comServDtls.t_sale_details_id " 
					 + " inner join public.t_sale_detail_package_mapper sdpkgm on " 
					 + " 	sdpkgm.t_sale_details_id = saledetails.id " 
					 + " 	and sdpkgm.status = 'A' " 
					 + " 	and sdpkgm.is_package_item = 'N' " 
					 + " inner join inventory.t_batch batch on " 
					 + " 	batch.id = saledetails.batch_id " 
					 + " 	and batch.is_active = true " 
					 + " 	and batch.is_deleted = false " 
					 + " inner join public.m_item item on " 
					 + " 	item.id = batch.item_id " 
					 + " left join doctor.m_doctor_master doc on " 
					 + " 	sale.doctor_id = doc.doctor_id " 
					 + " where " 
					 + " 	compInvDtls.company_invoice_detail_id = :invoiceDetailId " 
					 + " 	and comServDtls.due_amount <> 0  " ),
	
	@NamedNativeQuery(name="UPDATE_RECV_AND_DIALL_AMOUNT_BY_INV_DTLS",
						query="update billing.t_company_invoice_service_details set received_amount = :receivedAmount , "
								+ "disallowed_amount = :disallowedAmount , "
								+ "due_amount = :dueAmount, "
								+ "tds_amount=:tdsAmount "
								+ "where invoice_detail_id =:invoiceDetailId and service_id =:serviceId "),
								
	@NamedNativeQuery(name="GET_AMT_DTLS_BY_SERVICE_AND_INV_DTLS_ID",
						query="select cast(compServ.credit_payable_amount as double precision) as \"creditPayableAmount\","
								+ "		cast(compServ.due_amount as double precision) as \"dueAmount\" , cast(compServ.received_amount as double precision) \"receivedAmount\" ,"
								+ "		cast(compServ.disallowed_amount as double precision) as \"disallowedAmount\" "
								+ " from billing.t_company_invoice_service_details compServ "
								+ " where compServ.comp_inv_ser_detls_id = :compInvSerDetlsId ")
})

@Entity
@Table(name="t_company_invoice_service_details" , schema="billing")
@SequenceGenerator(name="t_company_invoice_service_details_seq" , sequenceName="billing.t_company_invoice_service_details_seq" , allocationSize=1)
public class TCompanyInvoiceServiceDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_company_invoice_service_details_seq")
	@Column(name="comp_inv_ser_detls_id")
	private Integer compInvSerDetlsId;
	
	@Column(name="invoice_id")
	private Integer invoiceId;
	
	@Column(name="invoice_detail_id")
	private Integer invoiceDetailId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="credit_payable_amount")
	private Double creditPayableAmount;
	
	@Column(name="due_amount")
	private Double dueAmount;	
	
	@Column(name="received_amount")
	private Double receivedAmount;
	
	@Column(name="disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name="other_deduction")
	private Double otherDeduction;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name ="org_id")
	private Integer orgId;
	
	@Column(name="org_unit_id")
	private Integer orgUnitId;

	@Column(name = "bill_detail_id")
	private Integer billDetailId;
	
	@Column(name="tds_amount")
	private Double tdsAmount;
	
	@Column(name="cr_net_amount")
	private Double crNetAmount;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "t_sale_details_id")
	private Integer tSaleDetailsId;
	
	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public Double getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public Double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public Integer getCompInvSerDetlsId() {
		return compInvSerDetlsId;
	}

	public void setCompInvSerDetlsId(Integer compInvSerDetlsId) {
		this.compInvSerDetlsId = compInvSerDetlsId;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getInvoiceDetailId() {
		return invoiceDetailId;
	}

	public void setInvoiceDetailId(Integer invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getCreditPayableAmount() {
		return creditPayableAmount;
	}

	public void setCreditPayableAmount(Double creditPayableAmount) {
		this.creditPayableAmount = creditPayableAmount;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getBillDetailId() {
		return billDetailId;
	}

	public void setBillDetailId(Integer billDetailId) {
		this.billDetailId = billDetailId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer gettSaleDetailsId() {
		return tSaleDetailsId;
	}

	public void settSaleDetailsId(Integer tSaleDetailsId) {
		this.tSaleDetailsId = tSaleDetailsId;
	}
	
}
