package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name="GET_SOFA_BY_ADMISSION_ID",
                query="SELECT sofaScaleScore.sofa_scale_score_id AS \"sofaScaleScoreId\", sofaScaleScore.admission_id AS \"admissionId\", "
                        + " sofaScaleScore.total_sofa_score AS \"totalSofaScore\", sofaScaleScore.save_as_draft AS \"saveAsDraft\", "
                        + " sofaScaleScore.unit_id AS \"unitId\", sofaScaleScore.organization_id AS \"organizationId\", "
                        + " sofaScaleScore.created_by AS \"createdBy\", sofaScaleScore.updated_by AS \"updatedBy\", "
                        + " sofaScaleScore.encounter_id AS \"encounterId\", sofaScaleScore.status AS \"status\", "
                        + " to_char(sofaScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(sofaScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_sofa_scale_score sofaScaleScore "
                        + " WHERE sofaScaleScore.status= 'A'"
                        + " AND sofaScaleScore.unit_id= :unitId"
                        + " AND sofaScaleScore.organization_id= :organizationId"
                        + " AND sofaScaleScore.admission_id= :admissionId"
                        + " ORDER BY sofaScaleScore.sofa_scale_score_id DESC"
        )
})

@Entity
@Table(name = "t_sofa_scale_score", schema = "adt")
@SequenceGenerator(name = "t_sofa_scale_score_seq", sequenceName = "adt.t_sofa_scale_score_seq", allocationSize = 1)
public class SofaScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_sofa_scale_score_seq")
    @Column(name = "sofa_scale_score_id")
    private Integer sofaScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_sofa_score")
    private Integer totalSofaScore;

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

    public Integer getSofaScaleScoreId() {
        return sofaScaleScoreId;
    }

    public void setSofaScaleScoreId(Integer sofaScaleScoreId) {
        this.sofaScaleScoreId = sofaScaleScoreId;
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

    public Integer getTotalSofaScore() {
        return totalSofaScore;
    }

    public void setTotalSofaScore(Integer totalSofaScore) {
        this.totalSofaScore = totalSofaScore;
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