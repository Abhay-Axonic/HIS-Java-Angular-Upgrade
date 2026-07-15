package com.param.hl7.dto;

public class PidDto {

    private Integer patientId;
    private Integer hospitalId;
    private Integer unitId;
    private String patientIdentifire;
    private String givenName;
    private String familyName;
    private String dob;
    private String sex;
    private String cityCode;
    private String cityName;
    private String countryCode;
    private String countryName;
    private String multipleBirthIndicator;
    private String mobileNumber;
    private String zipCode;
    private String doctorName;
    private Integer appointmentId;
    private String userName;
    private String prefixDesc;
    private String emailId;
    private String organizationName;
    private String specialityName;
    private String reasonDesc;
    private String unitName;
    private String stateName;
    private String address;
    private String appointmentDate;
    private String fromTimeString;
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientIdentifire() {
        return patientIdentifire!=null?patientIdentifire:"";
    }

    public void setPatientIdentifire(String patientIdentifire) {
        this.patientIdentifire = patientIdentifire;
    }

    public String getGivenName() {
        return givenName!=null?givenName:"";
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName!=null?familyName:"";
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getDob() {
        return dob!=null?dob:"";
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex!=null?sex:"";
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCityCode() {
        return cityCode!=null?cityCode:"";
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName!=null?cityName:"";
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode!=null?countryCode:"";
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName!=null?countryName:"";
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getMultipleBirthIndicator() {
        return  multipleBirthIndicator!=null?multipleBirthIndicator:"";
    }

    public void setMultipleBirthIndicator(String multipleBirthIndicator) {
        this.multipleBirthIndicator = multipleBirthIndicator;
    }

    public String getMobileNumber() {
        return mobileNumber!=null?mobileNumber:"";
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getZipCode() {
        return  zipCode!=null?zipCode:"";
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrefixDesc() {
        return prefixDesc;
    }

    public void setPrefixDesc(String prefixDesc) {
        this.prefixDesc = prefixDesc;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getFromTimeString() {
        return fromTimeString;
    }

    public void setFromTimeString(String fromTimeString) {
        this.fromTimeString = fromTimeString;
    }
}
