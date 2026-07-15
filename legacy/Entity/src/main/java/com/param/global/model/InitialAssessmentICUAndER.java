package com.param.global.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GET_INITIAL_ASSESSMENT_LIST_BY_ADMISSION_ID",
                query = "SELECT initialAssessment.initial_assessment_id AS \"initialAssessmentId\", " +
                        "initialAssessment.encounter_id AS \"encounterId\", " +
                        "initialAssessment.admission_id AS \"admissionId\", " +
                        "initialAssessment.uhid AS \"uhid\", " +
                        "initialAssessment.initial_assessment_status_id AS \"initialAssessmentStatusId\", " +
                        "initialAssessment.infusions AS \"infusions\", " +
                        "initialAssessment.save_as_draft AS \"saveAsDraft\", " +
                        "initialAssessment.unit_id AS \"unitId\", " +
                        "initialAssessment.organization_id AS \"organizationId\", " +
                        "initialAssessment.status AS \"status\", " +
                        "to_char(initialAssessment.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", " +
                        "to_char(initialAssessment.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", " +
                        "initialAssessment.created_by AS \"createdBy\", " +
                        "initialAssessment.updated_by AS \"updatedBy\" " +
                        "FROM public.t_initial_assessment initialAssessment " +
                        "WHERE initialAssessment.status = 'A' " +
//                        "AND initialAssessment.save_as_draft = 'Y' " +
                        "AND initialAssessment.unit_id = :unitId " +
                        "AND initialAssessment.organization_id = :organizationId " +
                        "AND initialAssessment.admission_id = :admissionId " +
                        "ORDER BY initialAssessment.initial_assessment_id DESC"
        ),
        @NamedNativeQuery(
                name = "GET_INITIAL_ASSESSMENT_BY_ADMISSION_ID",
                query = "SELECT " +
                        "initialAssessment.unit_id AS \"unitId\",to_char(initialAssessment.start_date_time, 'dd/MM/yyyy HH24:MI:SS')  as \"startDateTime\", " +
                        "initialAssessment.organization_id AS \"organizationId\", " +
                        "initialAssessment.uhid AS \"uhid\", " +
                        "initialAssessment.status AS \"status\", " +
                        "initialAssessment.infusions AS \"infusions\", " +
                        "initialAssessment.save_as_draft AS \"saveAsDraft\", " +
                        "initialAssessment.initial_assessment_status_id AS \"initialAssessmentStatusId\", " +
                        "mapper.assessment_id AS \"assessmentId\", " +
                        "mapper.assessment AS \"assessment\", " +
                        "mapper.index_id AS \"indexId\", " +
                        "mapper.assessment_key AS \"assessmentKey\", " +
                        "mapper.assessment_value_id AS \"assessmentValueId\", " +
                        "mapper.assessment_value AS \"assessmentValue\", " +
                        "to_char(initialAssessment.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", " +
                        "to_char(initialAssessment.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", " +
                        "initialAssessment.created_by AS \"createdBy\", " +
                        "initialAssessment.updated_by AS \"updatedBy\" " +
                        "FROM public.t_initial_assessment initialAssessment " +
                        "INNER JOIN public.t_initial_assessment_mapper mapper " +
                        "ON initialAssessment.admission_id =  mapper.admission_id " +
                        "WHERE initialAssessment.status = 'A' " +
                        "AND initialAssessment.save_as_draft = 'Y' " +
                        "AND initialAssessment.unit_id = :unitId " +
                        "AND initialAssessment.organization_id = :organizationId " +
                        "AND initialAssessment.admission_id = :admissionId " +
                        "ORDER BY initialAssessment.initial_assessment_id DESC"
        )
})

@Entity
@Table(name = "t_initial_assessment", schema = "public")
@SequenceGenerator(name = "t_initial_assessment_seq", sequenceName = "public.t_initial_assessment_seq", allocationSize = 1)
public class InitialAssessmentICUAndER {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_initial_assessment_seq")
    @Column(name = "initial_assessment_id")
    private Integer initialAssessmentId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "uhid")
    private String uhid;

    @Column(name = "initial_assessment_status_id")
    private Integer initialAssessmentStatusId;

    @Column(name = "infusions")
    private String infusions;

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

    @Column(name = "start_date_time")
    private Date startDateTime;

    public Integer getInitialAssessmentId() {
        return initialAssessmentId;
    }

    public void setInitialAssessmentId(Integer initialAssessmentId) {
        this.initialAssessmentId = initialAssessmentId;
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

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public Integer getInitialAssessmentStatusId() {
        return initialAssessmentStatusId;
    }

    public void setInitialAssessmentStatusId(Integer initialAssessmentStatusId) {
        this.initialAssessmentStatusId = initialAssessmentStatusId;
    }

    public String getInfusions() {
        return infusions;
    }

    public void setInfusions(String infusions) {
        this.infusions = infusions;
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

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }
}
