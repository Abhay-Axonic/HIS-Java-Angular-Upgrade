package com.param.global.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({

        @NamedNativeQuery(
                name = "GET_INITIAL_ASSESSMENT_MAPPER_BY_ADMISSION_ID",
                query = "SELECT mapper.initial_assessment_mapper_id AS \"initialAssessmentMapperId\", " +
                        "mapper.assessment_id AS \"assessmentId\", " +
                        "mapper.assessment AS \"assessment\", " +
                        "mapper.index_id AS \"indexId\", " +
                        "mapper.assessment_key AS \"assessmentKey\", " +
                        "mapper.assessment_value_id AS \"assessmentValueId\", " +
                        "mapper.assessment_value AS \"assessmentValue\", " +
                        "mapper.save_as_draft AS \"saveAsDraft\", " +
                        "mapper.unit_id AS \"unitId\", " +
                        "mapper.organization_id AS \"organizationId\", " +
                        "mapper.status AS \"status\", " +
                        "to_char(mapper.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", " +
                        "to_char(mapper.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", " +
                        "mapper.created_by AS \"createdBy\", " +
                        "mapper.updated_by AS \"updatedBy\" " +
                        "FROM public.t_initial_assessment_mapper mapper " +
                        "WHERE mapper.status = 'A' " +
//                        "AND mapper.save_as_draft = 'Y' " +
                        "AND mapper.unit_id = :unitId " +
                        "AND mapper.organization_id = :organizationId " +
                        "AND mapper.admission_id = :admissionId " +
                        "ORDER BY mapper.initial_assessment_mapper_id DESC"
        )
})

@Entity
@Table(name = "t_initial_assessment_mapper", schema = "public")
@SequenceGenerator(name = "t_initial_assessment_mapper_seq", sequenceName = "public.t_initial_assessment_mapper_seq", allocationSize = 1)
public class InitialAssessmentICUAndERMapper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_initial_assessment_mapper_seq")
    @Column(name = "initial_assessment_mapper_id")
    private Integer initialAssessmentMapperId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "assessment_id")
    private Integer assessmentId;

    @Column(name = "assessment")
    private String assessment;

    @Column(name = "index_id")
    private Integer indexId;

    @Column(name = "assessment_key")
    private String assessmentKey;

    @Column(name = "assessment_value_id")
    private Integer assessmentValueId;

    @Column(name = "assessment_value")
    private String assessmentValue;

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
    
    @Column(name = "initial_assessment_id")
    private Integer initialAssessmentId;


    @Column(name = "start_date_time")
    private Date startDateTime;

    public Integer getInitialAssessmentMapperId() {
        return initialAssessmentMapperId;
    }

    public void setInitialAssessmentMapperId(Integer initialAssessmentMapperId) {
        this.initialAssessmentMapperId = initialAssessmentMapperId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public String getAssessmentKey() {
        return assessmentKey;
    }

    public void setAssessmentKey(String assessmentKey) {
        this.assessmentKey = assessmentKey;
    }

    public Integer getAssessmentValueId() {
        return assessmentValueId;
    }

    public void setAssessmentValueId(Integer assessmentValueId) {
        this.assessmentValueId = assessmentValueId;
    }

    public String getAssessmentValue() {
        return assessmentValue;
    }

    public void setAssessmentValue(String assessmentValue) {
        this.assessmentValue = assessmentValue;
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

	public Integer getInitialAssessmentId() {
		return initialAssessmentId;
	}

	public void setInitialAssessmentId(Integer initialAssessmentId) {
		this.initialAssessmentId = initialAssessmentId;
	}

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }
}
