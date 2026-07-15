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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.CompanyType;
import com.param.global.common.DateConverter;
import com.param.global.model.CompanyMaster;


@Entity
@Table(name="m_company_invoice_reminder_master", schema="billing")
@SequenceGenerator(name = "m_seq_company_invoice_reminder_master", sequenceName = "billing.m_seq_company_invoice_reminder_master", allocationSize = 1)
public class CompanyInvoiceReminderMaster {
	@Id
	@Column(name="company_invoice_reminder_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_company_invoice_reminder_master")
	private Integer companyInvoiceReminderId;
	
	@Column(name = "company_type_id")
	private Integer companyTypeId;
	
	@Column(name = "company_id")
	private Integer companyId;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "invoice_id")
	private Integer invoiceId;

	@Column(name = "reminder_date")
	@Convert(converter=DateConverter.class)
	private Long reminderDate;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "response")
	private String response;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer orgId;


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
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_type_id", insertable = false, nullable = false, updatable = false)
	private CompanyType companyType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id", insertable = false, nullable = false, updatable = false)
	private MCompanyInvoiceMaster mCompanyInvoiceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payee_id", insertable = false, nullable = false, updatable = false)
	private CompanyMaster companyMaster;

	public Integer getCompanyInvoiceReminderId() {
		return companyInvoiceReminderId;
	}

	public void setCompanyInvoiceReminderId(Integer companyInvoiceReminderId) {
		this.companyInvoiceReminderId = companyInvoiceReminderId;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}


	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Long getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Long reminderDate) {
		this.reminderDate = reminderDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
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
	
	
	
	
}
