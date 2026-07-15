package com.param.adt.mrd;

import com.param.adt.admission.model.Admission;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.global.model.BoxMaster;
import com.param.global.model.MrdFileStatus;
import com.param.global.model.MrdRackMaster;
import com.param.global.model.RowMaster;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_mrd_file_management", schema = "adt")
@SequenceGenerator(name = "mrd_file_seq", sequenceName = "adt.mrd_file_seq", allocationSize = 1)
public class MrdFileAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mrd_file_seq")
    @Column(name = "mrd_file_id")
    private Integer mrdFileId;
    @Column(name = "allocation_number")
    private String allocationNumber;
    @Column(name = "patient_id")
    private Integer patientId;
    @Column(name = "admission_id")
    private Integer admissionId;
    @Column(name = "encounter_id")
    private Integer encounterId;
    @Column(name = "rack_id")
    private Integer rackId;
    @Column(name = "box_id")
    private Integer boxId;
    @Column(name = "row_id")
    private Integer rowId;
    @Column(name = "status_id")
    private Integer statusId;
    @Column(name = "file_no")
    private String fileNo;
    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "requested_by")
    private Integer requestedBy;

    @Column(name = "accepted_by")
    private Integer acceptedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "organization_id")
    private Integer organizationId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
    private Admission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
    private Encounter encounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
    private PatientRegistration patientRegistration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id", insertable = false, nullable = false, updatable = false)
    private MrdRackMaster rackMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id", insertable = false, nullable = false, updatable = false)
    private BoxMaster boxMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "row_id", insertable = false, nullable = false, updatable = false)
    private RowMaster rowMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", insertable = false, nullable = false, updatable = false)
    private MrdFileStatus fileStatus;

    public int getMrdFileId() {
        return mrdFileId;
    }

    public void setMrdFileId(int mrdFileId) {
        this.mrdFileId = mrdFileId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public Integer getRackId() {
        return rackId;
    }

    public void setRackId(Integer rackId) {
        this.rackId = rackId;
    }

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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

    public Admission getAdmission() {
        return admission;
    }

    public void setAdmission(Admission admission) {
        this.admission = admission;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public PatientRegistration getPatientRegistration() {
        return patientRegistration;
    }

    public void setPatientRegistration(PatientRegistration patientRegistration) {
        this.patientRegistration = patientRegistration;
    }

    public MrdRackMaster getRackMaster() {
        return rackMaster;
    }

    public void setRackMaster(MrdRackMaster rackMaster) {
        this.rackMaster = rackMaster;
    }

    public BoxMaster getBoxMaster() {
        return boxMaster;
    }

    public void setBoxMaster(BoxMaster boxMaster) {
        this.boxMaster = boxMaster;
    }

    public RowMaster getRowMaster() {
        return rowMaster;
    }

    public void setRowMaster(RowMaster rowMaster) {
        this.rowMaster = rowMaster;
    }

    public MrdFileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(MrdFileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public void setMrdFileId(Integer mrdFileId) {
        this.mrdFileId = mrdFileId;
    }

    public String getAllocationNumber() {
        return allocationNumber;
    }

    public void setAllocationNumber(String allocationNumber) {
        this.allocationNumber = allocationNumber;
    }

    public Integer getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Integer requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Integer getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(Integer acceptedBy) {
        this.acceptedBy = acceptedBy;
    }
}
