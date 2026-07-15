package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name="GET_DRAFTED_MALLAMPATTI_SCORES_BY_ID",
                query="SELECT tms.mallampatti_score_id AS \"mallampattiScoreId\", tms.admission_id AS \"admissionId\", "
                        + " tms.total_mallampatti_score AS \"totalMallampattiScore\", tms.save_as_draft AS \"saveAsDraft\", "
                        + " tms.mallampatti_class_id AS \"mallampattiClassId\", tms.final_save AS \"finalSave\", "
                        + " tms.unit_id AS \"unitId\", tms.organization_id AS \"organizationId\", "
                        + " tms.created_by AS \"createdBy\", tms.updated_by AS \"updatedBy\", "
                        + " tms.encounter_id AS \"encounterId\", tms.status AS \"status\", "
                        + " to_char(tms.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(tms.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_mallampatti_score tms "
                        + " WHERE tms.status= 'A' "
                        + " AND tms.final_save= 'N' "
                        + " AND tms.unit_id= :unitId"
                        + " AND tms.organization_id= :organizationId"
        )
})


@Entity
@Table(name = "t_mallampatti_score", schema = "adt")
@SequenceGenerator(name = "t_mallampatti_score_seq", sequenceName = "adt.t_mallampatti_score_seq", allocationSize = 1)
public class MallamapattiClass {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_mallampatti_score_seq")
    @Column(name = "mallampatti_score_id")
    private Integer mallampattiScoreId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "mallampatti_class_id")
    private Integer mallampattiClassId;

    @Column(name = "total_mallampatti_score")
    private Integer totalMallampattiScore;

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

    public Integer getMallampattiScoreId() {
        return mallampattiScoreId;
    }

    public void setMallampattiScoreId(Integer mallampattiScoreId) {
        this.mallampattiScoreId = mallampattiScoreId;
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

    public Integer getMallampattiClassId() {
        return mallampattiClassId;
    }

    public void setMallampattiClassId(Integer mallampattiClassId) {
        this.mallampattiClassId = mallampattiClassId;
    }

    public Integer getTotalMallampattiScore() {
        return totalMallampattiScore;
    }

    public void setTotalMallampattiScore(Integer totalMallampattiScore) {
        this.totalMallampattiScore = totalMallampattiScore;
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
