package com.param.billing.patientType.dto;

import java.util.List;

public class FranchiseShareDto {

    private Integer franchiseUnitId;

    private String code;

    private String unitName;

    private String description;

    private Integer organizationId;

    private Integer unitId;

    private String createdDate;

    private String updatedDate;

    private Integer createdBy;

    private Integer updatedBy;

    private Character status;

    private List<FranchiseShareDetailsDto> frShareDetailsList;

    public Integer getFranchiseUnitId() {
        return franchiseUnitId;
    }

    public void setFranchiseUnitId(Integer franchiseUnitId) {
        this.franchiseUnitId = franchiseUnitId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public List<FranchiseShareDetailsDto> getFrShareDetailsList() {
        return frShareDetailsList;
    }

    public void setFrShareDetailsList(List<FranchiseShareDetailsDto> frShareDetailsList) {
        this.frShareDetailsList = frShareDetailsList;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
