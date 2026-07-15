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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.PatientRegistration;
@Entity
@Table(name = "t_fundinflow_virtual_approval_details", schema = "billing")
@SequenceGenerator(name = "t_fundinflow_virtual_approval_details_seq", sequenceName = "billing.t_fundinflow_virtual_approval_details_seq", allocationSize = 1)

public class FundInflowApprovalDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_fundinflow_virtual_approval_details_seq")
	@Column(name = "fundinflow_virtual_approval_details_id")
	private Integer fundInflowVirtualApprovalDetailsId;
	
	@Column(name = "fundsanctionrequest_approval_details_id")
	private Integer fundsanctionrequestApprovalDetailsId;
	
	@Column(name = "trust_id")
	private Integer trustId;
	
	@Column(name = "fund_inflow_id")
	private Integer fundInFlowId;
	
	@Column(name = "fund_approval_amount")
	private Double fundApprovalAmount;
	
	@Column(name = "trust_acceptance_type_id")
	private Integer fundAcceptacepaymentTypeId;
	
	@Column(name="fundrequest_approval_date")
	private Date fundrequestApprovalDate;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate; 
	
	@Column(name="status")
	private String status;
	
	
	@Column(name = "approval_bill_consume_amount")
	private Double ApprovalBillConsumeAmount;


	public Integer getFundsanctionrequestApprovalDetailsId() {
		return fundsanctionrequestApprovalDetailsId;
	}

	public void setFundsanctionrequestApprovalDetailsId(Integer fundsanctionrequestApprovalDetailsId) {
		this.fundsanctionrequestApprovalDetailsId = fundsanctionrequestApprovalDetailsId;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public Integer getFundInFlowId() {
		return fundInFlowId;
	}

	public void setFundInFlowId(Integer fundInFlowId) {
		this.fundInFlowId = fundInFlowId;
	}

	public Double getFundApprovalAmount() {
		return fundApprovalAmount;
	}

	public void setFundApprovalAmount(Double fundApprovalAmount) {
		this.fundApprovalAmount = fundApprovalAmount;
	}

	public Integer getFundAcceptacepaymentTypeId() {
		return fundAcceptacepaymentTypeId;
	}

	public void setFundAcceptacepaymentTypeId(Integer fundAcceptacepaymentTypeId) {
		this.fundAcceptacepaymentTypeId = fundAcceptacepaymentTypeId;
	}

	public Date getFundrequestApprovalDate() {
		return fundrequestApprovalDate;
	}

	public void setFundrequestApprovalDate(Date fundrequestApprovalDate) {
		this.fundrequestApprovalDate = fundrequestApprovalDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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


	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getFundInflowVirtualApprovalDetailsId() {
		return fundInflowVirtualApprovalDetailsId;
	}

	public void setFundInflowVirtualApprovalDetailsId(Integer fundInflowVirtualApprovalDetailsId) {
		this.fundInflowVirtualApprovalDetailsId = fundInflowVirtualApprovalDetailsId;
	}

	public Double getApprovalBillConsumeAmount() {
		return ApprovalBillConsumeAmount;
	}

	public void setApprovalBillConsumeAmount(Double approvalBillConsumeAmount) {
		ApprovalBillConsumeAmount = approvalBillConsumeAmount;
	}
	
	
	
	
	
	

	
}