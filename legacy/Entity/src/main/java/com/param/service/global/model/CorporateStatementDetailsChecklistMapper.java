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
@Table(name = "t_corporate_statement_details_checklist_mapper" , schema = "billing")
@SequenceGenerator(name = "t_corporate_statement_details_checklist_mapper_seq" , sequenceName = "billing.t_corporate_statement_details_checklist_mapper_seq" , allocationSize = 1)

public class CorporateStatementDetailsChecklistMapper {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_corporate_statement_details_checklist_mapper_seq")
	@Column(name = "corporate_statement_details_checklist_mapper_id")
	private Integer corporateStatementDetailsChecklistId;
	
	@Column(name = "corporate_statement_id")
	private Integer corporateStatementId;
	
	@Column(name = "corporate_statement_checklist_id")
	private Integer corporateStatementChecklistId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getCorporateStatementDetailsChecklistId() {
		return corporateStatementDetailsChecklistId;
	}

	public Integer getCorporateStatementId() {
		return corporateStatementId;
	}

	public void setCorporateStatementId(Integer corporateStatementId) {
		this.corporateStatementId = corporateStatementId;
	}

	public void setCorporateStatementDetailsChecklistId(Integer corporateStatementDetailsChecklistId) {
		this.corporateStatementDetailsChecklistId = corporateStatementDetailsChecklistId;
	}

	public void setCorporateStatementChecklistId(Integer corporateStatementChecklistId) {
		this.corporateStatementChecklistId = corporateStatementChecklistId;
	}

	public Integer getCorporateStatementChecklistId() {
		return corporateStatementChecklistId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

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
	
	
	

}
