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

@Entity
@Table(name = "m_fund_inflow_diagnosis_details", schema = "billing")
@SequenceGenerator(name = "m_fund_inflow_diagnosis_details_seq", sequenceName = "billing.m_fund_inflow_diagnosis_details_seq", allocationSize = 1)

public class FundInFlowDiagnosisMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_fund_inflow_diagnosis_details_seq")
	@Column(name = "fund_inflow_diagnosis_detail_id")
	private Integer fundInflowDiagnosisDetailId;
	
	@Column(name = "fund_inflow_id")
	private Integer fundInflowId;
	
	@Column(name = "diagnosis_id")
	private Integer diagnosisId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;

	public Integer getFundInflowDiagnosisDetailId() {
		return fundInflowDiagnosisDetailId;
	}

	public void setFundInflowDiagnosisDetailId(Integer fundInflowDiagnosisDetailId) {
		this.fundInflowDiagnosisDetailId = fundInflowDiagnosisDetailId;
	}

	public Integer getFundInflowId() {
		return fundInflowId;
	}

	public void setFundInflowId(Integer fundInflowId) {
		this.fundInflowId = fundInflowId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
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
	
	
	

	
}