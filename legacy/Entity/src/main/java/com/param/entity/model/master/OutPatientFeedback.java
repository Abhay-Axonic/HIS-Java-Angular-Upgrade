package com.param.entity.model.master;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_OUT_PATIENT_FEEDBACK_BY_PATIENT_ID",
                query = "SELECT outPatientFeedback.out_patient_feedback_id AS \"outPatientFeedbackId\", "
                        + " outPatientFeedback.patient_id AS \"patientId\", "
                        + " outPatientFeedback.visit_type AS \"visitType\", "
                        + " outPatientFeedback.visit_type_id AS \"visitTypeId\", "
                        + " outPatientFeedback.feedback_key AS \"feedbackKey\", "
                        + " outPatientFeedback.feedback_value AS \"feedbackValue\", "
                        + " outPatientFeedback.unit_id AS \"unitId\", "
                        + " outPatientFeedback.organization_id AS \"organizationId\", "
                        + " outPatientFeedback.status AS \"status\", "
                        + " to_char(outPatientFeedback.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(outPatientFeedback.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM public.out_patient_feedback outPatientFeedback "
                        + " WHERE outPatientFeedback.status = 'A' "
                        + " AND outPatientFeedback.unit_id = :unitId "
                        + " AND outPatientFeedback.organization_id = :organizationId "
                        + " AND outPatientFeedback.patient_id = :patientId "
                        + " AND outPatientFeedback.visit_type = :visitType "
                        + " AND outPatientFeedback.visit_type_id = :visitTypeId "
                        + " ORDER BY outPatientFeedback.out_patient_feedback_id DESC"
        )
})


@Entity
@Table(name = "out_patient_feedback", schema = "public")
@SequenceGenerator(name = "out_patient_feedback_seq", sequenceName = "public.out_patient_feedback_seq", allocationSize = 1)
public class OutPatientFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "out_patient_feedback_seq")
    @Column(name = "out_patient_feedback_id")
    private Integer outPatientFeedbackId;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "visit_type")
    private String visitType;

    @Column(name = "visit_type_id")
    private Integer visitTypeId;

    @Column(name = "feedback_key")
    private Integer feedbackKey;

    @Column(name = "feedback_value")
    private String feedbackValue;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "status")
    private Character status;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;
    
    @Column(name = "encounter_id")
    private Integer encounterId;

    public Integer getOutPatientFeedbackId() {
        return outPatientFeedbackId;
    }

    public void setOutPatientFeedbackId(Integer outPatientFeedbackId) {
        this.outPatientFeedbackId = outPatientFeedbackId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Integer getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(Integer visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public Integer getFeedbackKey() {
        return feedbackKey;
    }

    public void setFeedbackKey(Integer feedbackKey) {
        this.feedbackKey = feedbackKey;
    }

    public String getFeedbackValue() {
        return feedbackValue;
    }

    public void setFeedbackValue(String feedbackValue) {
        this.feedbackValue = feedbackValue;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
    
    
}
