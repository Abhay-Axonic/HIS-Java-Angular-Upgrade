package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "speciality_triage", schema = "adt")
@SequenceGenerator(
        name = "speciality_triage_seq",
        sequenceName = "adt.speciality_triage_seq",
        allocationSize = 1
)
public class SpecialityTriage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "speciality_triage_seq")
    @Column(name = "speciality_triage_id")
    private Integer specialityTriageId;

    @Column(name = "admission_details_id", nullable = false)
    private Integer admissionDetailsId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name = "speciality_triage", nullable = false)
    private String specialityTriage; // PAEDIATRIC / OBSTETRIC


    @Column(name = "triage_details")
    private String triageDetails;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    // 🔗 Mapping with AdmissionDetails
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_details_id", insertable = false, updatable = false)
    private AdmissionDetails admissionDetails;


    public Integer getSpecialityTriageId() {
        return specialityTriageId;
    }

    public void setSpecialityTriageId(Integer specialityTriageId) {
        this.specialityTriageId = specialityTriageId;
    }

    public Integer getAdmissionDetailsId() {
        return admissionDetailsId;
    }

    public void setAdmissionDetailsId(Integer admissionDetailsId) {
        this.admissionDetailsId = admissionDetailsId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public String getSpecialityTriage() {
        return specialityTriage;
    }

    public void setSpecialityTriage(String specialityTriage) {
        this.specialityTriage = specialityTriage;
    }



    public String getTriageDetails() {
        return triageDetails;
    }

    public void setTriageDetails(String triageDetails) {
        this.triageDetails = triageDetails;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    public AdmissionDetails getAdmissionDetails() {
        return admissionDetails;
    }

    public void setAdmissionDetails(AdmissionDetails admissionDetails) {
        this.admissionDetails = admissionDetails;
    }
}
