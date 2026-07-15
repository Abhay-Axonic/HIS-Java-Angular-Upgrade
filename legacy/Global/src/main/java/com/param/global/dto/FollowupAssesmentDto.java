package com.param.global.dto;

import java.util.Date;

public class FollowupAssesmentDto {
    private Integer followUpAssessmentId;
    private String followUpAssessmentDesc;
    private Integer patientId;
    private Integer encounterId;
    private Integer admissionId;
    private Character status;
    private Date createdDate;
    private Integer createdBy;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer unitId;
    private Integer organizationId;
    private Date assessmentTime;
    private  String doctorName;
    private Integer doctorId;
    private Character isDraft;


    public Integer getFollowUpAssessmentId() {
        return followUpAssessmentId;
    }

    public void setFollowUpAssessmentId(Integer followUpAssessmentId) {
        this.followUpAssessmentId = followUpAssessmentId;
    }

    public String getFollowUpAssessmentDesc() {
        return followUpAssessmentDesc;
    }

    public void setFollowUpAssessmentDesc(String followUpAssessmentDesc) {
        this.followUpAssessmentDesc = followUpAssessmentDesc;
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

    public Date getAssessmentTime() {
        return assessmentTime;
    }

    public void setAssessmentTime(Date assessmentTime) {
        this.assessmentTime = assessmentTime;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Character getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Character isDraft) {
        this.isDraft = isDraft;
    }
}
