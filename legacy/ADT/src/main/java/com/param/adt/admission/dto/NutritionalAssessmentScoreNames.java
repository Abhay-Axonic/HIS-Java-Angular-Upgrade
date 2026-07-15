package com.param.adt.admission.dto;

public class NutritionalAssessmentScoreNames {

    private String nutritionalAssessmentName;

    private Integer nutritionalAssessmentId;

    private Integer nutritionalAssessmentNameId;

    private Double nutritionalAssessmentNamePoint;

    private Integer consumptionMarkerFlag;

    public String getNutritionalAssessmentName() {
        return nutritionalAssessmentName;
    }

    public void setNutritionalAssessmentName(String nutritionalAssessmentName) {
        this.nutritionalAssessmentName = nutritionalAssessmentName;
    }

    public Integer getNutritionalAssessmentId() {
        return nutritionalAssessmentId;
    }

    public void setNutritionalAssessmentId(Integer nutritionalAssessmentId) {
        this.nutritionalAssessmentId = nutritionalAssessmentId;
    }

    public Integer getNutritionalAssessmentNameId() {
        return nutritionalAssessmentNameId;
    }

    public void setNutritionalAssessmentNameId(Integer nutritionalAssessmentNameId) {
        this.nutritionalAssessmentNameId = nutritionalAssessmentNameId;
    }

    public Double getNutritionalAssessmentNamePoint() {
        return nutritionalAssessmentNamePoint;
    }

    public void setNutritionalAssessmentNamePoint(Double nutritionalAssessmentNamePoint) {
        this.nutritionalAssessmentNamePoint = nutritionalAssessmentNamePoint;
    }

    public Integer getConsumptionMarkerFlag() {
        return consumptionMarkerFlag;
    }

    public void setConsumptionMarkerFlag(Integer consumptionMarkerFlag) {
        this.consumptionMarkerFlag = consumptionMarkerFlag;
    }
}
