package com.param.adt.admission.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_nutritional_assessment_score_master", schema = "adt")
@SequenceGenerator(name = "nutritional_assessment_score_master_seq", sequenceName = "adt.nutritional_assessment_score_master_seq", allocationSize = 1)
public class NutritionalAssessmentScoreMaster {

	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutritional_assessment_score_master_seq")
    @Column(name = "nutritional_assessment_score_master_id")
    private Integer nutritionalAssessmentScoreMasterId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "patient_weight")
    private Integer patientWeight;

    @Column(name = "patient_height")
    private Integer patientHeight;

    @Column(name = "patient_bmi")
    private String patientBmi;

    @Column(name = "patient_knee_height")
    private Integer patientKneeHeight;

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
    
    @Column(name = "total_nutritional_assessment_score")
    private String totalNutritionalAssessmentScore;
    

	public Integer getNutritionalAssessmentScoreMasterId() {
		return nutritionalAssessmentScoreMasterId;
	}

	public void setNutritionalAssessmentScoreMasterId(Integer nutritionalAssessmentScoreMasterId) {
		this.nutritionalAssessmentScoreMasterId = nutritionalAssessmentScoreMasterId;
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

	public Integer getPatientWeight() {
		return patientWeight;
	}

	public void setPatientWeight(Integer patientWeight) {
		this.patientWeight = patientWeight;
	}

	public Integer getPatientHeight() {
		return patientHeight;
	}

	public void setPatientHeight(Integer patientHeight) {
		this.patientHeight = patientHeight;
	}

	public String getPatientBmi() {
		return patientBmi;
	}

	public void setPatientBmi(String patientBmi) {
		this.patientBmi = patientBmi;
	}

	public Integer getPatientKneeHeight() {
		return patientKneeHeight;
	}

	public void setPatientKneeHeight(Integer patientKneeHeight) {
		this.patientKneeHeight = patientKneeHeight;
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

	public String getTotalNutritionalAssessmentScore() {
		return totalNutritionalAssessmentScore;
	}

	public void setTotalNutritionalAssessmentScore(String totalNutritionalAssessmentScore) {
		this.totalNutritionalAssessmentScore = totalNutritionalAssessmentScore;
	}
}
