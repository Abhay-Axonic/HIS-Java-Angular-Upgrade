package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_BRADEN_BY_ADMISSION_ID",
                query = "SELECT bradenScaleScore.braden_scale_score_id AS \"bradenScaleScoreId\", "
                        + " bradenScaleScore.admission_id AS \"admissionId\", "
                        + " bradenScaleScore.encounter_id AS \"encounterId\", "
                        + " bradenScaleScore.total_braden_score AS \"totalBradenScore\", "
                        + " bradenScaleScore.save_as_draft AS \"saveAsDraft\", "
                        + " bradenScaleScore.unit_id AS \"unitId\", "
                        + " bradenScaleScore.organization_id AS \"organizationId\", "
                        + " bradenScaleScore.created_by AS \"createdBy\", "
                        + " bradenScaleScore.updated_by AS \"updatedBy\", "
                        + " bradenScaleScore.status AS \"status\", "
                        + " to_char(bradenScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(bradenScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_braden_scale_score bradenScaleScore "
                        + " WHERE bradenScaleScore.status= 'A'"
                        + " AND bradenScaleScore.unit_id= :unitId"
                        + " AND bradenScaleScore.organization_id= :organizationId"
                        + " AND bradenScaleScore.admission_id= :admissionId"
                        + " ORDER BY bradenScaleScore.braden_scale_score_id desc"
        )
})

@Entity
@Table(name = "t_braden_scale_score", schema = "adt")
@SequenceGenerator(name = "t_braden_scale_score_seq", sequenceName = "adt.t_braden_scale_score_seq", allocationSize = 1)
public class BradenScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_braden_scale_score_seq")
    @Column(name = "braden_scale_score_id")
    private Integer bradenScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_braden_score")
    private Integer totalBradenScore;

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

    public Integer getBradenScaleScoreId() {
        return bradenScaleScoreId;
    }

    public void setBradenScaleScoreId(Integer bradenScaleScoreId) {
        this.bradenScaleScoreId = bradenScaleScoreId;
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

    public Integer getTotalBradenScore() {
        return totalBradenScore;
    }

    public void setTotalBradenScore(Integer totalBradenScore) {
        this.totalBradenScore = totalBradenScore;
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
