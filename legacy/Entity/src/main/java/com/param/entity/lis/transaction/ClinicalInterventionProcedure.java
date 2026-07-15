package com.param.entity.lis.transaction;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "t_clinical_intervention_procedure", schema = "public")
@SequenceGenerator(name = "clinical_intervention_procedure_seq", sequenceName = "public.clinical_intervention_procedure_seq", allocationSize = 1)
public class ClinicalInterventionProcedure {
    @Id
    @Column(name = "clinical_intervention_procedure_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clinical_intervention_procedure_seq")
    private Integer clinicalInterventionProcedureId;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "lab_sample_dtls_id")
    private Integer labSampleDtlsId;

    @Column(name = "is_clinical_intervention_required")
    private Character isClinicalInterventionRequired;

    @Column(name = "intervention_requested_nurse_id")
    private Integer interventionRequestedNurseId;

    @Column(name = "intervention_start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date  interventionStartDateTime;

    @Column(name = "intervention_processed_doctor_id")
    private Integer interventionProcessedDoctorId;

    @Column(name = "intervention_completed_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date  interventionCompletedDateTime;

    @Column(name = "intervention_remark")
    private String interventionRemarks;

    @Column(name = "status")
    private Character status;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date  createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Integer getClinicalInterventionProcedureId() {
        return clinicalInterventionProcedureId;
    }

    public void setClinicalInterventionProcedureId(Integer clinicalInterventionProcedureId) {
        this.clinicalInterventionProcedureId = clinicalInterventionProcedureId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getLabSampleDtlsId() {
        return labSampleDtlsId;
    }

    public void setLabSampleDtlsId(Integer labSampleDtlsId) {
        this.labSampleDtlsId = labSampleDtlsId;
    }

    public Character getIsClinicalInterventionRequired() {
        return isClinicalInterventionRequired;
    }

    public void setIsClinicalInterventionRequired(Character isClinicalInterventionRequired) {
        this.isClinicalInterventionRequired = isClinicalInterventionRequired;
    }

    public Integer getInterventionRequestedNurseId() {
        return interventionRequestedNurseId;
    }

    public void setInterventionRequestedNurseId(Integer interventionRequestedNurseId) {
        this.interventionRequestedNurseId = interventionRequestedNurseId;
    }

    public Date getInterventionStartDateTime() {
        return interventionStartDateTime;
    }

    public void setInterventionStartDateTime(Date interventionStartDateTime) {
        this.interventionStartDateTime = interventionStartDateTime;
    }

    public Integer getInterventionProcessedDoctorId() {
        return interventionProcessedDoctorId;
    }

    public void setInterventionProcessedDoctorId(Integer interventionProcessedDoctorId) {
        this.interventionProcessedDoctorId = interventionProcessedDoctorId;
    }

    public Date getInterventionCompletedDateTime() {
        return interventionCompletedDateTime;
    }

    public void setInterventionCompletedDateTime(Date interventionCompletedDateTime) {
        this.interventionCompletedDateTime = interventionCompletedDateTime;
    }

    public String getInterventionRemarks() {
        return interventionRemarks;
    }

    public void setInterventionRemarks(String interventionRemarks) {
        this.interventionRemarks = interventionRemarks;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
