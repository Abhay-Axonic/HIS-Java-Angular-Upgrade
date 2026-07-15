package com.param.global.dto.global;

import java.sql.Timestamp;

public class PatientSearchDetails {

    private String prefixCode;

    private String firstName;

    private String middleName;

    private String lastName;

    private String uhidNumber;

    private Timestamp dob;

    private String genderCode;

    private String mobileNumber;

    private Integer patientId;

    public String getPrefixCode() {
        return prefixCode;
    }

    public void setPrefixCode(String prefixCode) {
        this.prefixCode = prefixCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUhidNumber() {
        return uhidNumber;
    }

    public void setUhidNumber(String uhidNumber) {
        this.uhidNumber = uhidNumber;
    }

    public Timestamp getDob() {
        return dob;
    }

    public void setDob(Timestamp dob) {
        this.dob = dob;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
