package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_refund_transaction_details", schema = "public")
@SequenceGenerator(name = "t_refund_transaction_details_id_seq", sequenceName = "public.t_refund_transaction_details_id_seq", allocationSize = 1)

public class RefundTransactionMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_cheque_transaction_details_id_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="receipt_number")
	private Integer receiptNumber;
	
	@Column(name="receipt_date")
	private Date receiptDate;
	
	@Column(name="refund_amount")
	private Double refundAmount;
	
	@Column(name="invoice_number")
	private Integer invoiceNumber;
	
	@Column(name="invoice_id")
	private Integer invoiceId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="denomination_id")
	private Integer denominationId;
	
	@Column(name="is_verified")
	private Boolean isVerified;
	
	@Column(name="verified_by_id")
	private Integer verifiedById;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="cashier_id")
	private Integer cashierId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(Integer receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Integer invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getDenominationId() {
		return denominationId;
	}

	public void setDenominationId(Integer denominationId) {
		this.denominationId = denominationId;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Integer getVerifiedById() {
		return verifiedById;
	}

	public void setVerifiedById(Integer verifiedById) {
		this.verifiedById = verifiedById;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCashierId() {
		return cashierId;
	}

	public void setCashierId(Integer cashierId) {
		this.cashierId = cashierId;
	}

	
}
