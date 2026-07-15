package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_npuap_scale_score", schema = "adt")
@SequenceGenerator(name = "t_npuap_scale_score_seq", sequenceName = "adt.t_npuap_scale_score_seq", allocationSize = 1)
public class NpuapScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_npuap_scale_score_seq")
    @Column(name = "npuap_scale_score_id")
    private Integer npuapScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_npuap_score")
    private String totalNpuapScore;

    @Column(name = "npuap")
    private String npuap;

    @Column(name = "epuap")
    private String epuap;

    @Column(name = "defining_characteristics")
    private String definingCharacteristics;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "status")
    private Character status;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Integer getNpuapScaleScoreId() {
        return npuapScaleScoreId;
    }

    public void setNpuapScaleScoreId(Integer npuapScaleScoreId) {
        this.npuapScaleScoreId = npuapScaleScoreId;
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

    public String getTotalNpuapScore() {
        return totalNpuapScore;
    }

    public void setTotalNpuapScore(String totalNpuapScore) {
        this.totalNpuapScore = totalNpuapScore;
    }

    public String getNpuap() {
        return npuap;
    }

    public void setNpuap(String npuap) {
        this.npuap = npuap;
    }

    public String getEpuap() {
        return epuap;
    }

    public void setEpuap(String epuap) {
        this.epuap = epuap;
    }

    public String getDefiningCharacteristics() {
        return definingCharacteristics;
    }

    public void setDefiningCharacteristics(String definingCharacteristics) {
        this.definingCharacteristics = definingCharacteristics;
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
