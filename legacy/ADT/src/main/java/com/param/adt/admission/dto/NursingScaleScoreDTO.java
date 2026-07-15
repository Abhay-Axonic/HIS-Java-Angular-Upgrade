package com.param.adt.admission.dto;

import java.util.Date;

public class NursingScaleScoreDTO {

    private Integer nursingScaleScoreId;

    private Integer gcsScaleScoreId;

    private Integer encounterId;

    private Integer admissionId;

    private String nursingScoreName;

    private String nursingScaleTotalScore;

    private Integer totalGcsScore;

    private Integer unitId;

    private Integer organizationId;

    private Character saveAsDraft;

    private Integer tabNumber;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;
    
    private Boolean IsScoreWith3;

    private Integer specialityId;

    private Integer specialityNursingScoreMapperId;

    private String nursingScoreCode;

    public Integer getNursingScaleScoreId() {
        return nursingScaleScoreId;
    }

    public void setNursingScaleScoreId(Integer nursingScaleScoreId) {
        this.nursingScaleScoreId = nursingScaleScoreId;
    }

    public Integer getGcsScaleScoreId() {
        return gcsScaleScoreId;
    }

    public void setGcsScaleScoreId(Integer gcsScaleScoreId) {
        this.gcsScaleScoreId = gcsScaleScoreId;
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

    public String getNursingScoreName() {
        return nursingScoreName;
    }

    public void setNursingScoreName(String nursingScoreName) {
        this.nursingScoreName = nursingScoreName;
    }

    public String getNursingScaleTotalScore() {
        return nursingScaleTotalScore;
    }

    public void setNursingScaleTotalScore(String nursingScaleTotalScore) {
        this.nursingScaleTotalScore = nursingScaleTotalScore;
    }

    public Integer getTotalGcsScore() {
        return totalGcsScore;
    }
    public void setTotalGcsScore(Integer totalGcsScore) {
        this.totalGcsScore = totalGcsScore;
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

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
    }

    public Integer getTabNumber() {
        return tabNumber;
    }

    public void setTabNumber(Integer tabNumber) {
        this.tabNumber = tabNumber;
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

	public Boolean getIsScoreWith3() {
		return IsScoreWith3;
	}

	public void setIsScoreWith3(Boolean isScoreWith3) {
		IsScoreWith3 = isScoreWith3;
	}

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public Integer getSpecialityNursingScoreMapperId() {
        return specialityNursingScoreMapperId;
    }

    public void setSpecialityNursingScoreMapperId(Integer specialityNursingScoreMapperId) {
        this.specialityNursingScoreMapperId = specialityNursingScoreMapperId;
    }

    public String getNursingScoreCode() {
        return nursingScoreCode;
    }

    public void setNursingScoreCode(String nursingScoreCode) {
        this.nursingScoreCode = nursingScoreCode;
    }
}
