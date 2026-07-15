package com.param.billing.global.model;

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
	
	@NamedNativeQuery(name = "GET_ALLPPENDING_REQUEST_ACCEPTED_VIRTUALAMOUNT_BY_TRUST_ID", 
			query = "select appdetails.fundsanctionrequest_approval_details_id as \"fundsanctionrequestApprovalDetailsId\", "
			        + " appdetails.trust_id as \"trustId\",  "
					+ " appdetails.fundsanctionrequest_details_id as \"fundsanctionrequestDetailsId\", "
			        +"  appdetails.fundrequest_amount as \"fundrequestAmount\", " 
					+"	acceptancedetails.fund_approval_amount as \"requestApprovalAmount\", "
					+"	acceptancedetails.approval_bill_consume_amount as \"approvalBillConsumeAmount\" , "
					+"	appdetails.virtual_paid_amount as \"virtualPaidAmount\" , "
					+"  request.fundrequest_number as \"fundrequestNumber\", "
					+"  concat(coalesce(pre.prefix_code,''),' ',coalesce( patient.first_name,''),' ',coalesce( patient.middle_name,'' ),' ',coalesce( patient.last_name,'' )) as \"patientName\"  "
					+"  from "  
					+"	billing.t_fundsanction_request_approval_details appdetails "  
					+"	inner join billing.t_fundsanction_request_details request  "
					+"	on appdetails.fundsanctionrequest_details_id =request.fundsanctionrequest_details_id "  
					+"	inner join billing.t_fundinflow_virtual_approval_details  acceptancedetails "  
					+"	on  appdetails.fundsanctionrequest_approval_details_id=acceptancedetails.fundsanctionrequest_approval_details_id " 
					+"	inner join patient.t_patient_registration patient " 
					+"  on  appdetails.patient_id=patient.patient_id "
					+"  left join m_prefix_master pre on pre.prefix_id = patient.prefix_id "
					+"  where "  
					+"	     appdetails.trust_id =:trustId "  
					+"	     and appdetails.trust_acceptance_type_id = 2 " 
					+"	     and appdetails.org_id =:orgId "  
					+"	     and appdetails.unit_id =:unitId "  
					+"	     and appdetails.fundrequest_approval_amount <> appdetails.virtual_paid_amount    "
					),
	
	
	@NamedNativeQuery(name = "GET_REQUEST_ACCEPTED_BY_INFLOW_ID", 
	query = 
			"select "
				 +" vertualdetalis.fundsanctionrequest_approval_details_id as \"fundsanctionrequestApprovalDetailsId\" ,"
				 +"	vertualdetalis.fund_approval_amount as \"requestApprovalAmount\" ,	 "
				 +"	reqapprove.fundsanctionrequest_details_id as  \"fundsanctionrequestDetailsId\"  , "
				 +"	reqapprove.fundrequest_amount as \"fundrequestAmount\", "
				 +"	vertualdetalis.patient_id as \"patientId\", "
				 +"	concat(coalesce(pre.prefix_code,''),' ',coalesce( patient.first_name,''),' ',coalesce( patient.middle_name,'' ),' ',coalesce( patient.last_name,'' )) as \"patientName\" , "
				 +"	req.fundrequest_number as \"fundrequestNumber\" "
				 +"  from "
				 +"	billing.t_fundinflow_virtual_approval_details vertualdetalis "
				 +" inner join billing.t_fundsanction_request_approval_details reqapprove  "
				 +"	on vertualdetalis.fundsanctionrequest_approval_details_id = reqapprove.fundsanctionrequest_approval_details_id "
				 +" inner join billing.t_fundsanction_request_details req  "
				 +"	on reqapprove.fundsanctionrequest_details_id = req.fundsanctionrequest_details_id "
				 +" inner join patient.t_patient_registration patient " 
				 +"	on  vertualdetalis.patient_id=patient.patient_id "
				 +"  left join m_prefix_master pre on pre.prefix_id = patient.prefix_id "
				 +" where "
				 +"	vertualdetalis.fund_inflow_id =:fundInFlowId  and vertualdetalis.org_id =:orgId  and vertualdetalis.unit_id =:unitId  "
				 +" order by "
				 +"	vertualdetalis.fundinflow_virtual_approval_details_id desc "
		   ),
	
	
	
	@NamedNativeQuery(name = "GET_VIRTUAL_REQUEST_PAYMENTS_DETAILS", 
	query = 
			"	select " + 
			"	virtual.virtual_acceptance_paid_amount as \"virtualAcceptancePaidAmmount\" , " + 
			"	virtual.fund_inflow_id as  \"fundInFlowId\" , " + 
			"	virtual.fundsanctionrequest_approval_details_id as \"fundsanctionrequestApprovalDetailsId\" , " + 
			"	appdetails.fundrequest_approval_amount as \"requestApprovalAmount\",  " + 
			"	appdetails.fundsanctionrequest_details_id \"fundsanctionrequestDetailsId\" , " + 
			"	reqdetails.fundrequest_number as \"fundrequestNumber\", " + 
			"	appdetails.patient_id as \"patientId\", " + 
			"	concat(coalesce(pre.prefix_code,''),' ',coalesce( patient.first_name, ''), ' ', coalesce( patient.middle_name, '' ), ' ', coalesce( patient.last_name, '' )) as \"patientName\" " + 
			"   from " + 
			"	billing.m_fund_inflow_virtualacceptance_details virtual " + 
			"	inner join billing.m_fund_inflow_master  mstr  " + 
			"	on mstr.fund_inflow_id= virtual.fund_inflow_id " + 
			"	inner join billing.t_fundsanction_request_approval_details appdetails " + 
			"	on appdetails.fundsanctionrequest_approval_details_id =virtual.fundsanctionrequest_approval_details_id " + 
			"	inner join billing.t_fundsanction_request_details reqdetails " + 
			"	on  reqdetails.fundsanctionrequest_details_id=appdetails.fundsanctionrequest_details_id " + 
			"	inner join patient.t_patient_registration patient  " + 
			"	on appdetails.patient_id=patient.patient_id " + 
			"  left join m_prefix_master pre on pre.prefix_id = patient.prefix_id " + 
			"   where 	 " + 
			"	virtual.fund_inflow_id =:fundInFlowId " + 
			"	and " + 
			"	appdetails.org_id =:orgId " + 
			"	and appdetails.unit_id =:unitId  "
		   ),
	
	@NamedNativeQuery(name = "GET_ALL_FUND_REQUEST_PAYMENTS_DETAILS_PATIENT_ID", 
	query = "select " + 
			"	appdetail.fundsanctionrequest_approval_details_id as \"fundsanctionrequestApprovalDetailsId\" , " + 
			"	appdetail.fundsanctionrequest_details_id as \"fundsanctionrequestDetailsId\", " + 
			"   acceptancedetails.fundinflow_virtual_approval_details_id as \"fundInflowVirtualApprovalDetailsId\", "+
			"	appdetail.trust_id as \"trustId\", " + 
			"	company.company_desc as \"trustName\", " + 
			"	acceptancedetails.fund_approval_amount as \"requestApprovalAmount\" , " + 
			"	appdetail.trust_acceptance_type_id as \"fundAcceptacepaymentTypeId\", " + 
			"	appdetail.patient_id as \"patientId\", " + 
			"	acceptancedetails.approval_bill_consume_amount as \"approvalBillConsumeAmount\", " + 
			"	appdetail.virtual_paid_amount as \"virtualPaidAmount\", " + 
			"	acceptancedetails.fund_inflow_id as \"fundInFlowId\", " + 
			"	reqeust.fundrequest_number as \"fundrequestNumber\", " + 
			"	concat(coalesce(pre.prefix_code,''),' ',coalesce( patient.first_name, ''), ' ', coalesce( patient.middle_name, '' ), ' ', coalesce( patient.last_name, '' )) as \"patientName\" " + 
			"   from " + 
			"	billing.t_fundsanction_request_approval_details appdetail " + 
			"	inner join billing.t_fundinflow_virtual_approval_details  acceptancedetails " + 
			"	on  appdetail.fundsanctionrequest_approval_details_id=acceptancedetails.fundsanctionrequest_approval_details_id " + 
			"	inner join billing.t_fundsanction_request_details reqeust  " + 
			"	on appdetail.fundsanctionrequest_details_id=reqeust.fundsanctionrequest_details_id " + 
			"	inner join  public.t_company_master company " + 
			"	on  company.company_id=appdetail.trust_id " + 
			"	inner join patient.t_patient_registration patient   " + 
			"   on appdetail.patient_id=patient.patient_id " + 
			"  left join m_prefix_master pre on pre.prefix_id = patient.prefix_id "+
			"   where  " + 
			"	appdetail.patient_id =:patientId  " + 
			"	and appdetail.org_id =:orgId " + 
			"	and appdetail.unit_id =:unitId " + 
			"	and acceptancedetails.approval_bill_consume_amount <> acceptancedetails.fund_approval_amount "+
			"   order by  appdetail.fundsanctionrequest_approval_details_id  "  
		   )
	})



