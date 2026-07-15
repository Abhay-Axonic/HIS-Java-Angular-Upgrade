package com.param.adt.admission.model;


import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_NUTRITIONAL_ASSESSMENT_SCORE_BY_ADMISSION_ID",
                query = "SELECT nutritionalAssessmentScore.nutritional_assessment_score_id AS \"nutritionalAssessmentScoreId\", "
                        + " nutritionalAssessmentScore.admission_id AS \"admissionId\", "
                        + " nutritionalAssessmentScore.encounter_id AS \"encounterId\", "
                        /*+ " nutritionalAssessmentScore.patient_weight AS \"patientWeight\", "
                        + " nutritionalAssessmentScore.patient_height AS \"patientHeight\", "
                        + " nutritionalAssessmentScore.patient_bmi AS \"patientBmi\", "
                        + " nutritionalAssessmentScore.patient_knee_height AS \"patientKneeHeight\", "*/
                        + " nutritionalAssessmentScore.nutritional_assessment_name AS \"nutritionalAssessmentName\", "
                       /* + " nutritionalAssessmentScore.total_nutritional_assessment_score AS \"totalNutritionalAssessmentScore\", "*/
                        + " nutritionalAssessmentScore.nutritional_assessment_id AS \"nutritionalAssessmentId\", "
                        + " nutritionalAssessmentScore.nutritional_assessment_name_id AS \"nutritionalAssessmentNameId\", "
                        + " nutritionalAssessmentScore.nutritional_assessment_name_point AS \"nutritionalAssessmentNamePoint\", "
                        + " nutritionalAssessmentScore.consumption_marker_flag AS \"consumptionMarkerFlag\", "
                        + " nutritionalAssessmentScore.save_as_draft AS \"saveAsDraft\", "
                        + " nutritionalAssessmentScore.unit_id AS \"unitId\", "
                        + " nutritionalAssessmentScore.organization_id AS \"organizationId\", "
                        + " nutritionalAssessmentScore.status AS \"status\", "
                        + " to_char(nutritionalAssessmentScore.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(nutritionalAssessmentScore.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", "
                        + " nutritionalAssessmentScore.created_by AS \"createdBy\", "
                        + " nutritionalAssessmentScore.updated_by AS \"updatedBy\" ,"
                        + " nutritionalAssessmentScore.nutritional_assessment_score_master_id as \"nutritionalAssessmentScoreMasterId\" "
                        + " FROM adt.t_nutritional_assessment_score nutritionalAssessmentScore "
                        + " WHERE nutritionalAssessmentScore.status= 'A'"
                        + " AND nutritionalAssessmentScore.save_as_draft= 'Y'"
                        + " AND nutritionalAssessmentScore.unit_id= :unitId"
                        + " AND nutritionalAssessmentScore.organization_id= :organizationId"
                        + " AND nutritionalAssessmentScore.admission_id= :admissionId"
                        + " ORDER BY nutritionalAssessmentScore.nutritional_assessment_score_id desc"
        )
})


@Entity
@Table(name = "t_nutritional_assessment_score", schema = "adt")
@SequenceGenerator(name = "t_nutritional_assessment_score_seq", sequenceName = "adt.t_nutritional_assessment_score_seq", allocationSize = 1)
public class NutritionalAssessmentScore {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_nutritional_assessment_score_seq")
    @Column(name = "nutritional_assessment_score_id")
    private Integer nutritionalAssessmentScoreId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "nutritional_assessment_name")
    private String nutritionalAssessmentName;

    @Column(name = "nutritional_assessment_id")
    private Integer nutritionalAssessmentId;

    @Column(name = "nutritional_assessment_name_id")
    private Integer nutritionalAssessmentNameId;

    @Column(name = "nutritional_assessment_name_point")
    private Double nutritionalAssessmentNamePoint;

    @Column(name = "save_as_draft")
    private Character saveAsDraft;

    @Column(name = "consumption_marker_flag")
    private Integer consumptionMarkerFlag;

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
    
    @Column(name = "nutritional_assessment_score_master_id")
    private Integer nutritionalAssessmentScoreMasterId;

	public Integer getNutritionalAssessmentScoreId() {
		return nutritionalAssessmentScoreId;
	}

	public void setNutritionalAssessmentScoreId(Integer nutritionalAssessmentScoreId) {
		this.nutritionalAssessmentScoreId = nutritionalAssessmentScoreId;
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

	public String getNutritionalAssessmentName() {
		return nutritionalAssessmentName;
	}

	public void setNutritionalAssessmentName(String nutritionalAssessmentName) {
		this.nutritionalAssessmentName = nutritionalAssessmentName;
	}

	public Integer getNutritionalAssessmentId() {
		return nutritionalAssessmentId;
	}

	public void setNutritionalAssessmentId(Integer nutritionalAssessmentId) {
		this.nutritionalAssessmentId = nutritionalAssessmentId;
	}

	public Integer getNutritionalAssessmentNameId() {
		return nutritionalAssessmentNameId;
	}

	public void setNutritionalAssessmentNameId(Integer nutritionalAssessmentNameId) {
		this.nutritionalAssessmentNameId = nutritionalAssessmentNameId;
	}

	public Double getNutritionalAssessmentNamePoint() {
		return nutritionalAssessmentNamePoint;
	}

	public void setNutritionalAssessmentNamePoint(Double nutritionalAssessmentNamePoint) {
		this.nutritionalAssessmentNamePoint = nutritionalAssessmentNamePoint;
	}

	public Character getSaveAsDraft() {
		return saveAsDraft;
	}

	public void setSaveAsDraft(Character saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}

	public Integer getConsumptionMarkerFlag() {
		return consumptionMarkerFlag;
	}

	public void setConsumptionMarkerFlag(Integer consumptionMarkerFlag) {
		this.consumptionMarkerFlag = consumptionMarkerFlag;
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

	public Integer getNutritionalAssessmentScoreMasterId() {
		return nutritionalAssessmentScoreMasterId;
	}

	public void setNutritionalAssessmentScoreMasterId(Integer nutritionalAssessmentScoreMasterId) {
		this.nutritionalAssessmentScoreMasterId = nutritionalAssessmentScoreMasterId;
	}

   
}
