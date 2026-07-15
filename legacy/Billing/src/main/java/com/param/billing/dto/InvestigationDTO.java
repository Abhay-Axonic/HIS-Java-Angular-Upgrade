package com.param.billing.dto;

import java.util.Date;

public class InvestigationDTO {
	
	private int investigation_id;
	private int service_type_id;
	private int service_id;
	private String service;
	private String service_description;
	private int patient_id;
	private boolean investigation_status;
	private Date created_date;
	private int created_by;
	private Date updated_date;
	private int updated_by;
	private Integer encounter_id;
	private Integer unit_id;
	private Integer organisation_id;
	private String service_type;
	
	public int getInvestigation_id() {
		return investigation_id;
	}
	public void setInvestigation_id(int investigation_id) {
		this.investigation_id = investigation_id;
	}

	public int getService_id() {
		return service_id;
	}
	public void setService_id(int service_id) {
		this.service_id = service_id;
	}
	public String getService_description() {
		return service_description;
	}
	public void setService_description(String service_description) {
		this.service_description = service_description;
	}
	public int getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}
	public boolean isInvestigation_status() {
		return investigation_status;
	}
	public void setInvestigation_status(boolean investigation_status) {
		this.investigation_status = investigation_status;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public int getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(int updated_by) {
		this.updated_by = updated_by;
	}
	public int getService_type_id() {
		return service_type_id;
	}
	public void setService_type_id(int service_type_id) {
		this.service_type_id = service_type_id;
	}
	public Integer getEncounter_id() {
		return encounter_id;
	}
	public void setEncounter_id(Integer encounter_id) {
		this.encounter_id = encounter_id;
	}
	public Integer getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}
	public Integer getOrganisation_id() {
		return organisation_id;
	}
	public void setOrganisation_id(Integer organisation_id) {
		this.organisation_id = organisation_id;
	}
	public String getService_type() {
		return service_type;
	}
	public void setService_type(String service_type) {
		this.service_type = service_type;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}

}
