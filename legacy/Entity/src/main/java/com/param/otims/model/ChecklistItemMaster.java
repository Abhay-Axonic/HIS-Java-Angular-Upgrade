package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLISTITEM_MASTER",
		query="SELECT mstr.checklistItemMasterId as checklistItemMasterId "
					+ " FROM ChecklistItemMaster mstr"
					+ " WHERE  mstr.organizationId= :organizationId"
					+ " AND mstr.unitId= :unitId"
				//	+ " AND (lower(mstr.code)= :code "
					+ " AND lower(mstr.description)= :description)"
					),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLISTITEM_MASTER_BY_NAME_NOT_ID",  //Dilip
	query="SELECT mstr.checklistItemMasterId as checklistItemMasterId "
				+ " FROM ChecklistItemMaster mstr"
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId AND checklist_item_master_id != :checklistItemMasterId"
				+ " AND lower(mstr.description)= :description)"
				),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLISTITEM_MASTER_BY_CODE_NOT_ID",  
	query="SELECT mstr.checklistItemMasterId as checklistItemMasterId "
				+ " FROM ChecklistItemMaster mstr"
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId AND checklist_item_master_id != :checklistItemMasterId"
				+ " AND (lower(mstr.code)= :code )"),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_ITEM_CODE",
	query="SELECT mstr.checklistItemMasterId as checklistItemMasterId "
				+ " FROM ChecklistItemMaster mstr"
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				+ " AND (lower(mstr.code)= :code)"
				),
	
	@NamedQuery(name="GET_CHECKLISTITEM_MASTER",
	query="SELECT mstr.checklistItemMasterId as checklistItemMasterId , " 
				+ " mstr.code as code , "
				+ " mstr.description as description, "
				+ " mstr.status as status "
				+ " FROM ChecklistItemMaster mstr "
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				),
	
	@NamedQuery(name="SEARCH_CHECKLISTITEM_MASTER_LIST",
	query="SELECT mstr.checklistItemMasterId as checklistItemMasterId , " 
				+ " mstr.code || ' | ' || mstr.description as description  "
				+ " FROM ChecklistItemMaster mstr "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.status= 'A' "
				+ " AND (lower(mstr.code) like :description OR lower(mstr.description) like :description )"
			),
})
@Entity
@Table(name = "m_checklist_item_master", schema = "otims")
@SequenceGenerator(name = "checklist_item_master_seq", sequenceName = "otims.checklist_item_master_seq", allocationSize = 1)
public class ChecklistItemMaster {

	
	@Id
	@Column(name = "checklist_item_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_item_master_seq")
	private Integer checklistItemMasterId;

	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

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

	public Integer getChecklistItemMasterId() {
		return checklistItemMasterId;
	}

	public void setChecklistItemMasterId(Integer checklistItemMasterId) {
		this.checklistItemMasterId = checklistItemMasterId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
