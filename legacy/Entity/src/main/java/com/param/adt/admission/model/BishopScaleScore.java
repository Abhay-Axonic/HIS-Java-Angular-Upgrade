package com.param.adt.admission.model;


import javax.persistence.*;
import java.util.Date;


@NamedNativeQueries({
        @NamedNativeQuery(name="GET_DRAFTED_BISHOP_SCORES_BY_ID",
                query="SELECT bishopDraftedScores.bishop_score_id AS \"bishopScoreId\", bishopDraftedScores.admission_id AS \"admissionId\", "
                        + " bishopDraftedScores.total_bishop_score AS \"totalBishopScore\", bishopDraftedScores.save_as_draft AS \"saveAsDraft\", "
                        + " bishopDraftedScores.dilation_id AS \"dilationId\", bishopDraftedScores.length_id AS \"lengthId\", "
                        + " bishopDraftedScores.station_id AS \"stationId\", bishopDraftedScores.final_save AS \"finalSave\", "
                        + " bishopDraftedScores.consistency_id AS \"consistencyId\", bishopDraftedScores.position_id AS \"positionId\", "
                        + " bishopDraftedScores.unit_id AS \"unitId\", bishopDraftedScores.organization_id AS \"organizationId\", "
                        + " bishopDraftedScores.created_by AS \"createdBy\", bishopDraftedScores.updated_by AS \"updatedBy\", "
                        + " bishopDraftedScores.encounter_id AS \"encounterId\", bishopDraftedScores.status AS \"status\", "
                        + " to_char(bishopDraftedScores.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(bishopDraftedScores.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_bishop_score bishopDraftedScores "
                        + " WHERE bishopDraftedScores.status= 'A' "
                        + " AND bishopDraftedScores.final_save= 'N' "
                        + " AND bishopDraftedScores.unit_id= :unitId"
                        + " AND bishopDraftedScores.organization_id= :organizationId"
        )
})

@Entity
@Table(name = "t_bishop_score", schema = "adt")
@SequenceGenerator(name = "t_bishop_score_seq", sequenceName = "adt.t_bishop_score_seq", allocationSize = 1)
public class BishopScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_bishop_score_seq")
    @Column(name = "bishop_score_id")
    private Integer bishopScoreId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "dilation_id")
    private Integer dilationId;

    @Column(name = "length_id")
    private Integer lengthId;

    @Column(name = "station_id")
    private Integer stationId;

    @Column(name = "consistency_id")
    private Integer consistencyId;

    @Column(name = "position_id")
    private Integer positionId;

    @Column(name = "total_bishop_score")
    private Integer totalBishopScore;

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

    public Integer getBishopScoreId() {
        return bishopScoreId;
    }

    public void setBishopScoreId(Integer bishopScoreId) {
        this.bishopScoreId = bishopScoreId;
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

    public Integer getDilationId() {
        return dilationId;
    }

    public void setDilationId(Integer dilationId) {
        this.dilationId = dilationId;
    }

    public Integer getLengthId() {
        return lengthId;
    }

    public void setLengthId(Integer lengthId) {
        this.lengthId = lengthId;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getConsistencyId() {
        return consistencyId;
    }

    public void setConsistencyId(Integer consistencyId) {
        this.consistencyId = consistencyId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getTotalBishopScore() {
        return totalBishopScore;
    }

    public void setTotalBishopScore(Integer totalBishopScore) {
        this.totalBishopScore = totalBishopScore;
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

