package com.param.CSSD.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AutoclaveLogDto {

	private Integer id;
	private Integer autoclaveNumber;
	private String bioIndiDate;
	private String control;
	private String test;
	private Integer created_by;
	private Integer updated_by;
	private String created_date;
	private String updated_date;
	private Integer organizationId;
	private Integer unitId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAutoclaveNumber() {
		return autoclaveNumber;
	}
	public void setAutoclaveNumber(Integer autoclaveNumber) {
		this.autoclaveNumber = autoclaveNumber;
	}
	public String getControl() {
		return control;
	}
	public void setControl(String control) {
		this.control = control;
	}
	public String getBioIndiDate() {
		return bioIndiDate;
	}
	public void setBioIndiDate(String bioIndiDate) {
		this.bioIndiDate = bioIndiDate;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public Integer getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
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
	
	

	
	
}
