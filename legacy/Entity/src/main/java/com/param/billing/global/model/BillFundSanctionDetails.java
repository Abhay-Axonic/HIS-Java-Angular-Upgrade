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
	
	@NamedNativeQuery(name = "GET_BILLFUND_DETAILS_BY_BILLMASTER_ID", 
			query = "select " + 
					"	fundbill.bill_fund_details_id as \"billFundDetailsId\", " + 
					"	fundbill.bill_master_id as \"billingMasterId\" , " + 
					"	fundbill.bill_fundconsumption_type_id as \"billFundconsumptionTypeId\", " + 
					"	fundbill.bill_fund_selected_amount as \"billAcceptancePaidAmount\", " + 
					"	fundbill.trust_acceptance_type_id  as \"fundAcceptacepaymentTypeId\", " + 
					"	fundbill.trust_id as \"trustId\", " + 
					"	fundbill.fund_inflow_id as \"fundInFlowId\" , " + 
					"	fundbill.patient_id as \"patientId\", " + 
					"	fundbill.fundsanctionrequest_details_id as \"fundsanctionrequestDetailsId\", " + 
					"	fundbill.fundsanctionrequest_approval_details_id as \"fundsanctionrequestApprovalDetailsId\", " + 
				    "   to_char(fundbill.created_date, 'dd/mm/yyyy') as \"createdDate\", "+
					"	fundbill.org_id as \"orgId\",  " + 
					"	fundbill.unit_id as \"unitId\", " + 
					"	reqdetails.fundrequest_number as \"fundrequestNumber\", " + 
					"	company.company_desc as \"trustName\", " + 
					"	approvedetails.fundrequest_approval_amount as  \"requestApprovalAmount\", " + 
					"	approvedetails.approval_bill_consume_amount as \"approvalBillConsumeAmount\" " + 
					"   from " + 
					"	billing.t_fund_bill_details fundbill " + 
					"	left join billing.t_fundsanction_request_details reqdetails " + 
					"	on fundbill.fundsanctionrequest_details_id=reqdetails.fundsanctionrequest_details_id " + 
					"	left join billing.t_fundsanction_request_approval_details approvedetails " + 
					"	on fundbill.fundsanctionrequest_approval_details_id=approvedetails.fundsanctionrequest_approval_details_id " + 
					"	left join public.t_company_master company  " + 
					"	on fundbill.trust_id=company.company_id  " + 
					"   where " + 
					"	fundbill.bill_master_id =:billingMasterId " + 
					"	and fundbill.org_id =:orgId	 " + 
			  		"	and fundbill.unit_id =:unitId  " + 
					"	and fundbill.status = 'A'  order by fundbill.bill_fund_details_id "
			)
	
	})


@Entity
@Table(name = "t_fund_bill_details", schema = "billing")
@SequenceGenerator(name = "t_fund_bill_details_seq", sequenceName = "billing.t_fund_bill_details_seq", allocationSize = 1)

public class BillFundSanctionDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_fund_bill_details_seq")
	@Column(name = "bill_fund_details_id")
	private Integer billFundDetailsId;
	
	@Column(name = "bill_master_id")
	private Integer billingMasterId;
	
	@Column(name = "bill_fundconsumption_type_id")
	private Integer billFundconsumptionTypeId;
	
	@Column(name = "fundsanctionrequest_details_id")
	private Integer fundsanctionrequestDetailsId;
	
	@Column(name = "fundsanctionrequest_approval_details_id")
	private Integer fundsanctionrequestApprovalDetailsId;
	
	@Column(name = "trust_acceptance_type_id")
	private Integer fundAcceptacepaymentTypeId;
	
	@Column(name = "trust_id")
	private Integer trustId;
	
	@Column(name = "fund_inflow_id")
	private Integer fundInFlowId;
	
	@Column(name = "bill_fund_selected_amount")
	private Double billFundSelectedAmount;
	
	@Column(name="patient_id")
	private Integer patientId;
	
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


	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getBillFundconsumptionTypeId() {
		return billFundconsumptionTypeId;
	}

	public void setBillFundconsumptionTypeId(Integer billFundconsumptionTypeId) {
		this.billFundconsumptionTypeId = billFundconsumptionTypeId;
	}

	public Integer getFundsanctionrequestDetailsId() {
		return fundsanctionrequestDetailsId;
	}

	public void setFundsanctionrequestDetailsId(Integer fundsanctionrequestDetailsId) {
		this.fundsanctionrequestDetailsId = fundsanctionrequestDetailsId;
	}

	public Integer getFundsanctionrequestApprovalDetailsId() {
		return fundsanctionrequestApprovalDetailsId;
	}

	public void setFundsanctionrequestApprovalDetailsId(Integer fundsanctionrequestApprovalDetailsId) {
		this.fundsanctionrequestApprovalDetailsId = fundsanctionrequestApprovalDetailsId;
	}

	public Integer getFundAcceptacepaymentTypeId() {
		return fundAcceptacepaymentTypeId;
	}

	public void setFundAcceptacepaymentTypeId(Integer fundAcceptacepaymentTypeId) {
		this.fundAcceptacepaymentTypeId = fundAcceptacepaymentTypeId;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public Double getBillFundSelectedAmount() {
		return billFundSelectedAmount;
	}

	public void setBillFundSelectedAmount(Double billFundSelectedAmount) {
		this.billFundSelectedAmount = billFundSelectedAmount;
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

	public Integer getBillFundDetailsId() {
		return billFundDetailsId;
	}

	public void setBillFundDetailsId(Integer billFundDetailsId) {
		this.billFundDetailsId = billFundDetailsId;
	}

	public Integer getFundInFlowId() {
		return fundInFlowId;
	}

	public void setFundInFlowId(Integer fundInFlowId) {
		this.fundInFlowId = fundInFlowId;
	}

	
	
	
	

	
}