@Entity
@Table(name = "t_fundsanction_request_approval_details", schema = "billing")
@SequenceGenerator(name = "t_fundsanction_request_approval_details_seq", sequenceName = "billing.t_fundsanction_request_approval_details_seq", allocationSize = 1)

public class FundSanctionRequestApprovalDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_fundsanction_request_approval_details_seq")
	@Column(name = "fundsanctionrequest_approval_details_id")
	private Integer fundsanctionrequestApprovalDetailsId;
	
	
	@Column(name = "fundsanctionrequest_details_id")
	private Integer fundsanctionrequestDetailsId;
	
	@Column(name = "trust_id")
	private Integer trustId;
	
	
	@Column(name = "fundrequest_amount")
	private Double fundrequestAmount;
	
	@Column(name = "fundrequest_approval_amount")
	private Double fundrequestApprovalAmount;
	
	
	@Column(name = "fundrequest_approval_available_amount")
	private Double fundrequestApprovalAvailableAmount;
	
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
	
	@Column(name="is_fundinflow_given")
	private String isFundInflowGiven;
	
	@Column(name = "approval_bill_consume_amount")
	private Double approvalBillConsumeAmount;
	
	
	@Column(name = "virtual_paid_amount")
	private Double virtualPaidAmount;
	
	
	
	
	
	

	public Integer getFundsanctionrequestApprovalDetailsId() {
		return fundsanctionrequestApprovalDetailsId;
	}

	public void setFundsanctionrequestApprovalDetailsId(Integer fundsanctionrequestApprovalDetailsId) {
		this.fundsanctionrequestApprovalDetailsId = fundsanctionrequestApprovalDetailsId;
	}

	public Integer getFundsanctionrequestDetailsId() {
		return fundsanctionrequestDetailsId;
	}

	public void setFundsanctionrequestDetailsId(Integer fundsanctionrequestDetailsId) {
		this.fundsanctionrequestDetailsId = fundsanctionrequestDetailsId;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public Double getFundrequestAmount() {
		return fundrequestAmount;
	}

	public void setFundrequestAmount(Double fundrequestAmount) {
		this.fundrequestAmount = fundrequestAmount;
	}

	public Double getFundrequestApprovalAmount() {
		return fundrequestApprovalAmount;
	}

	public void setFundrequestApprovalAmount(Double fundrequestApprovalAmount) {
		this.fundrequestApprovalAmount = fundrequestApprovalAmount;
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

	public Double getFundrequestApprovalAvailableAmount() {
		return fundrequestApprovalAvailableAmount;
	}

	public void setFundrequestApprovalAvailableAmount(Double fundrequestApprovalAvailableAmount) {
		this.fundrequestApprovalAvailableAmount = fundrequestApprovalAvailableAmount;
	}

	public String getIsFundInflowGiven() {
		return isFundInflowGiven;
	}

	public void setIsFundInflowGiven(String isFundInflowGiven) {
		this.isFundInflowGiven = isFundInflowGiven;
	}

	public Double getApprovalBillConsumeAmount() {
		return approvalBillConsumeAmount;
	}

	public void setApprovalBillConsumeAmount(Double approvalBillConsumeAmount) {
		this.approvalBillConsumeAmount = approvalBillConsumeAmount;
	}

	public Double getVirtualPaidAmount() {
		return virtualPaidAmount;
	}

	public void setVirtualPaidAmount(Double virtualPaidAmount) {
		this.virtualPaidAmount = virtualPaidAmount;
	}
	
	

	
	
	
	
	

	
}