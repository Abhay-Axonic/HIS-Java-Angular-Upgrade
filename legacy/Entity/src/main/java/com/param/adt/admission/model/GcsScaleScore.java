package com.param.adt.admission.model;


import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name="GET_GCS_BY_ADMISSION_ID",
                query="SELECT gcsScaleScore.gcs_scale_score_id AS \"gcsScaleScoreId\", gcsScaleScore.admission_id AS \"admissionId\", "
                        + " gcsScaleScore.total_gcs_score AS \"totalGcsScore\", gcsScaleScore.save_as_draft AS \"saveAsDraft\", "
                        + " gcsScaleScore.unit_id AS \"unitId\", gcsScaleScore.organization_id AS \"organizationId\", "
                        + " gcsScaleScore.created_by AS \"createdBy\", gcsScaleScore.updated_by AS \"updatedBy\", "
                        + " gcsScaleScore.encounter_id AS \"encounterId\", gcsScaleScore.status AS \"status\", "
                        + " to_char(gcsScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(gcsScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_gcs_scale_score gcsScaleScore "
                        + " WHERE gcsScaleScore.status= 'A'"
                        + " AND gcsScaleScore.unit_id= :unitId"
                        + " AND gcsScaleScore.organization_id= :organizationId"
                        + " AND gcsScaleScore.admission_id= :admissionId"
                        + " ORDER BY gcsScaleScore.gcs_scale_score_id desc"
        )
})


@Entity
@Table(name = "t_gcs_scale_score", schema = "adt")
@SequenceGenerator(name = "t_gcs_scale_score_seq", sequenceName = "adt.t_gcs_scale_score_seq", allocationSize = 1)
public class GcsScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_gcs_scale_score_seq")
    @Column(name = "gcs_scale_score_id")
    private Integer gcsScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_gcs_score")
    private Integer totalGcsScore;

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

    public Integer getGcsScaleScoreId() {
        return gcsScaleScoreId;
    }

    public void setGcsScaleScoreId(Integer gcsScaleScoreId) {
        this.gcsScaleScoreId = gcsScaleScoreId;
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

    public Integer getTotalGcsScore() {
        return totalGcsScore;
    }

    public void setTotalGcsScore(Integer totalGcsScore) {
        this.totalGcsScore = totalGcsScore;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
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
