package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;


//@NamedNativeQueries({
//        @NamedNativeQuery(name="GET_NURSING_SCALE_SCORE_BY_ADMISSION_ID",
//                query="SELECT nursingScaleScore.nursing_scale_score_id AS \"nursingScaleScoreId\", nursingScaleScore.admission_id AS \"admissionId\", "
//                        + " nursingScaleScore.nursing_scale_total_score AS \"nursingScaleTotalScore\", nursingScaleScore.save_as_draft AS \"saveAsDraft\", "
//                        + " nursingScaleScore.nursing_score_name AS \"nursingScoreName\", nursingScaleScore.tab_number AS \"tabNumber\", "
//                        + " nursingScaleScore.unit_id AS \"unitId\", nursingScaleScore.organization_id AS \"organizationId\", "
//                        + " nursingScaleScore.created_by AS \"createdBy\", nursingScaleScore.updated_by AS \"updatedBy\", "
//                        + " nursingScaleScore.encounter_id AS \"encounterId\", nursingScaleScore.status AS \"status\", "
//                        + " to_char(nursingScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
//                        + " to_char(nursingScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
//                        + " FROM adt.t_nursing_scale_score nursingScaleScore "
//                        + " WHERE nursingScaleScore.status= 'A'"
//                        + " AND nursingScaleScore.unit_id= :unitId"
//                        + " AND nursingScaleScore.organization_id= :organizationId"
//        )
//})


@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GET_LATEST_NURSING_SCALE_SCORE_BY_ADMISSION_ID",
                query = "SELECT " +
                        "nursingScaleScore.nursing_scale_score_id AS \"nursingScaleScoreId\", " +
                        "nursingScaleScore.admission_id AS \"admissionId\", " +
                        "nursingScaleScore.nursing_scale_total_score AS \"nursingScaleTotalScore\", " +
                        "nursingScaleScore.save_as_draft AS \"saveAsDraft\", " +
                        "nursingScaleScore.nursing_score_name AS \"nursingScoreName\", " +
                        "nursingScaleScore.tab_number AS \"tabNumber\", " +
                        "nursingScaleScore.unit_id AS \"unitId\", " +
                        "nursingScaleScore.organization_id AS \"organizationId\", " +
                        "nursingScaleScore.created_by AS \"createdBy\", " +
                        "nursingScaleScore.updated_by AS \"updatedBy\", " +
                        "nursingScaleScore.encounter_id AS \"encounterId\", " +
                        "nursingScaleScore.status AS \"status\", " +
                        "nursingScaleScore.score_with_3 AS \"IsScoreWith3\", " +
                        "to_char(nursingScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", " +
                        "to_char(nursingScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" " +
                        "FROM ( " +
                        "   SELECT *, ROW_NUMBER() OVER (PARTITION BY tab_number ORDER BY created_date DESC) AS rn " +
                        "   FROM adt.t_nursing_scale_score " +
                        "   WHERE admission_id = :admissionId AND unit_id = :unitId AND organization_id = :organizationId " +
                        ") AS nursingScaleScore " +
                        "WHERE rn = 1;"
        ),

        @NamedNativeQuery(
                name = "GET_ALL_NURSING_SCALE_SCORE_BY_ADMISSION_ID",
                query = "SELECT " +
                        "nursingScaleScore.nursing_scale_score_id AS \"nursingScaleScoreId\", " +
                        "nursingScaleScore.admission_id AS \"admissionId\", " +
                        "nursingScaleScore.nursing_scale_total_score AS \"nursingScaleTotalScore\", " +
                        "nursingScaleScore.save_as_draft AS \"saveAsDraft\", " +
                        "nursingScaleScore.nursing_score_name AS \"nursingScoreName\", " +
                        "nursingScaleScore.tab_number AS \"tabNumber\", " +
                        "nursingScaleScore.unit_id AS \"unitId\", " +
                        "nursingScaleScore.organization_id AS \"organizationId\", " +
                        "nursingScaleScore.created_by AS \"createdBy\", " +
                        "nursingScaleScore.updated_by AS \"updatedBy\", " +
                        "nursingScaleScore.encounter_id AS \"encounterId\", " +
                        "nursingScaleScore.status AS \"status\", " +
                        "to_char(nursingScaleScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", " +
                        "to_char(nursingScaleScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM adt.t_nursing_scale_score nursingScaleScore "
                        + " WHERE nursingScaleScore.status= 'A'"
                        + " AND nursingScaleScore.save_as_draft= 'N'"
                        + " AND nursingScaleScore.unit_id= :unitId"
                        + " AND nursingScaleScore.organization_id= :organizationId"
                        + " AND nursingScaleScore.admission_id= :admissionId"
                        + " AND nursingScaleScore.tab_number= :tabNumber"
        ),

        @NamedNativeQuery(
                name = "GET_SPECIALITY_MAPPED_NURSING_SCORE",
                query = """
                    SELECT
                        DISTINCT ON (mapper.nursing_score_id)
                        mapper.nursing_score_id AS "nursingScaleScoreId",
                        mapper.speciality_id AS "specialityId",
                        nursingScore.nursing_score_name AS "nursingScoreName",
                        nursingScore.nursing_score_code AS "nursingScoreCode",
                        mapper.speciality_nursing_score_mapper_id AS "specialityNursingScoreMapperId",
                        mapper.unit_id AS "unitId",
                        mapper.organization_id AS "organizationId",
                        mapper.created_by AS "createdBy",
                        mapper.updated_by AS "updatedBy",
                        mapper.status AS "status"
                        FROM public.m_nursing_score_master nursingScore
                        INNER JOIN public.t_speciality_nursing_score_mapper mapper ON
                          mapper.nursing_score_id = nursingScore.nursing_score_id
                        WHERE nursingScore.status= 'A'
                        AND mapper.status= 'A'
                        AND mapper.unit_id= :unitId
                        AND mapper.organization_id= :organizationId
                        AND mapper.speciality_id in (:specialityIds)
                        order by mapper.nursing_score_id asc
                    """
        )
        
})



