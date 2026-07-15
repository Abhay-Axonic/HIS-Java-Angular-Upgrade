package com.param.hl7.dto;

public class Al1Dto {
    private String allergyCode;

    public String getAllergyCode() {
        return allergyCode != null ? allergyCode :"";
    }

    public void setAllergyCode(String allergyCode) {
        this.allergyCode = allergyCode;
    }
}
