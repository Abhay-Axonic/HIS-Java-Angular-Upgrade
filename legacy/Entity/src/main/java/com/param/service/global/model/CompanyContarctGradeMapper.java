package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_company_contarct_grade_mpper",schema="service")
@SequenceGenerator(name="t_company_contarct_grade_mpper_seq",sequenceName="service.t_company_contarct_grade_mpper_seq")
public class CompanyContarctGradeMapper {

	@Id
	@Column(name="company_contarct_grade_mpper_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_company_contarct_grade_mpper_seq")
	private Integer companyContarctGradeMpperId;
	
	@Column(name="company_id")
	private Integer companyId;
	
	@Column(name="contract_grade_master_id")
	private Integer contractGradeMasterId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="org_unit_id")
	private Integer orgUnitId;
	
	@Column(name="status")
	private Character status;

	

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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCompanyContarctGradeMpperId() {
		return companyContarctGradeMpperId;
	}

	public void setCompanyContarctGradeMpperId(Integer companyContarctGradeMpperId) {
		this.companyContarctGradeMpperId = companyContarctGradeMpperId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getContractGradeMasterId() {
		return contractGradeMasterId;
	}

	public void setContractGradeMasterId(Integer contractGradeMasterId) {
		this.contractGradeMasterId = contractGradeMasterId;
	}
	
	
}
