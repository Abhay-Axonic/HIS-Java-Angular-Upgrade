package com.param.adt.admission.dto;

import java.util.List;

public class NutritionalAssessmentScoreDTO {

    private Integer encounterId;

    private Integer admissionId;

    private Integer patientWeight;

    private Integer patientHeight;

    private String patientBmi;

    private Integer patientKneeHeight;

    private String totalNutritionalAssessmentScore;

    private List<NutritionalAssessmentScoreNames> nutritionalAssessmentScoreNamesList;

    private Character saveAsDraft;

    private Integer consumptionMarkerFlag;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;
    
    private Integer nutritionalAssessmentScoreMasterId;

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

    public String getTotalNutritionalAssessmentScore() {
        return totalNutritionalAssessmentScore;
    }

    public void setTotalNutritionalAssessmentScore(String totalNutritionalAssessmentScore) {
        this.totalNutritionalAssessmentScore = totalNutritionalAssessmentScore;
    }

    public List<NutritionalAssessmentScoreNames> getNutritionalAssessmentScoreNamesList() {
        return nutritionalAssessmentScoreNamesList;
    }

    public void setNutritionalAssessmentScoreNamesList(List<NutritionalAssessmentScoreNames> nutritionalAssessmentScoreNamesList) {
        this.nutritionalAssessmentScoreNamesList = nutritionalAssessmentScoreNamesList;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
    }

    public Integer getConsumptionMarkerFlag() {
        return consumptionMarkerFlag;
    }

    public void setConsumptionMarkerFlag(Integer consumptionMarkerFlag) {
        this.consumptionMarkerFlag = consumptionMarkerFlag;
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

	public Integer getNutritionalAssessmentScoreMasterId() {
		return nutritionalAssessmentScoreMasterId;
	}

	public void setNutritionalAssessmentScoreMasterId(Integer nutritionalAssessmentScoreMasterId) {
		this.nutritionalAssessmentScoreMasterId = nutritionalAssessmentScoreMasterId;
	}
}
