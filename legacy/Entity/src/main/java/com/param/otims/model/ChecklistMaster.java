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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.OtimsRoleMaster;
import com.param.adt.master.global.model.RoleMaster;

@NamedQueries({
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_MASTER",
		query="SELECT mstr.checklistMasterId as checklistMasterId "
					+ " FROM ChecklistMaster mstr"
					+ " WHERE  mstr.organizationId= :organizationId"
					+ " AND mstr.unitId= :unitId"
					+ " AND lower(mstr.code)= :code "
					//+ " AND lower(mstr.description)= :description " //dilip
					),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_MASTER_BY_NOT_BY_ID", //dilip
	query="SELECT mstr.checklistMasterId as checklistMasterId "
				+ " FROM ChecklistMaster mstr"
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				+ " AND lower(mstr.code)= :code "
				+ " AND checklist_master_id != :checklistMasterId" 
				),
	
	@NamedQuery(name="GET_CHECKLIST_MASTER_LIST",
	query="SELECT mstr.checklistMasterId as checklistMasterId , " 
				+ " mstr.code as code , "
				+ " mstr.roleId as roleId , "
				+ " mstr.stageId as stageId , "
				+ " mstr.isWho as isWho , "
				+ " mstr.description as description, "
				+ " otimsRoleMaster.roleDesc as roleName, "
				+ " mstr.status as status "
				+ " FROM ChecklistMaster mstr "
				+ " INNER JOIN  mstr.roleMaster otimsRoleMaster  "
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_CHECKLIST_MASTER_LIST_BY_STAGE_ID",
	query="SELECT mstr.checklistMasterId as checklistMasterId , " 
				+ " mstr.code as code , "
				+ " mstr.roleId as roleId , "
				+ " mstr.stageId as stageId , "
				+ " mstr.isWho as isWho , "
				+ " mstr.description as description, "
				+ " otimsRoleMaster.roleDesc as roleName, "
				+ " mstr.status as status "
				+ " FROM ChecklistMaster mstr "
				+ " INNER JOIN  mstr.roleMaster otimsRoleMaster  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.stageId= :stageId "
				+ " AND mstr.status= 'A'"
				+ " ORDER BY mstr.description ASC"
				),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_MASTER_DESCRIPTION",
	query="SELECT mstr.checklistMasterId as checklistMasterId "
				+ " FROM ChecklistMaster mstr"
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				+ " AND lower(mstr.description)= :description " 
				),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CHECKLIST_MASTER_DESCRIPTION_BY_NOT_BY_ID", 
	query="SELECT mstr.checklistMasterId as checklistMasterId "
				+ " FROM ChecklistMaster mstr"
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				+ " AND lower(mstr.description)= :description"
				+ " AND checklist_master_id != :checklistMasterId" 
				),
})

@Entity
@Table(name = "m_checklist_master", schema = "otims")
@SequenceGenerator(name = "checklist_master_seq", sequenceName = "otims.checklist_master_seq", allocationSize = 1)
public class ChecklistMaster {

	@Id
	@Column(name = "checklist_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_master_seq")
	private Integer checklistMasterId ;

	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "stage_id")
	private Integer stageId;
	
	@Column(name = "is_who")
	private boolean isWho;
	
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
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private OtimsRoleMaster otimsRoleMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private RoleMaster roleMaster;


	public Integer getChecklistMasterId() {
		return checklistMasterId;
	}

	public void setChecklistMasterId(Integer checklistMasterId) {
		this.checklistMasterId = checklistMasterId;
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public boolean getIsWho() {
		return isWho;
	}

	public void setIsWho(boolean isWho) {
		this.isWho = isWho;
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

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

}
