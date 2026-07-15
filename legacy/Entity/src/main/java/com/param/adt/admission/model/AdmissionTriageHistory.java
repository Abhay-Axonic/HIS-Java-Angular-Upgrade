package com.param.adt.admission.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_admission_triage_history", schema = "adt")
@SequenceGenerator(
        name = "admission_triage_history_seq",
        sequenceName = "adt.admission_triage_history_seq",
        allocationSize = 1
)
public class AdmissionTriageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admission_triage_history_seq")
    @Column(name = "triage_history_id")
    private Integer triageHistoryId;


    @Column(name = "admission_details_id", nullable = false)
    private Integer admissionDetailsId;

    @Column(name = "current_triage_id", nullable = false)
    private Integer currentTriageId;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_details_id", insertable = false, updatable = false)
    private AdmissionDetails admissionDetails;

    public Integer getTriageHistoryId() {
        return triageHistoryId;
    }

    public void setTriageHistoryId(Integer triageHistoryId) {
        this.triageHistoryId = triageHistoryId;
    }

    public Integer getAdmissionDetailsId() {
        return admissionDetailsId;
    }

    public void setAdmissionDetailsId(Integer admissionDetailsId) {
        this.admissionDetailsId = admissionDetailsId;
    }

    public Integer getCurrentTriageId() {
        return currentTriageId;
    }

    public void setCurrentTriageId(Integer currentTriageId) {
        this.currentTriageId = currentTriageId;
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
