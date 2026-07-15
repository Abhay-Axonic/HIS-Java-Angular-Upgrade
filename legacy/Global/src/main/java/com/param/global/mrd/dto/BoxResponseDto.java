package com.param.global.mrd.dto;

public class BoxResponseDto {
    private Integer boxId;

    private String boxCode;

    private String boxDescription;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public String getBoxDescription() {
        return boxDescription;
    }

    public void setBoxDescription(String boxDescription) {
        this.boxDescription = boxDescription;
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
}
