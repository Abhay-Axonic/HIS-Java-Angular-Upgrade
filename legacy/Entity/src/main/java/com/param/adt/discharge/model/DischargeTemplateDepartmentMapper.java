package com.param.adt.discharge.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_discharge_template_department_mapper", schema = "adt")
@SequenceGenerator(name = "t_discharge_template_department_mapper_seq", sequenceName = "adt.t_discharge_template_department_mapper_seq", allocationSize = 1)
public class DischargeTemplateDepartmentMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_discharge_template_department_mapper_seq")
	@Column(name = "discharge_temp_dept_mppr_id")
	private Integer dischargeTempDeptMpprId;
	
	@Column(name = "discharge_summary_temp_id")
	private Integer dischargeSummaryTempId;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	public Integer getDischargeTempDeptMpprId() {
		return dischargeTempDeptMpprId;
	}

	public void setDischargeTempDeptMpprId(Integer dischargeTempDeptMpprId) {
		this.dischargeTempDeptMpprId = dischargeTempDeptMpprId;
	}

	public Integer getDischargeSummaryTempId() {
		return dischargeSummaryTempId;
	}

	public void setDischargeSummaryTempId(Integer dischargeSummaryTempId) {
		this.dischargeSummaryTempId = dischargeSummaryTempId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
