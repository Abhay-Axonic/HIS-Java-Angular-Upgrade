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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import com.param.global.model.CompanyMaster;
import com.param.global.model.PatientRegistration;
@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_SANCTION_REQUEST_BY_PATIENT_ID", 
			  query ="select " + 
			  		"	fundsanctionreq.fundsanctionrequest_details_id as \"fundsanctionrequestDetailsId\", " + 
			  		"	fundsanctionreq.trust_id as \"trustId\", " + 
			  		"	fundsanctionreq.fundrequest_number as \"fundrequestNumber\", " + 
			  		"	fundsanctionreq.fundrequest_amount as \"fundrequestAmount\", " + 
			  		"	fundsanctionreq.fundrequest_remark as \"fundrequestRemark\", " + 
			  		"	fundsanctionreq.fundrequest_status_id as \"fundrequestStatusId\", " + 
			  		"	to_char(fundsanctionreq.fundrequest_date, 'dd/mm/yyyy') as \"fundrequestDate\", " + 
			  		"	fundsanctionreq.patient_id as \"patientId\", " + 
			  		"	fundsanctionreq.org_id as \"orgId\", " + 
			  		"	fundsanctionreq.unit_id as \"unitId\", " + 
			  		"	companymaster.company_desc as \"trustName\", " + 
			  		"	(coalesce(pre.prefix_code,'')|| ' ' ||coalesce(patientreg.first_name, '')|| ' ' || coalesce(patientreg.middle_name, '')|| ' ' || coalesce(patientreg.last_name, '')) as \"patientName\" " + 
			  		"   from " + 
			  		"	billing.t_fundsanction_request_details fundsanctionreq " + 
			  		"   inner join public.t_company_master companymaster on " + 
			  		"	fundsanctionreq.trust_id = companymaster.company_id " + 
			  		"   inner join patient.t_patient_registration patientreg on " + 
			  		"	fundsanctionreq.patient_id = patientreg.patient_id " + 
			  		"	left join m_prefix_master pre on pre.prefix_id = patientreg.prefix_id " +
			  		"   where " + 
			  		"	fundsanctionreq.patient_id =:patientId " + 
			  		"	and fundsanctionreq.org_id =:orgId	 " + 
			  		"	and fundsanctionreq.unit_id =:unitId  " + 
			  		"	and fundsanctionreq.status = 'A' "
			  ),
	
})



@Entity
@Table(name = "t_fundsanction_request_details", schema = "billing")
@SequenceGenerator(name = "t_fundsanction_request_details_seq", sequenceName = "billing.t_fundsanction_request_details_seq", allocationSize = 1)

public class FundSanctionRequestDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_fundsanction_request_details_seq")
	@Column(name = "fundsanctionrequest_details_id")
	private Integer fundsanctionrequestDetailsId;
	
	@Column(name = "trust_id")
	private Integer trustId;
	
	@Column(name="fundrequest_number")
	private String fundrequestNumber;
	
	@Column(name = "fundrequest_amount")
	private Double fundrequestAmount;
	
	@Column(name="fundrequest_remark")
	private String fundrequestRemark;
	
	@Column(name = "fundrequest_status_id")
	private Integer fundrequestStatusId;
	
	@Column(name="fundrequest_date")
	private Date fundrequestDate;
	
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

	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="company_id", insertable=false, updatable=false)
	private CompanyMaster companyMaster;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="patient_id", insertable=false, updatable=false)
	private PatientRegistration patientRegistration;
	
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

	public String getFundrequestNumber() {
		return fundrequestNumber;
	}

	public void setFundrequestNumber(String fundrequestNumber) {
		this.fundrequestNumber = fundrequestNumber;
	}

	public Double getFundrequestAmount() {
		return fundrequestAmount;
	}

	public void setFundrequestAmount(Double fundrequestAmount) {
		this.fundrequestAmount = fundrequestAmount;
	}

	public String getFundrequestRemark() {
		return fundrequestRemark;
	}

	public void setFundrequestRemark(String fundrequestRemark) {
		this.fundrequestRemark = fundrequestRemark;
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

	public Integer getFundrequestStatusId() {
		return fundrequestStatusId;
	}

	public void setFundrequestStatusId(Integer fundrequestStatusId) {
		this.fundrequestStatusId = fundrequestStatusId;
	}

	public Date getFundrequestDate() {
		return fundrequestDate;
	}

	public void setFundrequestDate(Date fundrequestDate) {
		this.fundrequestDate = fundrequestDate;
	}

	public CompanyMaster getCompanyMaster() {
		return companyMaster;
	}

	public void setCompanyMaster(CompanyMaster companyMaster) {
		this.companyMaster = companyMaster;
	}

	
	
	
	

	
}