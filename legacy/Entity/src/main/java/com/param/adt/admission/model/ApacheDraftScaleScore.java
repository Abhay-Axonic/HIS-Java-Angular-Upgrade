package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;


@NamedNativeQueries({
        @NamedNativeQuery(name="GET_AS_DRAFT_APACHE_BY_ADMISSION_ID",
                query="SELECT apacheDraftScaleScore.apache_draft_scale_score_id AS \"gcsDraftScaleScoreId\", apacheDraftScaleScore.admission_id AS \"admissionId\", "
                        + " apacheDraftScaleScore.total_apache_score AS \"totalGcsScore\", apacheDraftScaleScore.save_as_draft AS \"saveAsDraft\", "
                        + " apacheDraftScaleScore.finding_id AS \"findingId\", apacheDraftScaleScore.finding_value AS \"findingValue\", "
                        + " apacheDraftScaleScore.finding_points AS \"findingPoints\", "
                        + " apacheDraftScaleScore.parameter_scale_id AS \"parameterScaleId\", apacheDraftScaleScore.parameter_name AS \"parameterName\", "
                        + " apacheDraftScaleScore.unit_id AS \"unitId\", apacheDraftScaleScore.organization_id AS \"organizationId\", "
                        + " apacheDraftScaleScore.created_by AS \"createdBy\", apacheDraftScaleScore.updated_by AS \"updatedBy\", "
                        + " apacheDraftScaleScore.encounter_id AS \"encounterId\", apacheDraftScaleScore.status AS \"status\", "
                        + " to_char(apacheDraftScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(apacheDraftScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_apache_draft_scale_score apacheDraftScaleScore "
                        + " WHERE apacheDraftScaleScore.status= 'A'"
                        + " AND apacheDraftScaleScore.unit_id= :unitId"
                        + " AND apacheDraftScaleScore.organization_id= :organizationId"
                        + " AND apacheDraftScaleScore.admission_id= :admissionId"
                        + " ORDER BY apacheDraftScaleScore.apache_draft_scale_score_id desc"
        )
})

@Entity
@Table(name = "t_apache_draft_scale_score", schema = "adt")
@SequenceGenerator(name = "t_apache_draft_scale_score_seq", sequenceName = "adt.t_apache_draft_scale_score_seq", allocationSize = 1)
public class ApacheDraftScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_apache_draft_scale_score_seq")
    @Column(name = "apache_draft_scale_score_id")
    private Integer apacheDraftScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_apache_score")
    private Integer totalApacheScore;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "finding_id")
    private Integer findingId;

    @Column(name = "finding_value")
    private String findingValue;

    @Column(name = "finding_points")
    private Integer findingPoints;

    @Column(name = "parameter_scale_id")
    private Integer parameterScaleId;

    @Column(name = "parameter_name")
    private String parameterName;

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

    public Integer getApacheDraftScaleScoreId() {
        return apacheDraftScaleScoreId;
    }

    public void setApacheDraftScaleScoreId(Integer apacheDraftScaleScoreId) {
        this.apacheDraftScaleScoreId = apacheDraftScaleScoreId;
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

    public Integer getFindingId() {
        return findingId;
    }

    public void setFindingId(Integer findingId) {
        this.findingId = findingId;
    }

    public String getFindingValue() {
        return findingValue;
    }

    public void setFindingValue(String findingValue) {
        this.findingValue = findingValue;
    }

    public Integer getFindingPoints() {
        return findingPoints;
    }

    public void setFindingPoints(Integer findingPoints) {
        this.findingPoints = findingPoints;
    }

    public Integer getParameterScaleId() {
        return parameterScaleId;
    }

    public void setParameterScaleId(Integer parameterScaleId) {
        this.parameterScaleId = parameterScaleId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
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
