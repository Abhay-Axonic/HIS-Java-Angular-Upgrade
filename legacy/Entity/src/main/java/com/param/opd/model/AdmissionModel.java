package com.param.opd.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.patient.PatientRegistration;
import com.param.global.model.DoctorMaster;
import com.param.opd.coversheet.model.EncounterMaster;

/*
 * 
 * Created by TUSHAR
 */
@Entity
@Table(name="t_admission",schema="opd")
@SequenceGenerator(name = "t_admission_opd_seq", sequenceName = "opd.t_admission_opd_seq", allocationSize = 1)
public class AdmissionModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_admission_opd_seq")
	@Column(name = "admission_id", unique = true, nullable = false)
	private int admission_id;
	
	@Column(name = "admission_type_id", nullable = false)
	private Integer admission_type_id;
	
	@Column(name = "bed_category_id", nullable = false)
	private Integer bed_category_id;
	
	@Column(name = "reason_for_admission_id", nullable = false)
	private Integer reason_for_admission_id;
	
	@Column(name = "admission_date", nullable = false)
	private Date admission_date;
	
	@Column(name = "plan_discharge_date", nullable = false)
	private Date plan_discharge_date;
	
	@Column(name = "is_flexible_status", nullable = false)
	private boolean is_flexible_status;
	
	@Column(name = "patient_id", nullable = false)
	private Integer patient_id;
	
	@Column(name = "created_date", nullable = false)
	private Date created_date;
	
	@Column(name = "created_by", nullable = false)
	private Integer created_by;
	
	@Column(name = "updated_date", nullable = false)
	private Date updated_date;
	
	@Column(name = "updated_by", nullable = false)
	private Integer updated_by;
	
	@Column(name = "encounter_id", nullable = false)
	private Integer encounter_id;
	
	@Column(name = "unit_id", nullable = false)
	private Integer unit_id;
	
	@Column(name = "organisation_id", nullable = false)
	private Integer organisation_id;
	  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	  
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private DoctorMaster createdByDoctor;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by", insertable = false, nullable = false, updatable = false)
	private DoctorMaster updatedByDoctor;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounterMaster;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reason_for_admission_id", insertable = false, nullable = false, updatable = false)
	private ReasonForAdmissionMaster reasonForAdmissionMaster;

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

	public Date getAdmission_date() {
		return admission_date;
	}

	public void setAdmission_date(Date admission_date) {
		this.admission_date = admission_date;
	}

	public Date getPlan_discharge_date() {
		return plan_discharge_date;
	}

	public void setPlan_discharge_date(Date plan_discharge_date) {
		this.plan_discharge_date = plan_discharge_date;
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

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public DoctorMaster getCreatedByDoctor() {
		return createdByDoctor;
	}

	public void setCreatedByDoctor(DoctorMaster createdByDoctor) {
		this.createdByDoctor = createdByDoctor;
	}

	public DoctorMaster getUpdatedByDoctor() {
		return updatedByDoctor;
	}

	public void setUpdatedByDoctor(DoctorMaster updatedByDoctor) {
		this.updatedByDoctor = updatedByDoctor;
	}

	public EncounterMaster getEncounterMaster() {
		return encounterMaster;
	}

	public void setEncounterMaster(EncounterMaster encounterMaster) {
		this.encounterMaster = encounterMaster;
	}

	public ReasonForAdmissionMaster getReasonForAdmissionMaster() {
		return reasonForAdmissionMaster;
	}

	public void setReasonForAdmissionMaster(
			ReasonForAdmissionMaster reasonForAdmissionMaster) {
		this.reasonForAdmissionMaster = reasonForAdmissionMaster;
	}
}
