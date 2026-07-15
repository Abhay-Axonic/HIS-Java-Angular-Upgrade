package com.param.hl7.dto;

public class Pr1Dto {

    private String procedureOrderId;
    private String procedureCode;
    private String procedureDescription;
    private String codingMethod;
    private String procedureType;
    private String procedureDate;

    public String getProcedureOrderId() {
        return procedureOrderId != null ? procedureOrderId :"";
    }

    public void setProcedureOrderId(String procedureOrderId) {
        this.procedureOrderId = procedureOrderId;
    }

    public String getProcedureCode() {
        return procedureCode != null ? procedureCode :"";
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureDescription() {
        return procedureDescription != null ? procedureDescription :"";
    }

    public void setProcedureDescription(String procedureDescription) {
        this.procedureDescription = procedureDescription;
    }

    public String getCodingMethod() {
        return codingMethod != null ? codingMethod :"";
    }

    public void setCodingMethod(String codingMethod) {
        this.codingMethod = codingMethod;
    }

    public String getProcedureType() {
        return procedureType != null ? procedureType :"";
    }

    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }

    public String getProcedureDate() {
        return procedureDate != null ? procedureDate :"";
    }

    public void setProcedureDate(String procedureDate) {
        this.procedureDate = procedureDate;
    }
}
