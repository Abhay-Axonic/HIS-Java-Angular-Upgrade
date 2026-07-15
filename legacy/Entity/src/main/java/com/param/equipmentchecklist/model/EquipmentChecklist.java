package com.param.equipmentchecklist.model;

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
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MASTER",
		query="SELECT mstr.checklistEquipmentMasterId as checklistEquipmentMasterId "
					+ " FROM EquipmentChecklist mstr"
					+ " WHERE  mstr.organizationId= :organizationId"
					+ " AND mstr.unitId= :unitId"
					+ " AND lower(mstr.checklistCode)= :checklistCode"
					+ " AND lower(mstr.checklistName)= :checklistName"
					),
	//added by minesh for update EquipmentChecklist.
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_EQUIPMENT_MASTER_NOT_ID",
	query="SELECT mstr.checklistEquipmentMasterId as checklistEquipmentMasterId "
				+ " FROM EquipmentChecklist mstr"
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				+ " AND mstr.checklistEquipmentMasterId != :checklistEquipmentMasterId"
				+ " AND lower(mstr.checklistCode)= :checklistCode"
				+ " AND lower(mstr.checklistName)= :checklistName"
				),//end update EquipmentChecklist
	
	@NamedQuery(name="GET_CHECKLIST_EQUIPMENT_MASTER_LIST",
	query="SELECT mstr.checklistEquipmentMasterId as checklistEquipmentMasterId , " 
				+ " mstr.checklistName as checklistName , "
				+ " mstr.checklistCode as checklistCode, "
				+ " mstr.status as status "
				+ " FROM EquipmentChecklist mstr "
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				),/*
	
	@NamedQuery(name="GET_CHECKLIST_MASTER_LIST_BY_STAGE_ID",
	query="SELECT mstr.checklistMasterId as checklistMasterId , " 
				+ " mstr.code as code , "
				+ " mstr.roleId as roleId , "
				+ " mstr.stageId as stageId , "
				+ " mstr.isWho as isWho , "
				+ " mstr.description as description, "
				+ " otimsRoleMaster.roleName as roleName, "
				+ " mstr.status as status "
				+ " FROM ChecklistMaster mstr "
				+ " INNER JOIN  mstr.otimsRoleMaster otimsRoleMaster  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.stageId= :stageId "
				+ " AND mstr.status= 'A'"
				+ " OREDR BY mstr.description ASC"
				)*/
})

@Entity
@Table(name = "m_checklist_equipment_master", schema = "equipment")
@SequenceGenerator(name = "checklist_equipment_master_seq", sequenceName = "equipment.checklist_equipment_master_seq", allocationSize = 1)
public class EquipmentChecklist {

	@Id
	@Column(name = "checklist_equipment_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_equipment_master_seq")
	private Integer checklistEquipmentMasterId ;

	@Column(name = "checklist_code")
	private String checklistCode;

	@Column(name = "checklist_name")
	private String checklistName;

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
	

	public Integer getChecklistEquipmentMasterId() {
		return checklistEquipmentMasterId;
	}

	public void setChecklistEquipmentMasterId(Integer checklistEquipmentMasterId) {
		this.checklistEquipmentMasterId = checklistEquipmentMasterId;
	}

	

	public String getChecklistCode() {
		return checklistCode;
	}

	public void setChecklistCode(String checklistCode) {
		this.checklistCode = checklistCode;
	}

	public String getChecklistName() {
		return checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
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
