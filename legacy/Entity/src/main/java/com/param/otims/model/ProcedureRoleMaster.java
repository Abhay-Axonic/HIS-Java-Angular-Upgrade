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
	@NamedQuery(name="GET_PROCEDURE_ROLE_MASTER_BY_ID",
			query=" SELECT "
					+ " procedureRoleMaster.procedureRoleId AS procedureRoleId, procedureRoleMaster.procedureRoleCode AS procedureRoleCode, "
					+ " procedureRoleMaster.procedureRoleName AS procedureRoleName, procedureRoleMaster.procedureRoleDesc AS procedureRoleDesc, procedureRoleMaster.status AS status, "
					+ " procedureRoleMaster.orgId AS orgId, procedureRoleMaster.unitId AS unitId"
					+ " FROM ProcedureRoleMaster procedureRoleMaster WHERE procedureRoleMaster.orgId= :orgId AND procedureRoleMaster.unitId= :unitId "
					+ " AND procedureRoleMaster.procedureRoleId= :procedureRoleId ORDER BY procedureRoleMaster.procedureRoleId asc"
					),
	
	@NamedQuery(name="GET_PROCEDURE_ROLE_MASTER_LIST",
			query=" SELECT "
					+ " procedureRoleMaster.procedureRoleId AS procedureRoleId, procedureRoleMaster.procedureRoleCode AS procedureRoleCode, "
					+ " procedureRoleMaster.procedureRoleName AS procedureRoleName, procedureRoleMaster.procedureRoleDesc AS procedureRoleDesc, procedureRoleMaster.status AS status, "
					+ " procedureRoleMaster.orgId AS orgId, procedureRoleMaster.unitId AS unitId"
					+ " FROM ProcedureRoleMaster procedureRoleMaster WHERE procedureRoleMaster.orgId= :orgId AND procedureRoleMaster.unitId= :unitId "
					+ " ORDER BY procedureRoleMaster.procedureRoleId asc"
					),
	
	@NamedQuery(name="GET_ACTIVE_PROCEDURE_ROLE_MASTER_LIST",
			query=" SELECT "
					+ " procedureRoleMaster.procedureRoleId AS procedureRoleId, procedureRoleMaster.procedureRoleCode AS procedureRoleCode, "
					+ " procedureRoleMaster.procedureRoleName AS procedureRoleName, procedureRoleMaster.procedureRoleDesc AS procedureRoleDesc, procedureRoleMaster.status AS status, "
					+ " procedureRoleMaster.orgId AS orgId, procedureRoleMaster.unitId AS unitId"
					+ " FROM ProcedureRoleMaster procedureRoleMaster WHERE procedureRoleMaster.orgId= :orgId AND procedureRoleMaster.unitId= :unitId "
					+ " AND procedureRoleMaster.status = 'A' ORDER BY procedureRoleMaster.procedureRoleId asc"
					),
	
	
	@NamedQuery(name="GET_PROCEDURE_ROLE_MASTER_BY_NAME",
			query=" SELECT "
					+ " procedureRoleMaster.procedureRoleId AS procedureRoleId, procedureRoleMaster.procedureRoleName AS procedureRoleName"
					+ " FROM ProcedureRoleMaster procedureRoleMaster WHERE LOWER(procedureRoleMaster.procedureRoleName)= :procedureRoleName  "
					),
	
	@NamedQuery(name="GET_PROCEDURE_ROLE_MASTER_BY_NAME_NOT_ID",
			query=" SELECT "
					+ " procedureRoleMaster.procedureRoleId AS procedureRoleId, procedureRoleMaster.procedureRoleName AS procedureRoleName"
					+ " FROM ProcedureRoleMaster procedureRoleMaster WHERE procedureRoleMaster.procedureRoleId != :procedureRoleId "
					+ " AND LOWER(procedureRoleMaster.procedureRoleName)= :procedureRoleName "
					)
})



@Entity
@Table(name = "m_procedure_role_master", schema = "otims")
@SequenceGenerator(name = "m_procedure_role_master_seq", sequenceName = "otims.m_procedure_role_master_seq", allocationSize = 1)
public class ProcedureRoleMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_procedure_role_master_seq")
	@Column(name = "procedure_role_id")
	private Integer procedureRoleId;
	
	@Column(name = "procedure_role_code")
	private String procedureRoleCode;
	
	@Column(name = "procedure_role_name")
	private String procedureRoleName;
	
	@Column(name = "procedure_role_desc")
	private String procedureRoleDesc;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
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

	public Integer getProcedureRoleId() {
		return procedureRoleId;
	}

	public void setProcedureRoleId(Integer procedureRoleId) {
		this.procedureRoleId = procedureRoleId;
	}

	public String getProcedureRoleCode() {
		return procedureRoleCode;
	}

	public void setProcedureRoleCode(String procedureRoleCode) {
		this.procedureRoleCode = procedureRoleCode;
	}

	public String getProcedureRoleName() {
		return procedureRoleName;
	}

	public void setProcedureRoleName(String procedureRoleName) {
		this.procedureRoleName = procedureRoleName;
	}

	

	public String getProcedureRoleDesc() {
		return procedureRoleDesc;
	}

	public void setProcedureRoleDesc(String procedureRoleDesc) {
		this.procedureRoleDesc = procedureRoleDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
