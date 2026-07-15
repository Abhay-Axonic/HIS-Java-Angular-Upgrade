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
	
	@NamedQuery(name="GET_STAFF_LIST_BY_PROCEDURE_CONFIG_ID",
	query="SELECT mstr.procedureConfigStaffId as procedureConfigStaffId , " 
				+ " mstr.procedureConfigId as procedureConfigId , "
				+ " mstr.noOfStaff as staffNumber, "
				+ " mstr.roleId as roleId, "
				+ " otimsRoleMaster.roleDesc as roleName, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " mstr.status as status "
				+ " FROM ProcedureConfigurationStaff mstr "
				+ " INNER JOIN  mstr.roleMaster otimsRoleMaster  "
				+ " INNER JOIN  mstr.procedureConfiguration procedureConfiguration  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				//+ " AND mstr.procedureConfigId= :procedureConfigId "
				+ " AND mstr.status = 'A' "
				)
})

@Entity
@Table(name = "t_procedure_config_staff" , schema = "otims")
@SequenceGenerator(name = "procedure_config_staff_seq" , sequenceName = "otims.procedure_config_staff_seq" , allocationSize = 1)
public class ProcedureConfigurationStaff {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_config_staff_seq")
	@Column(name = "procedure_config_staff_id")
	private Integer procedureConfigStaffId;
	
	@Column(name = "procedure_config_id")
	private Integer procedureConfigId;
	
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "no_of_staff")
	private Integer noOfStaff ;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private OtimsRoleMaster otimsRoleMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private RoleMaster roleMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "procedure_config_id", insertable = false, updatable = false)
	private ProcedureConfiguration procedureConfiguration;

	public Integer getProcedureConfigStaffId() {
		return procedureConfigStaffId;
	}

	public void setProcedureConfigStaffId(Integer procedureConfigStaffId) {
		this.procedureConfigStaffId = procedureConfigStaffId;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getNoOfStaff() {
		return noOfStaff;
	}

	public void setNoOfStaff(Integer noOfStaff) {
		this.noOfStaff = noOfStaff;
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

	
}
