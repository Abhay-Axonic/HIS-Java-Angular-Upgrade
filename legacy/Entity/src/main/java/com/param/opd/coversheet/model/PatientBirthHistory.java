package com.param.opd.coversheet.model;

import com.param.global.common.DateConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;


@Entity
@Table(name = "t_patient_birth_history", schema = "patient")
@SequenceGenerator(name = "patient_birth_history_sequence", sequenceName = "patient.patient_birth_history_sequence", allocationSize = 1)
public class PatientBirthHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_birth_history_sequence")
    @Column(name = "birth_history_id")
    private Integer birthHistoryId;

    @Column(name = "description")
    private String description;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_date")
    @Convert(converter = DateConverter.class)
    private Long createdDate;

    @Column(name = "updated_date")
    @Convert(converter = DateConverter.class)
    private Long updatedDate;

    @Column(name = "status")
    private Character status;

    public Integer getBirthHistoryId() {
        return birthHistoryId;
    }

    public void setBirthHistoryId(Integer birthHistoryId) {
        this.birthHistoryId = birthHistoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
