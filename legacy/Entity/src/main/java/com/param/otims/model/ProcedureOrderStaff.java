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
	
	@NamedQuery(name="GET_PROCEDURE_ORDER_STAFF_DETAILS_BY_PROCEDURE_ORDER_ID",
	query="SELECT mstr.procedureOrderStaffId as procedureOrderStaffId , " 
				+ " mstr.procedureRequestId as procedureRequestId , "
				+ " mstr.procedureOrderId as procedureOrderId , "
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
				+ " FROM ProcedureOrderStaff mstr "
				+ " INNER JOIN  mstr.roleMaster otimsRoleMaster  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.status = 'A' "
				+ " AND mstr.procedureOrderId = :procedureOrderId "
				)
})

@Entity	
@Table(name = "t_procedure_order_staff", schema = "otims")
@SequenceGenerator(name = "procedure_order_staff_seq", sequenceName = "otims.procedure_order_staff_seq", allocationSize = 1)
public class ProcedureOrderStaff {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_order_staff_seq")
	@Column(name = "procedure_order_staff_id")
	private Integer procedureOrderStaffId;
	
	@Column(name = "procedure_request_id")
	private Integer procedureRequestId;
	
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "no_of_staff")
	private Integer noOfStaff;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId ;
	
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

	public Integer getProcedureOrderStaffId() {
		return procedureOrderStaffId;
	}

	public void setProcedureOrderStaffId(Integer procedureOrderStaffId) {
		this.procedureOrderStaffId = procedureOrderStaffId;
	}

	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
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

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
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
