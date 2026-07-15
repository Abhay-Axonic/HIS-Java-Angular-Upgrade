package com.param.mrd.dto;

public class MrdFileListResponseDto {
    private Integer allocationId;
    private String allocationNumber;

    private Integer rackId;

    private Integer rowId;
    private Integer boxId;
    private String visitType;
    private String admissionEncounterNumber;
    private String rackDescription;
    private String boxDescription;
    private String rowDescription;
    private String fileNo;
    private String fileStatus;
    private Character status;
    private String admissionEncounterDate;
    private String dischargeDate;
    private Integer encounterId;

    private Integer admissionId;

    private Integer patientId;

    private String statusNo;

    private String createdDate;
    private String requestedBy;
    private String acceptedBy;
    private String requestedNo;
    private String issuedNo;

    private Integer requestedById;

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getRequestedNo() {
        return requestedNo;
    }

    public void setRequestedNo(String requestedNo) {
        this.requestedNo = requestedNo;
    }

    public String getIssuedNo() {
        return issuedNo;
    }

    public void setIssuedNo(String issuedNo) {
        this.issuedNo = issuedNo;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public Integer getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Integer allocationId) {
        this.allocationId = allocationId;
    }

    public String getAllocationNumber() {
        return allocationNumber;
    }

    public void setAllocationNumber(String allocationNumber) {
        this.allocationNumber = allocationNumber;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getAdmissionEncounterNumber() {
        return admissionEncounterNumber;
    }

    public void setAdmissionEncounterNumber(String admissionEncounterNumber) {
        this.admissionEncounterNumber = admissionEncounterNumber;
    }

    public Integer getRackId() {
        return rackId;
    }

    public void setRackId(Integer rackId) {
        this.rackId = rackId;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public String getRackDescription() {
        return rackDescription;
    }

    public void setRackDescription(String rackDescription) {
        this.rackDescription = rackDescription;
    }

    public String getBoxDescription() {
        return boxDescription;
    }

    public void setBoxDescription(String boxDescription) {
        this.boxDescription = boxDescription;
    }

    public String getRowDescription() {
        return rowDescription;
    }

    public void setRowDescription(String rowDescription) {
        this.rowDescription = rowDescription;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getAdmissionEncounterDate() {
        return admissionEncounterDate;
    }

    public void setAdmissionEncounterDate(String admissionEncounterDate) {
        this.admissionEncounterDate = admissionEncounterDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
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

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getStatusNo() {
        return statusNo;
    }

    public void setStatusNo(String statusNo) {
        this.statusNo = statusNo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getRequestedById() {
        return requestedById;
    }

    public void setRequestedById(Integer requestedById) {
        this.requestedById = requestedById;
    }
}
