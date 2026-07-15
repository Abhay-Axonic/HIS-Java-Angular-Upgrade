package com.param.global.dto;

import java.util.List;

import javax.persistence.Column;

import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDetailsDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;

public class NewEMRTemplateDto {
	private Integer covTemplateId;

	private String templateName;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;
	
	private String updatedDate;
	private String ceratedDate;

	private Integer unitId;

	private Integer organizationId;
	
	private Character status;
	private Integer patientId;
	private Integer roleId;

	private Integer admissionId;
	private Integer encounterId;
	private Integer doctorId;
	
	private Integer typeId;
	
	private Integer parentTypeId;
	
	private List<PatientHabitDetailsDto> patientHabitList;


	private List<ObservationDto> observationList;
	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
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

	
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public List<PatientHabitDetailsDto> getPatientHabitList() {
		return patientHabitList;
	}

	public void setPatientHabitList(List<PatientHabitDetailsDto> patientHabitList) {
		this.patientHabitList = patientHabitList;
	}

	public List<ObservationDto> getObservationList() {
		return observationList;
	}

	public void setObservationList(List<ObservationDto> observationList) {
		this.observationList = observationList;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getCeratedDate() {
		return ceratedDate;
	}

	public void setCeratedDate(String ceratedDate) {
		this.ceratedDate = ceratedDate;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(Integer parentTypeId) {
		this.parentTypeId = parentTypeId;
	}
	
	
}
