package com.param.global.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GET_ANESTHESIA_ASSESSMENT_LIST_BY_PATIENT_ID",
                query = "SELECT anesthesiaAssessment.anesthesia_assessment_id AS \"anesthesiaAssessmentId\", " +
                        "anesthesiaAssessment.patient_id AS \"patientId\", " +
                        "anesthesiaAssessment.is_previous_surgery AS \"isPreviousSurgery\", " +
                        "anesthesiaAssessment.selected_assessment AS \"selectedAssessment\", " +
                        "anesthesiaAssessment.anesthesia_plan AS \"anesthesiaPlan\", " +
                        "anesthesiaAssessment.save_as_draft AS \"saveAsDraft\", " +
                        "anesthesiaAssessment.unit_id AS \"unitId\", " +
                        "anesthesiaAssessment.organization_id AS \"organizationId\", " +
                        "anesthesiaAssessment.status AS \"status\", " +
                        "to_char(anesthesiaAssessment.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", " +
                        "to_char(anesthesiaAssessment.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", " +
                        "anesthesiaAssessment.created_by AS \"createdBy\", " +
                        "anesthesiaAssessment.updated_by AS \"updatedBy\", " +
                        "anesthesiaAssessment.previous_surgery AS \"previousSurgeryDesc\" " +
                        "FROM public.t_anesthesia_assessments anesthesiaAssessment " +
                        "WHERE anesthesiaAssessment.status = 'A' " +
                        "AND anesthesiaAssessment.save_as_draft = 'Y' " +
                        "AND anesthesiaAssessment.unit_id = :unitId " +
                        "AND anesthesiaAssessment.organization_id = :organizationId " +
                        "AND anesthesiaAssessment.patient_id = :patientId " +
                        "ORDER BY anesthesiaAssessment.anesthesia_assessment_id DESC"
        )
})


@Entity
@Table(name = "t_anesthesia_assessments", schema = "public")
@SequenceGenerator(name = "t_anesthesia_assessments_seq", sequenceName = "public.t_anesthesia_assessments_seq", allocationSize = 1)
public class AnesthesiaAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_anesthesia_assessments_seq")
    @Column(name = "anesthesia_assessment_id")
    private Integer anesthesiaAssessmentId;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "is_previous_surgery")
    private Integer isPreviousSurgery;

    @Column(name = "selected_assessment")
    private String selectedAssessment;

    @Column(name = "anesthesia_plan")
    private String anesthesiaPlan;

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
    
    @Column(name = "encounter_id")
    private Integer encounterId;
    
    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "previous_surgery")
    private String previousSurgery;

    public Integer getAnesthesiaAssessmentId() {
        return anesthesiaAssessmentId;
    }

    public void setAnesthesiaAssessmentId(Integer anesthesiaAssessmentId) {
        this.anesthesiaAssessmentId = anesthesiaAssessmentId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getIsPreviousSurgery() {
        return isPreviousSurgery;
    }

    public void setIsPreviousSurgery(Integer isPreviousSurgery) {
        this.isPreviousSurgery = isPreviousSurgery;
    }

    public String getSelectedAssessment() {
        return selectedAssessment;
    }

    public void setSelectedAssessment(String selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
    }

    public String getAnesthesiaPlan() {
        return anesthesiaPlan;
    }

    public void setAnesthesiaPlan(String anesthesiaPlan) {
        this.anesthesiaPlan = anesthesiaPlan;
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

    public String getPreviousSurgery() {
        return previousSurgery;
    }

    public void setPreviousSurgery(String previousSurgery) {
        this.previousSurgery = previousSurgery;
    }
}
