package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.SequenceGenerator;
import javax.persistence.Table;




@Entity
@Table(name = "t_employee_vaccination", schema = "public")
@SequenceGenerator(name = "t_employee_vaccination_seq", sequenceName = "public.t_employee_vaccination_seq", allocationSize = 1)
public class EmployeeVaccination {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_employee_vaccination_seq")
	@Column(name = "employee_vaccination_id")
	private Integer employeeVaccinationId;

	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "vaccination_id")
	private Integer vaccinationId;

	@Column(name = "selection_date")
	private Date selectionDate;

	@Column(name = "status")
	private Character status;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	
	 /* @OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeVaccination",
	  cascade = CascadeType.ALL, orphanRemoval = true) private
	  List<VaccinationRoleMapper> vaccinationRoleMapper;*/
	
	
	 

	public Integer getEmployeeVaccinationId() {
		return employeeVaccinationId;
	}

	public Date getSelectionDate() {
		return selectionDate;
	}

	public void setSelectionDate(Date selectionDate) {
		this.selectionDate = selectionDate;
	}

	public void setEmployeeVaccinationId(Integer employeeVaccinationId) {
		this.employeeVaccinationId = employeeVaccinationId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	
	public Integer getVaccinationId() {
		return vaccinationId;
	}

	public void setVaccinationId(Integer vaccinationId) {
		this.vaccinationId = vaccinationId;
	}

	

	

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	

}
