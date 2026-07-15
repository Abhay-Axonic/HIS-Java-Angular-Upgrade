package com.param.global.dto;

import javax.persistence.Column;
import java.util.Date;

public class AbcdeAssessmentDto {
    private Integer abcdeAssessmentId;
    private String abcdeAssessmentDesc;
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
    private Date assesmentTime;
    private  String doctorName;
    private Integer doctorId;

    public Integer getAbcdeAssessmentId() {
        return abcdeAssessmentId;
    }

    public void setAbcdeAssessmentId(Integer abcdeAssessmentId) {
        this.abcdeAssessmentId = abcdeAssessmentId;
    }

    public String getAbcdeAssessmentDesc() {
        return abcdeAssessmentDesc;
    }

    public void setAbcdeAssessmentDesc(String abcdeAssessmentDesc) {
        this.abcdeAssessmentDesc = abcdeAssessmentDesc;
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

//    public String getAssesmentTime() {
//        return assesmentTime;
//    }
//
//    public void setAssesmentTime(String assesmentTime) {
//        this.assesmentTime = assesmentTime;
//    }

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

    public Date getAssesmentTime() {
        return assesmentTime;
    }

    public void setAssesmentTime(Date assesmentTime) {
        this.assesmentTime = assesmentTime;
    }
}
