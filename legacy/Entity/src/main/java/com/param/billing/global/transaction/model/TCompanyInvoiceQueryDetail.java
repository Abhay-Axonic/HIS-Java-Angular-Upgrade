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

@NamedNativeQueries(
{ 

@NamedNativeQuery(name = "GET_PENDING_COMPANY_QUERY_LIST", 
query ="select"
		+ " qd.company_invoice_query_detail_id as \"companyInvoiceQueryDetailId\","
		+ " qd.query_no as \"queryNo\","
		+ " qd.query_status_id as \"queryStatusId\","
		+ " to_char(qd.query_date, 'DD-MM-YYYY') AS \"queryDate\","
		+ " to_char(qd.response_date, 'YYYY-MM-DDTHH24:MI:SSZ') as \"responseDate\","
		+ "qd.query_details AS \"queryDetails\","
		+ " qd.response_details as \"responseDetails\","
		+ " qd.remark as \"remark\","
		+ "qd.invoice_detail_id as \"invoiceDetailId\" "
		+ "from"
		+ "  billing.t_company_invoice_query_detail qd "
		+ "WHERE"
		+ " qd.org_id = :orgId "
		+ " AND qd.unit_id = :unitId "
		+ " AND qd.query_status_id = :queryStatusId "
		+" AND qd.invoice_detail_id=:companyInvoiceQueryDetailId ")
})

@Entity
@Table(name="t_company_invoice_query_detail",schema="billing")
@SequenceGenerator(name = "t_seq_company_invoice_query_detail", sequenceName = "billing.t_seq_company_invoice_query_detail", allocationSize = 1)
public class TCompanyInvoiceQueryDetail {

	@Id
	@Column(name="company_invoice_query_detail_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_company_invoice_query_detail")
	private int companyInvoiceQueryDetailId;
	
	@Column(name="invoice_detail_id")
	private Integer invoiceDetailId;
	
	@Column(name="company_id")
	private Integer companyId;
	
	@Column(name="query_details")
	private String queryDetails;
	
	@Column(name="query_no")
	private String queryNo;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="response_details")
	private String responseDetails;
	
	@Column(name="query_status_id")
	private Integer queryStatusId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;
	
	@Column(name="query_date")
	@Convert(converter=DateConverter.class)
	private Long queryDate;
	
	@Column(name="response_date")
	@Convert(converter=DateConverter.class)
	private Long responseDate;
	
	@Column(name="updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="org_id")
	private Integer orgId;


	public int getCompanyInvoiceQueryDetailId() {
		return companyInvoiceQueryDetailId;
	}

	public void setCompanyInvoiceQueryDetailId(int companyInvoiceQueryDetailId) {
		this.companyInvoiceQueryDetailId = companyInvoiceQueryDetailId;
	}

	public Integer getInvoiceDetailId() {
		return invoiceDetailId;
	}

	public void setInvoiceDetailId(Integer invoiceDetailId) {
		this.invoiceDetailId = invoiceDetailId;
	}

	public String getQueryDetails() {
		return queryDetails;
	}

	public void setQueryDetails(String queryDetails) {
		this.queryDetails = queryDetails;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResponseDetails() {
		return responseDetails;
	}

	public void setResponseDetails(String responseDetails) {
		this.responseDetails = responseDetails;
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

	public Long getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Long queryDate) {
		this.queryDate = queryDate;
	}

	public Long getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Long responseDate) {
		this.responseDate = responseDate;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}


	public Integer getQueryStatusId() {
		return queryStatusId;
	}

	public void setQueryStatusId(Integer queryStatusId) {
		this.queryStatusId = queryStatusId;
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getQueryNo() {
		return queryNo;
	}

	public void setQueryNo(String queryNo) {
		this.queryNo = queryNo;
	}

	
}
