package com.param.global.mrd.dto;

public class RackResponseDto {
    private Integer rackId;

    private String rackCode;

    private String rackDescription;

    private Integer unitId;

    private Integer organizationId;

    private Character status;


    public Integer getRackId() {
        return rackId;
    }

    public void setRackId(Integer rackId) {
        this.rackId = rackId;
    }

    public String getRackCode() {
        return rackCode;
    }

    public void setRackCode(String rackCode) {
        this.rackCode = rackCode;
    }

    public String getRackDescription() {
        return rackDescription;
    }

    public void setRackDescription(String rackDescription) {
        this.rackDescription = rackDescription;
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

    @Override
    public String toString() {
        return "RackResponseDto{" +
                "rackId=" + rackId +
                ", rackCode='" + rackCode + '\'' +
                ", rackDescription='" + rackDescription + '\'' +
                ", unitId=" + unitId +
                ", organizationId=" + organizationId +
                ", status=" + status +
                '}';
    }
}
