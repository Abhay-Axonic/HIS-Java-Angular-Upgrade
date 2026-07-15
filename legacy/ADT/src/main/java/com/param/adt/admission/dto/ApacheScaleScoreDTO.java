package com.param.adt.admission.dto;

import java.util.List;

public class ApacheScaleScoreDTO {

    private Integer apacheScaleScoreId;

    private Integer nursingScaleScoreId;

    private String nursingScoreName;

    private Integer encounterId;

    private Integer admissionId;

    private Integer unitId;

    private Integer organizationId;

    private Integer totalGcsScore;

    private Character saveAsDraft;

    private List<NursingParamScaleScores> nursingParamScaleScores;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    public Integer getApacheScaleScoreId() {
        return apacheScaleScoreId;
    }

    public void setApacheScaleScoreId(Integer apacheScaleScoreId) {
        this.apacheScaleScoreId = apacheScaleScoreId;
    }

    public Integer getNursingScaleScoreId() {
        return nursingScaleScoreId;
    }

    public void setNursingScaleScoreId(Integer nursingScaleScoreId) {
        this.nursingScaleScoreId = nursingScaleScoreId;
    }

    public String getNursingScoreName() {
        return nursingScoreName;
    }

    public void setNursingScoreName(String nursingScoreName) {
        this.nursingScoreName = nursingScoreName;
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

    public Integer getTotalGcsScore() {
        return totalGcsScore;
    }

    public void setTotalGcsScore(Integer totalGcsScore) {
        this.totalGcsScore = totalGcsScore;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
    }

    public List<NursingParamScaleScores> getNursingParamScaleScores() {
        return nursingParamScaleScores;
    }

    public void setNursingParamScaleScores(List<NursingParamScaleScores> nursingParamScaleScores) {
        this.nursingParamScaleScores = nursingParamScaleScores;
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
}
