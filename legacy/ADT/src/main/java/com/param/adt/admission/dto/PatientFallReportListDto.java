package com.param.adt.admission.dto;

public class PatientFallReportListDto {

    private Integer fallReportingId;

    private String fallReportingName;

    private Integer fallReportingNameId;

    public Integer getFallReportingId() {
        return fallReportingId;
    }

    public void setFallReportingId(Integer fallReportingId) {
        this.fallReportingId = fallReportingId;
    }

    public String getFallReportingName() {
        return fallReportingName;
    }

    public void setFallReportingName(String fallReportingName) {
        this.fallReportingName = fallReportingName;
    }

    public Integer getFallReportingNameId() {
        return fallReportingNameId;
    }

    public void setFallReportingNameId(Integer fallReportingNameId) {
        this.fallReportingNameId = fallReportingNameId;
    }
}
