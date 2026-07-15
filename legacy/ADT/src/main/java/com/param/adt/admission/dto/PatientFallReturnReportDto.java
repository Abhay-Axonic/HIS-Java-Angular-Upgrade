package com.param.adt.admission.dto;

import javax.persistence.Column;
import java.util.Date;

public class PatientFallReturnReportDto {

    private Integer patientFallReportId;

    private Integer admissionId;

    private Integer fallReportingId;

    private String fallReportingName;

    private Integer fallReportingNameId;

    private String selectedCheckBox;

    private Integer gcsScoreBefore;

    private Integer gcsScoreAfter;

    private String diagnosisName;

    private String assessmentOtherRemarkBefore;

    private String assessmentOtherRemarkAfter;

    private String factorOtherText;

    private Character saveAsDraft;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String incidentDateTime;

    private Integer incidentHour;

    private Integer incidentMinute;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    public Integer getPatientFallReportId() {
        return patientFallReportId;
    }

    public void setPatientFallReportId(Integer patientFallReportId) {
        this.patientFallReportId = patientFallReportId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getFallReportingId() {
        return fallReportingId;
    }

    public void setFallReportingId(Integer fallReportingId) {
        this.fallReportingId = fallReportingId;
    }

    public String getFallReportingName() {
        return fallReportingName;
    }

    public void setFallReportingName(String fallReportingName) {
        this.fallReportingName = fallReportingName;
    }

    public Integer getFallReportingNameId() {
        return fallReportingNameId;
    }

    public void setFallReportingNameId(Integer fallReportingNameId) {
        this.fallReportingNameId = fallReportingNameId;
    }

    public String getSelectedCheckBox() {
        return selectedCheckBox;
    }

    public void setSelectedCheckBox(String selectedCheckBox) {
        this.selectedCheckBox = selectedCheckBox;
    }

    public Integer getGcsScoreBefore() {
        return gcsScoreBefore;
    }

    public void setGcsScoreBefore(Integer gcsScoreBefore) {
        this.gcsScoreBefore = gcsScoreBefore;
    }

    public Integer getGcsScoreAfter() {
        return gcsScoreAfter;
    }

    public void setGcsScoreAfter(Integer gcsScoreAfter) {
        this.gcsScoreAfter = gcsScoreAfter;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getAssessmentOtherRemarkBefore() {
        return assessmentOtherRemarkBefore;
    }

    public void setAssessmentOtherRemarkBefore(String assessmentOtherRemarkBefore) {
        this.assessmentOtherRemarkBefore = assessmentOtherRemarkBefore;
    }

    public String getAssessmentOtherRemarkAfter() {
        return assessmentOtherRemarkAfter;
    }

    public void setAssessmentOtherRemarkAfter(String assessmentOtherRemarkAfter) {
        this.assessmentOtherRemarkAfter = assessmentOtherRemarkAfter;
    }

    public String getFactorOtherText() {
        return factorOtherText;
    }

    public void setFactorOtherText(String factorOtherText) {
        this.factorOtherText = factorOtherText;
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

    public String getIncidentDateTime() {
        return incidentDateTime;
    }

    public void setIncidentDateTime(String incidentDateTime) {
        this.incidentDateTime = incidentDateTime;
    }

    public Integer getIncidentHour() {
        return incidentHour;
    }

    public void setIncidentHour(Integer incidentHour) {
        this.incidentHour = incidentHour;
    }

    public Integer getIncidentMinute() {
        return incidentMinute;
    }

    public void setIncidentMinute(Integer incidentMinute) {
        this.incidentMinute = incidentMinute;
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
