package com.param.entity.model.opd;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	
	@NamedQuery(name="GET_NURSING_INITIAL_ASSESSMENT_LIST",
			query="SELECT nurse.initialAssessmentId AS initialAssessmentId ,"
					+ " nurse.communicationAndCognitionDesc AS communicationAndCognitionDesc ,"
					+ " nurse.invasiveLinesCatheterTubesDrainsDesc AS invasiveLinesCatheterTubesDrainsDesc ,"
					+ " nurse.psychosocialDesc AS psychosocialDesc ,"
					+ " nurse.painAssessment AS painAssessment ,"
					+ " nurse.restAndSleepDesc AS restAndSleepDesc ,"
					+ " nurse.nutritionAssessmentDesc AS nutritionAssessmentDesc ,"
					+ " nurse.fallRiskAssessmentDesc AS fallRiskAssessmentDesc ,"
					+ " nurse.examinationDesc AS examinationDesc ,"
					+ " nurse.patientId AS patientId ,"
					+ " nurse.encounterId AS encounterId ,"
					+ " nurse.admissionId AS admissionId ,"
					+ " nurse.patientId AS patientId ,"
					+ " to_char(nurse.assessmentStartTime,'hh24:mi')  AS assessmentStartTime ,"
					+ " to_char(nurse.assessmentEndTime,'hh24:mi')  AS assessmentEndTime ,"
					+ " nurse.arrivedBy AS arrivedBy ,"
					+ " nurse.unitId AS unitId ,"
					+ " nurse.organizationId AS organizationId ,"
					+ " nurse.isSubmit AS isSubmit ,"
					+ " nurse.status AS status ,"
					+ "to_char(nurse.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate ,"
					+ "to_char(nurse.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate "
						+ " FROM NursingInitialAssessmentModel nurse"
						+ " WHERE nurse.patientId = :patientId"
						+ " and nurse.organizationId = :organizationId"
						+ " and nurse.unitId = :unitId "
						+ " and nurse.admissionId = :id "
						+ " order by nurse.initialAssessmentId  desc "
						),
	
	@NamedQuery(name="GET_NURSING_INITIAL_ASSESSMENT_LIST_BY_ENCOUNTER",
	query="SELECT nurse.initialAssessmentId AS initialAssessmentId ,"
			+ " nurse.communicationAndCognitionDesc AS communicationAndCognitionDesc ,"
			+ " nurse.invasiveLinesCatheterTubesDrainsDesc AS invasiveLinesCatheterTubesDrainsDesc ,"
			+ " nurse.psychosocialDesc AS psychosocialDesc ,"
			+ " nurse.painAssessment AS painAssessment ,"
			+ " nurse.restAndSleepDesc AS restAndSleepDesc ,"
			+ " nurse.nutritionAssessmentDesc AS nutritionAssessmentDesc ,"
			+ " nurse.fallRiskAssessmentDesc AS fallRiskAssessmentDesc ,"
			+ " nurse.examinationDesc AS examinationDesc ,"
			+ " nurse.patientId AS patientId ,"
			+ " nurse.encounterId AS encounterId ,"
			+ " nurse.admissionId AS admissionId ,"
			+ " nurse.patientId AS patientId ,"
			+ " to_char(nurse.assessmentStartTime,'hh24:mi')  AS assessmentStartTime ,"
			+ " to_char(nurse.assessmentEndTime,'hh24:mi')  AS assessmentEndTime ,"
			+ " nurse.arrivedBy AS arrivedBy ,"
			+ " nurse.unitId AS unitId ,"
			+ " nurse.organizationId AS organizationId ,"
			+ " nurse.isSubmit AS isSubmit ,"
			+ " nurse.status AS status ,"
			+ "to_char(nurse.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate ,"
			+ "to_char(nurse.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate "
				+ " FROM NursingInitialAssessmentModel nurse"
				+ " WHERE nurse.patientId = :patientId"
				+ " and nurse.organizationId = :organizationId"
				+ " and nurse.unitId = :unitId "
				+ " and nurse.encounterId = :id "
				+ " order by nurse.initialAssessmentId  desc "
				),
	})
