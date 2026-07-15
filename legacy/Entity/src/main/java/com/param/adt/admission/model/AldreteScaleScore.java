package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;


@NamedNativeQueries({
        @NamedNativeQuery(name="GET_DRAFTED_ALDRTE_SCORES_BY_ID",
                query="SELECT aldreteDraftedScores.aldrete_score_id AS \"aldreteScoreId\", aldreteDraftedScores.admission_id AS \"admissionId\", "
                        + " aldreteDraftedScores.total_aldrete_score AS \"totalAldreteScore\", aldreteDraftedScores.save_as_draft AS \"saveAsDraft\", "
                        + " aldreteDraftedScores.activity_id AS \"activityId\", aldreteDraftedScores.respiration_id AS \"respirationId\", "
                        + " aldreteDraftedScores.circulation_id AS \"circulationId\", aldreteDraftedScores.final_save AS \"finalSave\", "
                        + " aldreteDraftedScores.consciousness_id AS \"consciousnessId\", aldreteDraftedScores.oxygen_saturation_id AS \"oxygenSaturationId\", "
                        + " aldreteDraftedScores.unit_id AS \"unitId\", aldreteDraftedScores.organization_id AS \"organizationId\", "
                        + " aldreteDraftedScores.created_by AS \"createdBy\", aldreteDraftedScores.updated_by AS \"updatedBy\", "
                        + " aldreteDraftedScores.encounter_id AS \"encounterId\", aldreteDraftedScores.status AS \"status\", "
                        + " to_char(aldreteDraftedScores.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(aldreteDraftedScores.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_aldrete_score aldreteDraftedScores "
                        + " WHERE aldreteDraftedScores.status= 'A' "
                        + " AND aldreteDraftedScores.final_save= 'N' "
                        + " AND aldreteDraftedScores.unit_id= :unitId"
                        + " AND aldreteDraftedScores.organization_id= :organizationId"
        )
})


@Entity
@Table(name = "t_aldrete_score", schema = "adt")
@SequenceGenerator(name = "t_aldrete_score_seq", sequenceName = "adt.t_aldrete_score_seq", allocationSize = 1)
public class AldreteScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_aldrete_score_seq")
    @Column(name = "aldrete_score_id")
    private Integer aldreteScoreId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "respiration_id")
    private Integer respirationId;

    @Column(name = "circulation_id")
    private Integer circulationId;

    @Column(name = "consciousness_id")
    private Integer consciousnessId;

    @Column(name = "oxygen_saturation_id")
    private Integer oxygenSaturationId;

    @Column(name = "total_aldrete_score")
    private Integer totalAldreteScore;

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

    public Integer getAldreteScoreId() {
        return aldreteScoreId;
    }

    public void setAldreteScoreId(Integer aldreteScoreId) {
        this.aldreteScoreId = aldreteScoreId;
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

    public Integer getCirculationId() {
        return circulationId;
    }

    public void setCirculationId(Integer circulationId) {
        this.circulationId = circulationId;
    }

    public Integer getConsciousnessId() {
        return consciousnessId;
    }

    public void setConsciousnessId(Integer consciousnessId) {
        this.consciousnessId = consciousnessId;
    }

    public Integer getOxygenSaturationId() {
        return oxygenSaturationId;
    }

    public void setOxygenSaturationId(Integer oxygenSaturationId) {
        this.oxygenSaturationId = oxygenSaturationId;
    }

    public Integer getTotalAldreteScore() {
        return totalAldreteScore;
    }

    public void setTotalAldreteScore(Integer totalAldreteScore) {
        this.totalAldreteScore = totalAldreteScore;
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
