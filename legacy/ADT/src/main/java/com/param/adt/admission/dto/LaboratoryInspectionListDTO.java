package com.param.adt.admission.dto;

import java.util.List;

public class LaboratoryInspectionListDTO {

    private Integer labInspectionId;

    private String labInspectionName;

    private Integer labInspectionNameId;

    private Integer labInspectionNameFlag;

    public Integer getLabInspectionId() {
        return labInspectionId;
    }

    public void setLabInspectionId(Integer labInspectionId) {
        this.labInspectionId = labInspectionId;
    }

    public String getLabInspectionName() {
        return labInspectionName;
    }

    public void setLabInspectionName(String labInspectionName) {
        this.labInspectionName = labInspectionName;
    }

    public Integer getLabInspectionNameId() {
        return labInspectionNameId;
    }

    public void setLabInspectionNameId(Integer labInspectionNameId) {
        this.labInspectionNameId = labInspectionNameId;
    }

    public Integer getLabInspectionNameFlag() {
        return labInspectionNameFlag;
    }

    public void setLabInspectionNameFlag(Integer labInspectionNameFlag) {
        this.labInspectionNameFlag = labInspectionNameFlag;
    }
}
