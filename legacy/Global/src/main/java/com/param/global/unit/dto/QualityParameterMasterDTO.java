package com.param.global.unit.dto;

import javax.persistence.Column;
import java.util.Date;

public class QualityParameterMasterDTO {

    private Integer qualityParameterMasterId;

    private Integer moduleId;

    private String moduleName;

    private Integer activityId;

    private String activityName;

    private Integer parameterId;

    private Character update;

    private String standard;

    private Integer uomId;

    private String uomDesc;

    private Integer thresholdPercentage;

    private Integer targetPercentage;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;

    public Integer getQualityParameterMasterId() {
        return qualityParameterMasterId;
    }

    public void setQualityParameterMasterId(Integer qualityParameterMasterId) {
        this.qualityParameterMasterId = qualityParameterMasterId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public Character getUpdate() {
        return update;
    }

    public void setUpdate(Character update) {
        this.update = update;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Integer getUomId() {
        return uomId;
    }

    public void setUomId(Integer uomId) {
        this.uomId = uomId;
    }

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
    }

    public Integer getThresholdPercentage() {
        return thresholdPercentage;
    }

    public void setThresholdPercentage(Integer thresholdPercentage) {
        this.thresholdPercentage = thresholdPercentage;
    }

    public Integer getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(Integer targetPercentage) {
        this.targetPercentage = targetPercentage;
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
}
