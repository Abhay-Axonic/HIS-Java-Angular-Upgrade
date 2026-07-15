package com.param.billing.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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
@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_ACTIVE_FUNDINFLOW_BY_TRUST_ID", 
			query =" SELECT  fundinflow.fund_inflow_id as \"fundInFlowId\",  "
					+" fundinflow.trust_id as \"trustId\", companymaster.company_desc  as \"trustName\", fundinflow.fundtype_id as \"fundtypeId\",  "
					+" fundinflow.totalcollectedamount as \"totalCollectedAmount\", "
					+" fundinflow.available_amount as \"availableAmount\" "
					+" from billing.m_fund_inflow_master fundinflow "
					+" INNER JOIN  public.t_company_master companymaster on fundinflow.trust_id = companymaster.company_id "
					+" where fundinflow.trust_id=:trustId and fundinflow.org_id=:orgId and fundinflow.unit_id=:unitId and fundinflow.status='A'  and ( fundinflow.fundtype_id=1 or  fundinflow.fundtype_id=3 ) order by fundinflow.fund_inflow_id DESC "
			),
	
	@NamedNativeQuery(name = "GET_ACTIVE_ALL_FUND_INFLOWDETAILS_BY_TRUST_ID", 
	query =" SELECT  fundinflow.fund_inflow_id as \"fundInFlowId\",  "
			+" fundinflow.trust_id as \"trustId\", companymaster.company_desc as \"trustName\", "
			+" fundinflow.totalcollectedamount as \"totalCollectedAmount\", "
			+" fundinflow.fundtype_id  as \"fundtypeId\" , "
			+" fundinflow.available_amount as \"availableAmount\" ,fundinflow.bill_consume_amount as \"billConsumeAmount\"  "
			+" from billing.m_fund_inflow_master fundinflow "
			+" INNER JOIN  public.t_company_master companymaster on fundinflow.trust_id = companymaster.company_id "
			+" where fundinflow.trust_id=:trustId and fundinflow.org_id=:orgId and fundinflow.unit_id=:unitId and ( fundinflow.bill_consume_amount <> fundinflow.totalCollectedAmount ) order by fundinflow.fund_inflow_id DESC "
	),
	
	@NamedNativeQuery(name = "GET_FUND_INFLOWDETAILS_BY_FUNDINFLOW_ID",
			query=	" SELECT  "
					+ " fd.payment_mode_id as \"paymentModeId\", fd.amount as \"amount\","
					+ " fd.bank_id as \"bankId\", fd.cash_amount as \"cashAmount\", "
					+ " fd.fund_inflow_detail_id as \"fundInFlowDetailsId\" , fd.cheque_amount as \"chequeAmount\" , fd.check_number as \"chequeNumber\", fd.cheque_date as \"chequeDate\","
					+ " fd.created_date as \"creditDate\"  "
					+ " FROM billing.m_fund_inflow_details fd "
					+ " where fd.fund_inflow_id =:fundInFlowId "
					+"  AND  fd.org_id=:orgId "
					+ " AND fd.unit_id=:unitId "
					+ " order by fd.fund_inflow_detail_id desc "),
})
@Entity
@Table(name = "m_fund_inflow_master", schema = "billing")
@SequenceGenerator(name = "m_fund_inflow_master_seq", sequenceName = "billing.m_fund_inflow_master_seq", allocationSize = 1)

public class FundInFlowMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_fund_inflow_master_seq")
	@Column(name = "fund_inflow_id")
	private Integer fundInFlowId;
	
	@Column(name = "trust_id")
	private Integer trustId;
	
	@Column(name = "totalcollectedamount")
	private Double totalCollectedAmount;
	
	@Column(name = "available_amount ")
	private Double availableAmount;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private String status;
	
	
	@Column(name="fund_remark")
	private String fundRemark;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="fundtype_id")
	private Integer fundtypeId;
	
	@Column(name = "bill_consume_amount ")
	private Double billConsumeAmount;

	
	/*@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="trust_id", insertable=false, updatable=false)
	private TrustMaster trustMaster;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="fund_inflow_id", insertable=false, updatable=false)
	private FundInFlowDetails fundInFlowDetails;*/
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "trustMaster")
	private List<FundInFlowDetails> listfundInFlowDetails;
	*/
	public Integer getFundInFlowId() {
		return fundInFlowId;
	}

	public void setFundInFlowId(Integer fundInFlowId) {
		this.fundInFlowId = fundInFlowId;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public Double getTotalCollectedAmount() {
		return totalCollectedAmount;
	}

	public void setTotalCollectedAmount(Double totalCollectedAmount) {
		this.totalCollectedAmount = totalCollectedAmount;
	}

	public Double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFundRemark() {
		return fundRemark;
	}

	public void setFundRemark(String fundRemark) {
		this.fundRemark = fundRemark;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getFundtypeId() {
		return fundtypeId;
	}

	public void setFundtypeId(Integer fundtypeId) {
		this.fundtypeId = fundtypeId;
	}

	public Double getBillConsumeAmount() {
		return billConsumeAmount;
	}

	public void setBillConsumeAmount(Double billConsumeAmount) {
		this.billConsumeAmount = billConsumeAmount;
	}

	
}