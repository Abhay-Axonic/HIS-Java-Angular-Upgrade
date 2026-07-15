package com.param.global.unit.dto;

import java.util.Date;


public class VaccinationDto {
	private Date selectionDate;
	
	private Integer vaccinationId;
	
	private Integer employeeVaccinationId;
	
	
	
	
	public Integer getEmployeeVaccinationId() {
		return employeeVaccinationId;
	}
	public void setEmployeeVaccinationId(Integer employeeVaccinationId) {
		this.employeeVaccinationId = employeeVaccinationId;
	}
	public Integer getVaccinationId() {
		return vaccinationId;
	}
	public void setVaccinationId(Integer vaccinationId) {
		this.vaccinationId = vaccinationId;
	}
	public Date getSelectionDate() {
		return selectionDate;
	}
	public void setSelectionDate(Date selectionDate) {
		this.selectionDate = selectionDate;
	}

}
