package com.param.entity.model.opd;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_follow_up_assessment", schema = "opd")
@SequenceGenerator(name = "t_follow_up_assessment_seq", sequenceName = "opd.t_follow_up_assessment_seq", allocationSize = 1)
public class FollowUpAssesmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_follow_up_assessment_seq")
    @Column(name = "follow_up_assessment_id")
    private Integer followUpAssessmentId;

    @Column(name = "follow_up_assessment_desc")
    private String followUpAssessmentDesc;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "status")
    private Character status;

    @Column(name = "created_date",updatable = false)
    private Date createdDate;

    @Column(name = "assessment_time")
    private Date assessmentTime;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "is_draft")
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

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
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

    public Character getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Character isDraft) {
        this.isDraft = isDraft;
    }

    public Date getAssessmentTime() {
        return assessmentTime;
    }

    public void setAssessmentTime(Date assessmentTime) {
        this.assessmentTime = assessmentTime;
    }
}
