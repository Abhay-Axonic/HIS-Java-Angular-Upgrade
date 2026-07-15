package com.param.global.unit.dto;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class InitialAssessmentICUAndERDTO {

    private Integer initialAssessmentId;

    private Integer encounterId;

    private Integer admissionId;

    private String uhid;

    private Integer initialAssessmentStatusId;

    private List<InitialAssessmentICUAndERMapperDTO> selectedKeyValueList;

    private String infusions;

    private Character saveAsDraft;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    private String  startDateTime;


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

    public List<InitialAssessmentICUAndERMapperDTO> getSelectedKeyValueList() {
        return selectedKeyValueList;
    }

    public void setSelectedKeyValueList(List<InitialAssessmentICUAndERMapperDTO> selectedKeyValueList) {
        this.selectedKeyValueList = selectedKeyValueList;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }
}