@Entity
@Table(name = "t_initial_assessment_nursing", schema = "opd")
@SequenceGenerator(name = "t_initial_assessment_nursing_seq", sequenceName = "opd.t_initial_assessment_nursing_seq", allocationSize = 1)
public class NursingInitialAssessmentModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_initial_assessment_nursing_seq")
	@Column(name = "initial_assessment_id")
	private Integer initialAssessmentId;

	@Column(name = "communication_and_cognition_desc")
	private String communicationAndCognitionDesc;
	
	@Column(name = "invasive_lines_catheter_tubes_drains_desc")
	private String invasiveLinesCatheterTubesDrainsDesc;
	
	@Column(name = "psychosocial_desc")
	private String psychosocialDesc;
	
	@Column(name = "pain_assessment_desc")
	private String painAssessment;
	
	@Column(name = "rest_and_sleep_desc")
	private String restAndSleepDesc;
	
	@Column(name = "nutrition_assessment_desc")
	private String nutritionAssessmentDesc;
	
	@Column(name = "fall_risk_assessment_desc")
	private String fallRiskAssessmentDesc;
	
	@Column(name = "examination_desc")
	private String examinationDesc;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "assessment_start_time")
	@Convert(converter = InstantConverter.class)
	private Instant assessmentStartTime;
	
	@Column(name = "assessment_end_time")
	@Convert(converter = InstantConverter.class)
	private Instant assessmentEndTime;
	
	@Column(name = "arrived_by")
	private Integer arrivedBy;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "is_submit")
	private Character isSubmit;


	public Integer getInitialAssessmentId() {
		return initialAssessmentId;
	}

	public void setInitialAssessmentId(Integer initialAssessmentId) {
		this.initialAssessmentId = initialAssessmentId;
	}

	public String getCommunicationAndCognitionDesc() {
		return communicationAndCognitionDesc;
	}

	public void setCommunicationAndCognitionDesc(String communicationAndCognitionDesc) {
		this.communicationAndCognitionDesc = communicationAndCognitionDesc;
	}

	public String getInvasiveLinesCatheterTubesDrainsDesc() {
		return invasiveLinesCatheterTubesDrainsDesc;
	}

	public void setInvasiveLinesCatheterTubesDrainsDesc(String invasiveLinesCatheterTubesDrainsDesc) {
		this.invasiveLinesCatheterTubesDrainsDesc = invasiveLinesCatheterTubesDrainsDesc;
	}

	public String getPsychosocialDesc() {
		return psychosocialDesc;
	}

	public void setPsychosocialDesc(String psychosocialDesc) {
		this.psychosocialDesc = psychosocialDesc;
	}

	

	public String getRestAndSleepDesc() {
		return restAndSleepDesc;
	}

	public void setRestAndSleepDesc(String restAndSleepDesc) {
		this.restAndSleepDesc = restAndSleepDesc;
	}

	public String getNutritionAssessmentDesc() {
		return nutritionAssessmentDesc;
	}

	public void setNutritionAssessmentDesc(String nutritionAssessmentDesc) {
		this.nutritionAssessmentDesc = nutritionAssessmentDesc;
	}

	public String getFallRiskAssessmentDesc() {
		return fallRiskAssessmentDesc;
	}

	public void setFallRiskAssessmentDesc(String fallRiskAssessmentDesc) {
		this.fallRiskAssessmentDesc = fallRiskAssessmentDesc;
	}

	public String getExaminationDesc() {
		return examinationDesc;
	}

	public void setExaminationDesc(String examinationDesc) {
		this.examinationDesc = examinationDesc;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Instant getAssessmentStartTime() {
		return assessmentStartTime;
	}

	public void setAssessmentStartTime(Instant assessmentStartTime) {
		this.assessmentStartTime = assessmentStartTime;
	}

	public Instant getAssessmentEndTime() {
		return assessmentEndTime;
	}

	public void setAssessmentEndTime(Instant assessmentEndTime) {
		this.assessmentEndTime = assessmentEndTime;
	}

	public Integer getArrivedBy() {
		return arrivedBy;
	}

	public void setArrivedBy(Integer arrivedBy) {
		this.arrivedBy = arrivedBy;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPainAssessment() {
		return painAssessment;
	}

	public void setPainAssessment(String painAssessment) {
		this.painAssessment = painAssessment;
	}

	public Character getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(Character isSubmit) {
		this.isSubmit = isSubmit;
	}
	
	
}
