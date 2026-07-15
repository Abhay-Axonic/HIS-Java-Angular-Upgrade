package com.param.global.model;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_QUALITY_PARAMETER_MASTER_LIST",
                query = "SELECT qualityParameterMaster.quality_parameter_master_id AS \"qualityParameterMasterId\", "
                        + " qualityParameterMaster.module_id AS \"moduleId\", "
                        + " qualityParameterMaster.module_name AS \"moduleName\", "
                        + " qualityParameterMaster.activity_id AS \"activityId\", "
                        + " qualityParameterMaster.activity_name AS \"activityName\", "
                        + " qualityParameterMaster.parameter_id AS \"parameterId\", "
                        + " qualityParameterMaster.standard AS \"standard\", "
                        + " qualityParameterMaster.uom_id AS \"uomId\", "
                        + " qualityParameterMaster.uom_desc AS \"uomDesc\", "
                        + " qualityParameterMaster.threshold_percentage AS \"thresholdPercentage\", "
                        + " qualityParameterMaster.target_percentage AS \"targetPercentage\", "
                        + " qualityParameterMaster.unit_id AS \"unitId\", "
                        + " qualityParameterMaster.organization_id AS \"organizationId\", "
                        + " qualityParameterMaster.status AS \"status\", "
                        + " to_char(qualityParameterMaster.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(qualityParameterMaster.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", "
                        + " qualityParameterMaster.created_by AS \"createdBy\", "
                        + " qualityParameterMaster.updated_by AS \"updatedBy\" "
                        + " FROM public.t_quality_parameter_master qualityParameterMaster "
                        + " WHERE qualityParameterMaster.unit_id = :unitId"
                        + " AND qualityParameterMaster.organization_id = :organizationId"
                        + " ORDER BY qualityParameterMaster.quality_parameter_master_id desc"
        ),

        @NamedNativeQuery(name = "GET_QUALITY_PARAMETER_MASTER_BY_ID",
                query = "SELECT qualityParameterMaster.quality_parameter_master_id AS \"qualityParameterMasterId\", "
                        + " qualityParameterMaster.module_id AS \"moduleId\", "
                        + " qualityParameterMaster.activity_id AS \"activityId\", "
                        + " qualityParameterMaster.activity_name AS \"activityName\", "
                        + " qualityParameterMaster.parameter_id AS \"parameterId\", "
                        + " qualityParameterMaster.standard AS \"standard\", "
                        + " qualityParameterMaster.uom_id AS \"uomId\", "
                        + " qualityParameterMaster.threshold_percentage AS \"thresholdPercentage\", "
                        + " qualityParameterMaster.target_percentage AS \"targetPercentage\", "
                        + " qualityParameterMaster.unit_id AS \"unitId\", "
                        + " qualityParameterMaster.organization_id AS \"organizationId\", "
                        + " qualityParameterMaster.status AS \"status\", "
                        + " to_char(qualityParameterMaster.created_date, 'dd/MM/yyyy HH24:MI:SS') AS \"createdDate\", "
                        + " to_char(qualityParameterMaster.updated_date, 'dd/MM/yyyy HH24:MI:SS') AS \"updatedDate\", "
                        + " qualityParameterMaster.created_by AS \"createdBy\", "
                        + " qualityParameterMaster.updated_by AS \"updatedBy\" "
                        + " FROM public.t_quality_parameter_master qualityParameterMaster "
                        + " AND qualityParameterMaster.unit_id = :unitId"
                        + " AND qualityParameterMaster.organization_id = :organizationId"
                        + " AND qualityParameterMaster.activity_id = :activityId"
                        + " ORDER BY qualityParameterMaster.quality_parameter_master_id desc"
        )
})

@Entity
@Table(name = "t_quality_parameter_master", schema = "public")
@SequenceGenerator(name = "t_quality_parameter_master_seq", sequenceName = "public.t_quality_parameter_master_seq", allocationSize = 1)
public class QualityParameterMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_quality_parameter_master_seq")
    @Column(name = "quality_parameter_master_id")
    private Integer qualityParameterMasterId;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "activity_name")
    private String activityName;

    @Column(name = "parameter_id")
    private Integer parameterId;

    @Column(name = "standard")
    private String standard;

    @Column(name = "uom_id")
    private Integer uomId;
    @Column(name = "uom_desc")
    private String uomDesc;

    @Column(name = "threshold_percentage")
    private Integer thresholdPercentage;

    @Column(name = "target_percentage")
    private Integer targetPercentage;

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

    public String getUomDesc() {
        return uomDesc;
    }

    public void setUomDesc(String uomDesc) {
        this.uomDesc = uomDesc;
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
}
