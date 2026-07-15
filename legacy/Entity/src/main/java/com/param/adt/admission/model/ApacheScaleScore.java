package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;


@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_APACHE_BY_ADMISSION_ID",
                query = "SELECT apacheScaleScore.apache_scale_score_id AS \"apacheScaleScoreId\", apacheScaleScore.admission_id AS \"admissionId\", "
                        + " apacheScaleScore.total_apache_score AS \"totalApacheScore\", apacheScaleScore.save_as_draft AS \"saveAsDraft\", "
                        + " apacheScaleScore.unit_id AS \"unitId\", apacheScaleScore.organization_id AS \"organizationId\", "
                        + " apacheScaleScore.created_by AS \"createdBy\", apacheScaleScore.updated_by AS \"updatedBy\", "
                        + " apacheScaleScore.encounter_id AS \"encounterId\", apacheScaleScore.status AS \"status\", "
                        + " to_char(apacheScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(apacheScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_apache_scale_score apacheScaleScore "
                        + " WHERE apacheScaleScore.status= 'A'"
                        + " AND apacheScaleScore.unit_id= :unitId"
                        + " AND apacheScaleScore.organization_id= :organizationId"
                        + " AND apacheScaleScore.admission_id= :admissionId"
                        + " ORDER BY apacheScaleScore.apache_scale_score_id desc"
        )
})

@Entity
@Table(name = "t_apache_scale_score", schema = "adt")
@SequenceGenerator(name = "t_apache_scale_score_seq", sequenceName = "adt.t_apache_scale_score_seq", allocationSize = 1)
public class ApacheScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_apache_scale_score_seq")
    @Column(name = "apache_scale_score_id")
    private Integer apacheScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_apache_score")
    private Integer totalApacheScore;

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

    public Integer getApacheScaleScoreId() {
        return apacheScaleScoreId;
    }

    public void setApacheScaleScoreId(Integer apacheScaleScoreId) {
        this.apacheScaleScoreId = apacheScaleScoreId;
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

    public Integer getTotalApacheScore() {
        return totalApacheScore;
    }

    public void setTotalApacheScore(Integer totalApacheScore) {
        this.totalApacheScore = totalApacheScore;
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
