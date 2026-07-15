package com.param.mrd.dto;

public class SearchNoResponseDto {
    private Integer admissionId;
    private String admissionNo;
    private Integer encounterId;
    private String encounterNo;
    private Integer visitTypeId;

    public Integer getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(Integer visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public Integer getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(Integer encounterId) {
        this.encounterId = encounterId;
    }

    public String getEncounterNo() {
        return encounterNo;
    }

    public void setEncounterNo(String encounterNo) {
        this.encounterNo = encounterNo;
    }
}
