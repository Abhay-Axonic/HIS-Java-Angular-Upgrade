package com.param.adt.admission.dto;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class LaboratoryInspectionScoreDTO {

    private Integer laboratoryInspectionId;

    private Integer departmentId;

    private List<LaboratoryInspectionListDTO> labInspectionSelectedList;

    private String labInspectionDate;

    private Character saveAsDraft;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    public Integer getLaboratoryInspectionId() {
        return laboratoryInspectionId;
    }

    public void setLaboratoryInspectionId(Integer laboratoryInspectionId) {
        this.laboratoryInspectionId = laboratoryInspectionId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public List<LaboratoryInspectionListDTO> getLabInspectionSelectedList() {
        return labInspectionSelectedList;
    }

    public void setLabInspectionSelectedList(List<LaboratoryInspectionListDTO> labInspectionSelectedList) {
        this.labInspectionSelectedList = labInspectionSelectedList;
    }

    public String getLabInspectionDate() {
        return labInspectionDate;
    }

    public void setLabInspectionDate(String labInspectionDate) {
        this.labInspectionDate = labInspectionDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}
