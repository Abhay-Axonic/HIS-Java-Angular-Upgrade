package com.param.hl7.dto;

public class Dg1Dto {

    private String diagnosisId;
    private String icd10Code;
    private String diagnosisType;
    private String diagnosisCode;
    private String diagnosisDescription;
    private String diagnosisDate;

    public String getDiagnosisId() {
        return diagnosisId != null ? diagnosisId :"";
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getIcd10Code() {
        return icd10Code != null ? icd10Code :"";
    }

    public void setIcd10Code(String icd10Code) {
        this.icd10Code = icd10Code;
    }

    public String getDiagnosisType() {
        return diagnosisType != null ? diagnosisType :"";
    }

    public void setDiagnosisType(String diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public String getDiagnosisCode() {
        return diagnosisCode != null ? diagnosisCode :"";
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisDescription() {
        return diagnosisDescription != null ? diagnosisDescription :"";
    }

    public void setDiagnosisDescription(String diagnosisDescription) {
        this.diagnosisDescription = diagnosisDescription;
    }

    public String getDiagnosisDate() {
        return diagnosisDate != null ? diagnosisDate :"";
    }

    public void setDiagnosisDate(String diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }
}