@Entity
@Table(name = "t_nursing_scale_score", schema = "adt")
@SequenceGenerator(name = "t_t_nursing_scale_score_seq", sequenceName = "adt.t_t_nursing_scale_score_seq", allocationSize = 1)
public class NursingScaleScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_t_nursing_scale_score_seq")
    @Column(name = "nursing_scale_score_id")
    private Integer nursingScaleScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "nursing_score_name")
    private String nursingScoreName;

    @Column(name = "nursing_scale_total_score")
    private String nursingScaleTotalScore;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "tab_number")
    private Integer tabNumber;

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
    
    @Column(name = "score_with_3")
    private Boolean IsScoreWith3;

    public Integer getNursingScaleScoreId() {
        return nursingScaleScoreId;
    }

    public void setNursingScaleScoreId(Integer nursingScaleScoreId) {
        this.nursingScaleScoreId = nursingScaleScoreId;
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

    public String getNursingScoreName() {
        return nursingScoreName;
    }

    public void setNursingScoreName(String nursingScoreName) {
        this.nursingScoreName = nursingScoreName;
    }

    public String getNursingScaleTotalScore() {
        return nursingScaleTotalScore;
    }

    public void setNursingScaleTotalScore(String nursingScaleTotalScore) {
        this.nursingScaleTotalScore = nursingScaleTotalScore;
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

    public Integer getTabNumber() {
        return tabNumber;
    }

    public void setTabNumber(Integer tabNumber) {
        this.tabNumber = tabNumber;
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

	public Boolean getIsScoreWith3() {
		return IsScoreWith3;
	}

	public void setIsScoreWith3(Boolean isScoreWith3) {
		IsScoreWith3 = isScoreWith3;
	}

}

