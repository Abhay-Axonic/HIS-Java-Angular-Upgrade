package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_PATIENT_FALL_REPORT_BY_ADMISSION_ID",
                query = "SELECT patientFallReport.patient_fall_report_id AS \"patientFallReportId\", "
                        + " patientFallReport.admission_id AS \"admissionId\", "
                        + " patientFallReport.fall_reporting_id AS \"fallReportingId\", "
                        + " patientFallReport.fall_reporting_name AS \"fallReportingName\", "
                        + " patientFallReport.fall_reporting_name_id AS \"fallReportingNameId\", "
                        + " patientFallReport.selected_check_box AS \"selectedCheckBox\", "
                        + " patientFallReport.gcs_score_before AS \"gcsScoreBefore\", "
                        + " patientFallReport.gcs_score_after AS \"gcsScoreAfter\", "
                        + " patientFallReport.diagnosis_name AS \"diagnosisName\", "
                        + " patientFallReport.assessment_other_remark_before AS \"assessmentOtherRemarkBefore\", "
                        + " patientFallReport.assessment_other_remark_after AS \"assessmentOtherRemarkAfter\", "
                        + " patientFallReport.factor_other_text AS \"factorOtherText\", "
                        + " patientFallReport.save_as_draft AS \"saveAsDraft\", "
                        + " patientFallReport.unit_id AS \"unitId\", "
                        + " patientFallReport.organization_id AS \"organizationId\", "
                        + " patientFallReport.status AS \"status\", "
                        + " patientFallReport.incident_hour AS \"incidentHour\", "
                        + " patientFallReport.incident_minute AS \"incidentMinute\", "
                        + " to_char(patientFallReport.incident_date_time, 'dd/MM/yyyy HH24:MI:SS') AS \"incidentDateTime\", "
                        + " to_char(patientFallReport.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(patientFallReport.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", "
                        + " patientFallReport.created_by AS \"createdBy\", "
                        + " patientFallReport.updated_by AS \"updatedBy\" "
                        + " FROM adt.t_patient_fall_report patientFallReport "
                        + " WHERE patientFallReport.status = 'A' "
                        + " AND patientFallReport.save_as_draft = 'Y' "
                        + " AND patientFallReport.unit_id = :unitId "
                        + " AND patientFallReport.organization_id = :organizationId "
                        + " AND patientFallReport.admission_id = :admissionId "
                        + " ORDER BY patientFallReport.patient_fall_report_id DESC"
        )
})

@Entity
@Table(name = "t_patient_fall_report", schema = "adt")
@SequenceGenerator(name = "t_patient_fall_report_seq", sequenceName = "adt.t_patient_fall_report_seq", allocationSize = 1)
public class PatientFallReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_patient_fall_report_seq")
    @Column(name = "patient_fall_report_id")
    private Integer patientFallReportId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "fall_reporting_id")
    private Integer fallReportingId;

    @Column(name = "fall_reporting_name")
    private String fallReportingName;

    @Column(name = "fall_reporting_name_id")
    private Integer fallReportingNameId;

    @Column(name = "selected_check_box")
    private String selectedCheckBox;

    @Column(name = "gcs_score_before")
    private Integer gcsScoreBefore;

    @Column(name = "gcs_score_after")
    private Integer gcsScoreAfter;

    @Column(name = "diagnosis_name")
    private String diagnosisName;

    @Column(name = "assessment_other_remark_before")
    private String assessmentOtherRemarkBefore;

    @Column(name = "assessment_other_remark_after")
    private String assessmentOtherRemarkAfter;

    @Column(name = "factor_other_text")
    private String factorOtherText;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "status")
    private Character status;

    @Column(name = "incident_date_time")
    private Date incidentDateTime;

    @Column(name = "incident_hour")
    private Integer incidentHour;

    @Column(name = "incident_minute")
    private Integer incidentMinute;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

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

    public Date getIncidentDateTime() {
        return incidentDateTime;
    }

    public void setIncidentDateTime(Date incidentDateTime) {
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
}
