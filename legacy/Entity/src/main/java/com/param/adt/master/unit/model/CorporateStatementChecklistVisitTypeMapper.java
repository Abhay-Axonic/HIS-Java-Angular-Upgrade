package com.param.adt.master.unit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.VisitTypeMaster;


@NamedQuery(name="GET_CORPORATE_STATEMENT_CHECKLIST_VISIT_LIST",
query="SELECT bbc.visitTypeId as id "
//        + " bbc.corporateStatementChecklistId as corporateStatementChecklistId ,"
//        + " bbc.corporateStatementChecklistVisitTypeMapperId as corporateStatementChecklistVisitTypeMapperId "
		+ " FROM CorporateStatementChecklistVisitTypeMapper bbc "
		+ " WHERE bbc.organizationId=:orgId "
		+ " AND bbc.corporateStatementChecklistId=:corporateStatementChecklistId "
		+ " AND bbc.status='A' ")	

@Entity
@Table(name="t_corporate_statement_checklist_visit_type_mapper",schema="adt")
@SequenceGenerator(name="corporate_statement_checklist_visit_type_mapper_seq",sequenceName="adt.corporate_statement_checklist_visit_type_mapper_seq",allocationSize=1)
public class CorporateStatementChecklistVisitTypeMapper {

	@Id
	@Column(name="corporate_statement_checklist_visit_type_mapper_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="corporate_statement_checklist_visit_type_mapper_seq")
	private Integer corporateStatementChecklistVisitTypeMapperId;
	
	@Column(name="corporate_statement_checklist_id")
	private Integer corporateStatementChecklistId;

	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_bed_category_id", insertable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;

	public Integer getCorporateStatementChecklistVisitTypeMapperId() {
		return corporateStatementChecklistVisitTypeMapperId;
	}

	public void setCorporateStatementChecklistVisitTypeMapperId(Integer corporateStatementChecklistVisitTypeMapperId) {
		this.corporateStatementChecklistVisitTypeMapperId = corporateStatementChecklistVisitTypeMapperId;
	}

	public Integer getCorporateStatementChecklistId() {
		return corporateStatementChecklistId;
	}

	public void setCorporateStatementChecklistId(Integer corporateStatementChecklistId) {
		this.corporateStatementChecklistId = corporateStatementChecklistId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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


