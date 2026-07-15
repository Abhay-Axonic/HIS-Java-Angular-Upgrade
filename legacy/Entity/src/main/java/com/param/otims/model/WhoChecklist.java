package com.param.otims.model;

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

	  @NamedNativeQuery(name="GET_WHO_CHECKLIST_BY_PRE_OPERATIVE_ID",
			query=" SELECT checklist.who_checklist_id AS \"whoChecklistId\", " 
					+" checklist.pre_operative_id AS \"preOperativeId\" , checklist.checklist_master_id AS \"checklistMasterId\", " 
					+" checklist.who_status_id AS \"whoStatusId\", " 
					+" checklist.remark AS \"remark\","
					+" checkmstr.description AS \"description\","
					+" checkmstr.is_who AS \"isWho\", " 
					+" checklist.created_by AS \"createdBy\" , checklist.status AS \"status\"," 
					+" to_char(checklist.created_date,'YYYY-MM-DD') as \"createdDate\", "
					+" to_char(checklist.updated_date,'YYYY-MM-DD') as \"updatedDate\", "
					+" checklist.updated_by AS \"updatedBy\", " 
					+" checklist.unit_id AS \"unitId\", checklist.organization_id AS \"organizationId\" " 
					+" FROM otims.t_who_checklist checklist " 
					+" INNER JOIN  otims.m_checklist_master checkmstr on checkmstr.checklist_master_id = checklist.checklist_master_id "
					+" WHERE checklist.pre_operative_id = :preOperativeId " ),
	  
	  @NamedNativeQuery(name="GET_WHO_CHECKLIST_BY_INTRA_OPERATIVE_ID",
		query=" SELECT checklist.who_checklist_id AS \"whoChecklistId\", " 
				+" checklist.intra_operative AS \"intraOperative\" , checklist.checklist_master_id AS \"checklistMasterId\", " 
				+" checklist.who_status_id AS \"whoStatusId\", " 
				+" checklist.remark AS \"remark\","
				+" checkmstr.description AS \"description\","
				+" checkmstr.is_who AS \"isWho\", " 
				+" checklist.created_by AS \"createdBy\" , checklist.status AS \"status\"," 
				+" to_char(checklist.created_date,'YYYY-MM-DD') as \"createdDate\", "
				+" to_char(checklist.updated_date,'YYYY-MM-DD') as \"updatedDate\", "
				+" checklist.updated_by AS \"updatedBy\", " 
				+" checklist.unit_id AS \"unitId\", checklist.organization_id AS \"organizationId\" " 
				+" FROM otims.t_who_checklist checklist " 
				+" INNER JOIN  otims.m_checklist_master checkmstr on checkmstr.checklist_master_id = checklist.checklist_master_id "
				+" WHERE checklist.intra_operative = :intraOperativeId " )
})

@Entity
@Table(name = "t_who_checklist" , schema = "otims")
@SequenceGenerator(name = "who_checklist_seq" , sequenceName = "otims.who_checklist_seq" , allocationSize = 1)
public class WhoChecklist {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "who_checklist_seq")
	@Column(name = "who_checklist_id")
	private Integer whoChecklistId;
	
	@Column(name = "pre_operative_id")
	private Integer preOperativeId;
	
	@Column(name = "checklist_master_id")
	private Integer checklistMasterId;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "who_status_id")
	private Integer whoStatusId;
	
	@Column(name = "intra_operative")
	private Integer intraOperative;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getWhoChecklistId() {
		return whoChecklistId;
	}

	public void setWhoChecklistId(Integer whoChecklistId) {
		this.whoChecklistId = whoChecklistId;
	}

	public Integer getPreOperativeId() {
		return preOperativeId;
	}

	public void setPreOperativeId(Integer preOperativeId) {
		this.preOperativeId = preOperativeId;
	}

	public Integer getChecklistMasterId() {
		return checklistMasterId;
	}

	public void setChecklistMasterId(Integer checklistMasterId) {
		this.checklistMasterId = checklistMasterId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getWhoStatusId() {
		return whoStatusId;
	}

	public void setWhoStatusId(Integer whoStatusId) {
		this.whoStatusId = whoStatusId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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


	public Integer getIntraOperative() {
		return intraOperative;
	}

	public void setIntraOperative(Integer intraOperative) {
		this.intraOperative = intraOperative;
	}
	
	
	
}
