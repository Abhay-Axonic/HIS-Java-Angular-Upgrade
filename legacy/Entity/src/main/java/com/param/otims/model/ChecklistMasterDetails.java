package com.param.otims.model;

import java.util.Date;

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

@NamedQueries({
	@NamedQuery(name="GET_CHECKLIST_MASTER_DETAILS_LIST_BY_MASTER_ID",
	query="SELECT mstr.checklistMasterDetailsId as checklistMasterDetailsId , " 
				+ " mstr.createdBy as createdBy , "
				+ " mstr.checklistItemMasterId as checklistItemMasterId , "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY HH:mm:ss') as createdDate , "
				+ " to_char(mstr.updatedDate,'DD-MM-YYYY HH:mm:ss') as updatedDate , "
				+ " mstr.createdBy as createdBy , "
				+ " mstr.updatedBy as updatedBy , "
				+ " mstr.organizationId as organizationId , "
				+ " mstr.unitId as unitId , "
				+ " mstr.status as status , "
				+ " checklistItemMaster.description as description  "
				+ " FROM ChecklistMasterDetails mstr "
				+ " INNER JOIN  mstr.checklistItemMaster checklistItemMaster  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.checklistMasterId= :checklistMasterId "
				+ " AND mstr.status= 'A' "
				),
	
	
})

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_CHECKLIST_MASTER_DETAILS_LIST_BY_CHECK_LIST_MASTER_ID",
			query="SELECT chedet.checklist_master_details_id as \"checklistMasterDetailsId\" , " 
					+ " chedet.created_by as \"createdBy\" , "
					+ " to_char(chedet.created_date,'DD-MM-YYYY HH:mm:ss') as \"createdDate\" , "
					+ " to_char(chedet.updated_date,'DD-MM-YYYY HH:mm:ss') as \"updatedDate\" , "
					+ " chedet.updated_by as \"updatedBy\" , "
					+ " chedet.organization_id as \"organizationId\" , "
					+ " chedet.unit_id as \"unitId\" , "
					+ " chedet.status as \"status\" , "
					+ " chedet.checklist_master_id AS \"checklistId\", "
					+ " chedet.checklist_item_master_id AS \"checklistItemMasterId\", "
					+ " cheitmstr.description as \"description\"  "
					+ " FROM otims.m_checklist_master chmstr  "
					+ " inner join otims.m_checklist_master_details chedet on chmstr.checklist_master_id =  chedet.checklist_master_id "
					+ " inner join otims.m_checklist_item_master cheitmstr on  " + 
					"	chedet.checklist_item_master_id = cheitmstr.checklist_item_master_id"
					+ " WHERE  chedet.organization_id= :organizationId "
					+ " AND chedet.unit_id= :unitId "
					+ " AND chedet.checklist_master_id= :checklistMasterId "
					+ " AND chedet.status= 'A' "
					//+ " AND chmstr.role_id in (8,9,10,11,12) "
					),
	
	@NamedNativeQuery(name = "GET_SAVED_CHECKLIST_ITEM_LIST",
	query=" 	select distinct" + 
			"	otcheck.ot_checklist_details_id as \"otChecklistDetailsId\", " + 
			"	otcheck.checklist_id as \"checklistId\", " + 
			"	otcheck.checklist_status_id as \"checklistStatusId\", " + 
			"	otcheck.checklist_master_id as \"checklistItemMasterId\", " + 
			"	otcheck.ot_checklist_name as \"description\", " + 
			"	otcheck.remark  as \"remark\","
		    + " otcheck.stage_id  as \"stageId\"" + 
			"	from otims.t_ot_checklist_details otcheck " + 
			"	inner join  otims.m_checklist_master mstr on otcheck.checklist_id = mstr.checklist_master_id "
			+ " inner join otims.m_checklist_master_details ckDtls on  otcheck.checklist_master_id = ckDtls.checklist_item_master_id " 
			+ " WHERE  otcheck.pro_req_id= :proReqId "
			+ " AND otcheck.checklist_id= :checklistMasterId "
			+ " AND otcheck.status= 'A'"
			+ " AND otcheck.stage_id= :stageId "
			//+ " AND mstr.role_id in (8,9,10,11,12) "
			+ " AND ckDtls.status='A'"
			),
})

@Entity
@Table(name = "m_checklist_master_details", schema = "otims")
@SequenceGenerator(name = "checklist_master_details_seq", sequenceName = "otims.checklist_master_details_seq", allocationSize = 1)
public class ChecklistMasterDetails {

	@Id
	@Column(name = "checklist_master_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_master_details_seq")
	private Integer checklistMasterDetailsId;

	@Column(name = "checklist_master_id")
	private Integer checklistMasterId;

	@Column(name = "checklist_item_master_id")
	private Integer checklistItemMasterId;

	@Column(name = "status")
	private Character status;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checklist_item_master_id", insertable = false, updatable = false)
	private ChecklistItemMaster checklistItemMaster;

	public Integer getChecklistMasterDetailsId() {
		return checklistMasterDetailsId;
	}

	public void setChecklistMasterDetailsId(Integer checklistMasterDetailsId) {
		this.checklistMasterDetailsId = checklistMasterDetailsId;
	}

	public Integer getChecklistMasterId() {
		return checklistMasterId;
	}

	public void setChecklistMasterId(Integer checklistMasterId) {
		this.checklistMasterId = checklistMasterId;
	}

	public Integer getChecklistItemMasterId() {
		return checklistItemMasterId;
	}

	public void setChecklistItemMasterId(Integer checklistItemMasterId) {
		this.checklistItemMasterId = checklistItemMasterId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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
}
