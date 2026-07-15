package com.param.global.unit.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class InitialAssessmentICUAndERMapperDTO {

    private Integer encounterId;

    private Integer admissionId;

    private Integer initialAssessmentMapperId;
    private Integer initialAssessmentId;

    private Integer initialAssessmentStatusId;

    private String uhid;

    private Integer assessmentId;

    private String assessment;

    private Integer indexId;

    private String infusions;

    private String assessmentKey;

    private Integer assessmentValueId;

    private String assessmentValue;

    private Character saveAsDraft;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    private String  startDateTime;

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

    public Integer getInitialAssessmentStatusId() {
        return initialAssessmentStatusId;
    }

    public void setInitialAssessmentStatusId(Integer initialAssessmentStatusId) {
        this.initialAssessmentStatusId = initialAssessmentStatusId;
    }

    public Integer getInitialAssessmentId() {
        return initialAssessmentId;
    }

    public void setInitialAssessmentId(Integer initialAssessmentId) {
        this.initialAssessmentId = initialAssessmentId;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public Integer getInitialAssessmentMapperId() {
        return initialAssessmentMapperId;
    }

    public void setInitialAssessmentMapperId(Integer initialAssessmentMapperId) {
        this.initialAssessmentMapperId = initialAssessmentMapperId;
    }

//    public Integer getAdmissionId() {
//        return admissionId;
//    }
//
//    public void setAdmissionId(Integer admissionId) {
//        this.admissionId = admissionId;
//    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getInfusions() {
        return infusions;
    }

    public void setInfusions(String infusions) {
        this.infusions = infusions;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public String getAssessmentKey() {
        return assessmentKey;
    }

    public void setAssessmentKey(String assessmentKey) {
        this.assessmentKey = assessmentKey;
    }

    public Integer getAssessmentValueId() {
        return assessmentValueId;
    }

    public void setAssessmentValueId(Integer assessmentValueId) {
        this.assessmentValueId = assessmentValueId;
    }

    public String getAssessmentValue() {
        return assessmentValue;
    }

    public void setAssessmentValue(String assessmentValue) {
        this.assessmentValue = assessmentValue;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
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

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }
}
