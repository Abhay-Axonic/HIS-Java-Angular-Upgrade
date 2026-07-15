package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;


@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_AS_DRAFT_BRADEN_BY_ADMISSION_ID",
                query = "SELECT bradenDraftScaleScore.braden_draft_scale_score_id AS \"gcsDraftScaleScoreId\", "
                        + " bradenDraftScaleScore.admission_id AS \"admissionId\", "
                        + " bradenDraftScaleScore.encounter_id AS \"encounterId\", "
                        + " bradenDraftScaleScore.total_braden_score AS \"totalGcsScore\", "
                        + " bradenDraftScaleScore.save_as_draft AS \"saveAsDraft\", "
                        + " bradenDraftScaleScore.finding_id AS \"findingId\", "
                        + " bradenDraftScaleScore.finding_value AS \"findingValue\", "
                        + " bradenDraftScaleScore.finding_points AS \"findingPoints\", "
                        + " bradenDraftScaleScore.finding_description AS \"findingDescription\", "
                        + " bradenDraftScaleScore.parameter_scale_id AS \"parameterScaleId\", "
                        + " bradenDraftScaleScore.parameter_name AS \"parameterName\", "
                        + " bradenDraftScaleScore.unit_id AS \"unitId\", "
                        + " bradenDraftScaleScore.organization_id AS \"organizationId\", "
                        + " bradenDraftScaleScore.created_by AS \"createdBy\", "
                        + " bradenDraftScaleScore.updated_by AS \"updatedBy\", "
                        + " bradenDraftScaleScore.status AS \"status\", "
                        + " to_char(bradenDraftScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(bradenDraftScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_braden_draft_scale_score bradenDraftScaleScore "
                        + " WHERE bradenDraftScaleScore.status= 'A'"
                        + " AND bradenDraftScaleScore.unit_id= :unitId"
                        + " AND bradenDraftScaleScore.organization_id= :organizationId"
                        + " AND bradenDraftScaleScore.admission_id= :admissionId"
                        + " ORDER BY bradenDraftScaleScore.braden_draft_scale_score_id desc"
        )
})

@Entity
@Table(name = "t_braden_draft_scale_score", schema = "adt")
@SequenceGenerator(name = "t_braden_draft_scale_score_seq", sequenceName = "adt.t_braden_draft_scale_score_seq", allocationSize = 1)
public class BradenDraftScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_braden_draft_scale_score_seq")
    @Column(name = "braden_draft_scale_score_id")
    private Integer bradenDraftScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "total_braden_score")
    private Integer totalBradenScore;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "finding_id")
    private Integer findingId;

    @Column(name = "finding_value")
    private String findingValue;

    @Column(name = "finding_points")
    private Integer findingPoints;

    @Column(name = "finding_description")
    private String findingDescription;

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

    public Integer getBradenDraftScaleScoreId() {
        return bradenDraftScaleScoreId;
    }

    public void setBradenDraftScaleScoreId(Integer bradenDraftScaleScoreId) {
        this.bradenDraftScaleScoreId = bradenDraftScaleScoreId;
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

    public String getFindingDescription() {
        return findingDescription;
    }

    public void setFindingDescription(String findingDescription) {
        this.findingDescription = findingDescription;
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
