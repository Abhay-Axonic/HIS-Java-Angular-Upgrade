package com.param.adt.admission.dto;

public class ApacheDraftScaleScoreDTO {

    private Integer apacheDraftScaleScoreId ;

    private Integer encounterId;

    private Integer admissionId;

    private Integer unitId;

    private Integer organizationId;

    private Integer totalApacheScore;

    private Character saveAsDraft;

    private Integer findingId;

    private String findingValue;

    private Integer parameterScaleId;

    private String parameterName;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    public Integer getApacheDraftScaleScoreId() {
        return apacheDraftScaleScoreId;
    }

    public void setApacheDraftScaleScoreId(Integer apacheDraftScaleScoreId) {
        this.apacheDraftScaleScoreId = apacheDraftScaleScoreId;
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

    public Integer getTotalApacheScore() {
        return totalApacheScore;
    }

    public void setTotalApacheScore(Integer totalApacheScore) {
        this.totalApacheScore = totalApacheScore;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
    }

    public Integer getFindingId() {
        return findingId;
    }

    public void setFindingId(Integer findingId) {
        this.findingId = findingId;
    }

    public String getFindingValue() {
        return findingValue;
    }

    public void setFindingValue(String findingValue) {
        this.findingValue = findingValue;
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
