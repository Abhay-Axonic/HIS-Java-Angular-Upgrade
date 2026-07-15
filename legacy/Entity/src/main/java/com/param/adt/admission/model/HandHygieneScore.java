package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_HAND_HYGIENE_SCORE_BY_WARD_ID",
                query = "SELECT handHygieneScore.hand_hygiene_score_id AS \"handHygieneScoreId\", "
                        + " handHygieneScore.ward_id AS \"wardId\", "
                        + " handHygieneScore.question_id AS \"questionId\", "
                        + " handHygieneScore.option_id AS \"optionId\", "
                        + " handHygieneScore.save_as_draft AS \"saveAsDraft\", "
                        + " handHygieneScore.unit_id AS \"unitId\", "
                        + " handHygieneScore.organization_id AS \"organizationId\", "
                        + " handHygieneScore.status AS \"status\", "
                        + " to_char(handHygieneScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(handHygieneScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", "
                        + " handHygieneScore.created_by AS \"createdBy\", "
                        + " handHygieneScore.updated_by AS \"updatedBy\" "
                        + " FROM adt.t_hand_hygiene_score handHygieneScore "
                        + " WHERE handHygieneScore.status = 'A'"
                        + " AND handHygieneScore.save_as_draft = 'Y'"
                        + " AND handHygieneScore.unit_id = :unitId"
                        + " AND handHygieneScore.organization_id = :organizationId"
                        + " AND handHygieneScore.ward_id = :wardId"
                        + " ORDER BY handHygieneScore.hand_hygiene_score_id DESC"
        )
})


@Entity
@Table(name = "t_hand_hygiene_score", schema = "adt")
@SequenceGenerator(name = "t_hand_hygiene_score_seq", sequenceName = "adt.t_hand_hygiene_score_seq", allocationSize = 1)
public class HandHygieneScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_hand_hygiene_score_seq")
    @Column(name = "hand_hygiene_score_id")
    private Integer handHygieneScoreId;

    @Column(name = "ward_id")
    private Integer wardId;

    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "option_id")
    private Integer optionId;

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

    public Integer getHandHygieneScoreId() {
        return handHygieneScoreId;
    }

    public void setHandHygieneScoreId(Integer handHygieneScoreId) {
        this.handHygieneScoreId = handHygieneScoreId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
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
