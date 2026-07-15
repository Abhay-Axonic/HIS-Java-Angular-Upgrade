package com.param.adt.admission.dto;

public class NursingParamScaleScores {

    private Integer admissionId;

    private Integer encounterId;

    private Integer findingId;
    private Integer gcsDraftScaleScoreId;

    private String findingValue;

    private Integer findingPoints;

    private String findingDescription;

    private Integer parameterScaleId;

    private String parameterName;

    private String parameterDesc;

    private Integer totalGcsScore;

    private Character saveAsDraft;

    private Character status;

    private Integer organizationId;

    private Integer unitId;

    private Integer createdBy;

    private String createdDate;

    private Integer updatedBy;

    private String updatedDate;

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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

    public Integer getFindingId() {
        return findingId;
    }

    public void setFindingId(Integer findingId) {
        this.findingId = findingId;
    }

    public Integer getGcsDraftScaleScoreId() {
        return gcsDraftScaleScoreId;
    }

    public void setGcsDraftScaleScoreId(Integer gcsDraftScaleScoreId) {
        this.gcsDraftScaleScoreId = gcsDraftScaleScoreId;
    }

    public String getFindingValue() {
        return findingValue;
    }

    public void setFindingValue(String findingValue) {
        this.findingValue = findingValue;
    }

    public Integer getFindingPoints() {
        return findingPoints;
    }

    public void setFindingPoints(Integer findingPoints) {
        this.findingPoints = findingPoints;
    }

    public String getFindingDescription() {
        return findingDescription;
    }

    public void setFindingDescription(String findingDescription) {
        this.findingDescription = findingDescription;
    }

    public Integer getParameterScaleId() {
        return parameterScaleId;
    }

    public void setParameterScaleId(Integer parameterScaleId) {
        this.parameterScaleId = parameterScaleId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterDesc() {
        return parameterDesc;
    }

    public void setParameterDesc(String parameterDesc) {
        this.parameterDesc = parameterDesc;
    }
}
