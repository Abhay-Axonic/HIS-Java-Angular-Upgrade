package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name="GET_DRAFTED_APGAR_SCORES_BY_ID",
                query="SELECT agparDraftedScores.apgar_score_id AS \"apgarScoreId\", agparDraftedScores.admission_id AS \"admissionId\", "
                        + " agparDraftedScores.total_apgar_score AS \"totalApgarScore\", agparDraftedScores.save_as_draft AS \"saveAsDraft\", "
                        + " agparDraftedScores.appearance_id AS \"appearanceId\", agparDraftedScores.pulse_id AS \"pulseId\", "
                        + " agparDraftedScores.grimace_id AS \"grimaceId\", agparDraftedScores.final_save AS \"finalSave\", "
                        + " agparDraftedScores.activity_id AS \"activityId\", agparDraftedScores.respiration_id AS \"respirationId\", "
                        + " agparDraftedScores.unit_id AS \"unitId\", agparDraftedScores.organization_id AS \"organizationId\", "
                        + " agparDraftedScores.created_by AS \"createdBy\", agparDraftedScores.updated_by AS \"updatedBy\", "
                        + " agparDraftedScores.encounter_id AS \"encounterId\", agparDraftedScores.status AS \"status\", "
                        + " to_char(agparDraftedScores.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(agparDraftedScores.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_apgar_score agparDraftedScores "
                        + " WHERE agparDraftedScores.status= 'A' "
                        + " AND agparDraftedScores.final_save= 'N' "
                        + " AND agparDraftedScores.unit_id= :unitId"
                        + " AND agparDraftedScores.organization_id= :organizationId"
        )
})

@Entity
@Table(name = "t_apgar_score", schema = "adt")
@SequenceGenerator(name = "t_apgar_score_seq", sequenceName = "adt.t_apgar_score_seq", allocationSize = 1)
public class APGARScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_apgar_score_seq")
    @Column(name = "apgar_score_id")
    private Integer apgarScoreId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "appearance_id")
    private Integer appearanceId;

    @Column(name = "pulse_id")
    private Integer pulseId;

    @Column(name = "grimace_id")
    private Integer grimaceId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "respiration_id")
    private Integer respirationId;

    @Column(name = "total_apgar_score")
    private Integer totalApgarScore;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "final_save")
    private Character finalSave;

    @Column(name = "status")
    private Character status;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Integer getApgarScoreId() {
        return apgarScoreId;
    }

    public void setApgarScoreId(Integer apgarScoreId) {
        this.apgarScoreId = apgarScoreId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public Integer getAppearanceId() {
        return appearanceId;
    }

    public void setAppearanceId(Integer appearanceId) {
        this.appearanceId = appearanceId;
    }

    public Integer getPulseId() {
        return pulseId;
    }

    public void setPulseId(Integer pulseId) {
        this.pulseId = pulseId;
    }

    public Integer getGrimaceId() {
        return grimaceId;
    }

    public void setGrimaceId(Integer grimaceId) {
        this.grimaceId = grimaceId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getRespirationId() {
        return respirationId;
    }

    public void setRespirationId(Integer respirationId) {
        this.respirationId = respirationId;
    }

    public Integer getTotalApgarScore() {
        return totalApgarScore;
    }

    public void setTotalApgarScore(Integer totalApgarScore) {
        this.totalApgarScore = totalApgarScore;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
    }

    public Character getFinalSave() {
        return finalSave;
    }

    public void setFinalSave(Character finalSave) {
        this.finalSave = finalSave;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
