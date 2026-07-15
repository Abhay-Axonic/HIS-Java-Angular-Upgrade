package com.param.entity.model.master;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_IN_PATIENT_FEEDBACK_BY_PATIENT_ID",
                query = "SELECT inPatientFeedback.in_patient_feedback_id AS \"inPatientFeedbackId\", "
                        + " inPatientFeedback.patient_id AS \"patientId\", "
                        + " inPatientFeedback.visit_type AS \"visitType\", "
                        + " inPatientFeedback.visit_type_id AS \"visitTypeId\", "
                        + " inPatientFeedback.feedback_key AS \"feedbackKey\", "
                        + " inPatientFeedback.feedback_value AS \"feedbackValue\", "
                        + " inPatientFeedback.unit_id AS \"unitId\", "
                        + " inPatientFeedback.organization_id AS \"organizationId\", "
                        + " inPatientFeedback.status AS \"status\", "
                        + " to_char(inPatientFeedback.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(inPatientFeedback.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\" "
                        + " FROM public.in_patient_feedback inPatientFeedback "
                        + " WHERE inPatientFeedback.status = 'A' "
                        + " AND inPatientFeedback.unit_id = :unitId "
                        + " AND inPatientFeedback.organization_id = :organizationId "
                        + " AND inPatientFeedback.patient_id = :patientId "
                        + " AND inPatientFeedback.visit_type = :visitType "
                        + " AND inPatientFeedback.visit_type_id = :visitTypeId "
                        + " ORDER BY inPatientFeedback.in_patient_feedback_id DESC"
        )
})


@Entity
@Table(name = "in_patient_feedback", schema = "public")
@SequenceGenerator(name = "in_patient_feedback_seq", sequenceName = "public.in_patient_feedback_seq", allocationSize = 1)
public class InPatientFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "in_patient_feedback_seq")
    @Column(name = "in_patient_feedback_id")
    private Integer inPatientFeedbackId;

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

    @Column(name = "admission_id")
    private Integer admissionId;
    
    public Integer getInPatientFeedbackId() {
        return inPatientFeedbackId;
    }

    public void setInPatientFeedbackId(Integer inPatientFeedbackId) {
        this.inPatientFeedbackId = inPatientFeedbackId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
    
}
