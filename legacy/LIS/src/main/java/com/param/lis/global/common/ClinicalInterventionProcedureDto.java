package com.param.lis.global.common;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ClinicalInterventionProcedureDto {
    private Integer clinicalInterventionProcedureId;
    private Integer patientId;
    private Integer labSampleDtlsId;
    private Character isClinicalInterventionRequired;
    private Integer interventionRequestedNurseId;
    private String  interventionStartDate;
    private Integer interventionCompletedDoctorId;
    private String  interventionEndDate;
    private Character status;
    private Integer organizationId;
    private Integer unitId;
    private Integer createdBy;
    private String CreatedDate;
    private Integer updatedBy;
    private String  updatedDate;
    private String  remarks;

    private List<Integer> patientIds;
    private List<Integer> labSampleDtlsIds;

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

    public Integer getInterventionCompletedDoctorId() {
        return interventionCompletedDoctorId;
    }

    public void setInterventionCompletedDoctorId(Integer interventionCompletedDoctorId) {
        this.interventionCompletedDoctorId = interventionCompletedDoctorId;
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

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<Integer> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(List<Integer> patientIds) {
        this.patientIds = patientIds;
    }

    public List<Integer> getLabSampleDtlsIds() {
        return labSampleDtlsIds;
    }

    public void setLabSampleDtlsIds(List<Integer> labSampleDtlsIds) {
        this.labSampleDtlsIds = labSampleDtlsIds;
    }

    public String getInterventionStartDate() {
        return interventionStartDate;
    }

    public void setInterventionStartDate(String interventionStartDate) {
        this.interventionStartDate = interventionStartDate;
    }

    public String getInterventionEndDate() {
        return interventionEndDate;
    }

    public void setInterventionEndDate(String interventionEndDate) {
        this.interventionEndDate = interventionEndDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
