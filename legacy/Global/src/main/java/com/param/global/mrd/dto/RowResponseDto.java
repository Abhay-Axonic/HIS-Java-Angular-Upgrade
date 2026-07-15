package com.param.global.mrd.dto;

public class RowResponseDto {
    private Integer rowId;

    private String rowCode;

    private String rowDescription;

    private Integer unitId;

    private Integer organizationId;

    private Character status;


    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getRowCode() {
        return rowCode;
    }

    public void setRowCode(String rowCode) {
        this.rowCode = rowCode;
    }

    public String getRowDescription() {
        return rowDescription;
    }

    public void setRowDescription(String rowDescription) {
        this.rowDescription = rowDescription;
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
