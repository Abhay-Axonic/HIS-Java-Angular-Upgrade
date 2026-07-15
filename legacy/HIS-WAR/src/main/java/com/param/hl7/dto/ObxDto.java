package com.param.hl7.dto;

public class ObxDto {

    private String labResDtlsId;
    private String paramUnit;
    private String paramName;
    private String abnormalFlag;
    private String refRange;
    private String obrValue;
    private String obrResult;
    private String refRangeType;
    private String paramCode;
    private String testCode;

    public String getLabResDtlsId() {
        return labResDtlsId!=null?labResDtlsId:"";
    }

    public void setLabResDtlsId(String labResDtlsId) {
        this.labResDtlsId = labResDtlsId;
    }

    public String getParamUnit() {
        return paramUnit!=null?paramUnit:"";
    }

    public void setParamUnit(String paramUnit) {
        this.paramUnit = paramUnit;
    }

    public String getParamName() {
        return paramName!=null?paramName:"";
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getAbnormalFlag() {
        return abnormalFlag!=null?abnormalFlag:"";
    }

    public void setAbnormalFlag(String abnormalFlag) {
        this.abnormalFlag = abnormalFlag;
    }

    public String getRefRange() {
        return refRange!=null?refRange:"";
    }

    public void setRefRange(String refRange) {
        this.refRange = refRange;
    }

    public String getObrValue() {
        return obrValue!=null?obrValue:"";
    }

    public void setObrValue(String obrValue) {
        this.obrValue = obrValue;
    }

    public String getObrResult() {
        return obrResult!=null?obrResult:"";
    }

    public void setObrResult(String obrResult) {
        this.obrResult = obrResult;
    }

    public String getRefRangeType() {
        return refRangeType!=null?refRangeType:"";
    }

    public void setRefRangeType(String refRangeType) {
        this.refRangeType = refRangeType;
    }

    public String getParamCode() {
        return paramCode!=null?paramCode:"";
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getTestCode() {
        return testCode!=null?testCode:"";
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }
}
