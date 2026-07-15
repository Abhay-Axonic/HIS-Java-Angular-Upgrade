package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;

public class EmployeeVaccinationDto {

	private Integer employeeVaccinationId;

	private Integer roleId;

	private Integer userId;

	private Character status;

	private Integer organisationId;

	private Integer unitId;

	private int createdBy;

	private Date createdDate;

	private int updatedBy;

	private Date updatedDate;

	private List<VaccinationDto> vaccinationDto;

	private Integer vaccinationId;

	private String vaccinationName;

	private Date selectionDate;
	
	
   
	

	public Date getSelectionDate() {
		return selectionDate;
	}

	public void setSelectionDate(Date selectionDate) {
		this.selectionDate = selectionDate;
	}

	public Integer getVaccinationId() {
		return vaccinationId;
	}

	public void setVaccinationId(Integer vaccinationId) {
		this.vaccinationId = vaccinationId;
	}

	public List<VaccinationDto> getVaccinationDto() {
		return vaccinationDto;
	}

	public void setVaccinationDto(List<VaccinationDto> vaccinationDto) {
		this.vaccinationDto = vaccinationDto;
	}

	public Integer getEmployeeVaccinationId() {
		return employeeVaccinationId;
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

	public String getVaccinationName() {
		return vaccinationName;
	}

	public void setVaccinationName(String vaccinationName) {
		this.vaccinationName = vaccinationName;
	}

	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
