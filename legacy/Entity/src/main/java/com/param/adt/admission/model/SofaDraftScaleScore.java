package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;


@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_AS_DRAFT_SOFA_BY_ADMISSION_ID",
                query = "SELECT sofaDraftScaleScore.sofa_draft_scale_score_id AS \"gcsDraftScaleScoreId\", sofaDraftScaleScore.admission_id AS \"admissionId\", "
                        + " sofaDraftScaleScore.total_sofa_score AS \"totalGcsScore\", sofaDraftScaleScore.save_as_draft AS \"saveAsDraft\", "
                        + " sofaDraftScaleScore.finding_id AS \"findingId\", sofaDraftScaleScore.finding_value AS \"findingValue\", "
                        + " sofaDraftScaleScore.parameter_scale_id AS \"parameterScaleId\", sofaDraftScaleScore.parameter_name AS \"parameterName\", "
                        + " sofaDraftScaleScore.parameter_desc AS \"parameterDesc\", sofaDraftScaleScore.finding_points AS \"findingPoints\", "
                        + " sofaDraftScaleScore.unit_id AS \"unitId\", sofaDraftScaleScore.organization_id AS \"organizationId\", "
                        + " sofaDraftScaleScore.created_by AS \"createdBy\", sofaDraftScaleScore.updated_by AS \"updatedBy\", "
                        + " sofaDraftScaleScore.encounter_id AS \"encounterId\", sofaDraftScaleScore.status AS \"status\", "
                        + " to_char(sofaDraftScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(sofaDraftScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_sofa_draft_scale_score sofaDraftScaleScore "
                        + " WHERE sofaDraftScaleScore.status= 'A'"
                        + " AND sofaDraftScaleScore.unit_id= :unitId"
                        + " AND sofaDraftScaleScore.organization_id= :organizationId"
                        + " AND sofaDraftScaleScore.admission_id= :admissionId"
                        + " ORDER BY sofaDraftScaleScore.sofa_draft_scale_score_id DESC"
        )
})


@Entity
@Table(name = "t_sofa_draft_scale_score", schema = "adt")
@SequenceGenerator(name = "t_sofa_draft_scale_score_seq", sequenceName = "adt.t_sofa_draft_scale_score_seq", allocationSize = 1)
public class SofaDraftScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_sofa_draft_scale_score_seq")
    @Column(name = "sofa_draft_scale_score_id")
    private Integer sofaDraftScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_sofa_score")
    private Integer totalSofaScore;

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

    @Column(name = "parameter_desc")
    private String parameterDesc;

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

    public Integer getSofaDraftScaleScoreId() {
        return sofaDraftScaleScoreId;
    }

    public void setSofaDraftScaleScoreId(Integer sofaDraftScaleScoreId) {
        this.sofaDraftScaleScoreId = sofaDraftScaleScoreId;
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

    public String getParameterDesc() {
        return parameterDesc;
    }

    public void setParameterDesc(String parameterDesc) {
        this.parameterDesc = parameterDesc;
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
