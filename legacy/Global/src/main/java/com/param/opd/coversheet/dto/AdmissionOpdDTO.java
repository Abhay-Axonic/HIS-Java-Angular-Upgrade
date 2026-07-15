package com.param.opd.coversheet.dto;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.In;

/*
 * created by Tushar
 */
public class AdmissionOpdDTO {
	
	private int admission_id;
	private Integer admission_type_id;
	private Integer bed_category_id;
	private Integer reason_for_admission_id;
	private String admission_date;
	private String plan_discharge_date;
	private String instruction_post_admission;
	private boolean is_flexible_status;
	private Integer patient_id;
	private Date created_date;
	private Integer created_by;
	private Date updated_date;
	private Integer updated_by;
	private Integer encounter_id;
	private Integer unit_id;
	private Integer organisation_id;
	private Integer instruction_post_admission_id;
	private Integer admissionId;
	
	
	public int getAdmission_id() {
		return admission_id;
	}
	public void setAdmission_id(int admission_id) {
		this.admission_id = admission_id;
	}
	public Integer getAdmission_type_id() {
		return admission_type_id;
	}
	public void setAdmission_type_id(Integer admission_type_id) {
		this.admission_type_id = admission_type_id;
	}
	public Integer getBed_category_id() {
		return bed_category_id;
	}
	public void setBed_category_id(Integer bed_category_id) {
		this.bed_category_id = bed_category_id;
	}
	public Integer getReason_for_admission_id() {
		return reason_for_admission_id;
	}
	public void setReason_for_admission_id(Integer reason_for_admission_id) {
		this.reason_for_admission_id = reason_for_admission_id;
	}
	public String getAdmission_date() {
		return admission_date;
	}
	public void setAdmission_date(String admission_date) {
		this.admission_date = admission_date;
	}
	public String getPlan_discharge_date() {
		return plan_discharge_date;
	}
	public void setPlan_discharge_date(String plan_discharge_date) {
		this.plan_discharge_date = plan_discharge_date;
	}
	public String getInstruction_post_admission() {
		return instruction_post_admission;
	}
	public void setInstruction_post_admission(String instruction_post_admission) {
		this.instruction_post_admission = instruction_post_admission;
	}
	public boolean isIs_flexible_status() {
		return is_flexible_status;
	}
	public void setIs_flexible_status(boolean is_flexible_status) {
		this.is_flexible_status = is_flexible_status;
	}
	public Integer getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(Integer patient_id) {
		this.patient_id = patient_id;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public Integer getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
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
	public Integer getInstruction_post_admission_id() {
		return instruction_post_admission_id;
	}
	public void setInstruction_post_admission_id(
			Integer instruction_post_admission_id) {
		this.instruction_post_admission_id = instruction_post_admission_id;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

}
