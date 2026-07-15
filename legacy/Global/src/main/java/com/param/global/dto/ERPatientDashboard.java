package com.param.global.dto;

public class ERPatientDashboard {
    private Integer admissionId;
    private Integer patientId;
    private Integer unitId;
    private Integer organizationId;
    private String patientName;
    private String uhid;
    private String arrivalTime;
    private String modeOfArrival;
    private String mlc;
    private String zone;
    private String bedNumber;
    private Integer triageCategoryId;
    private Integer doorToTraige;
    private Integer doorToDoctor;
    private String abcdeDesc;
    private String pocTest;
    private String diagnosis;
    private String flag;
    private String referrals;
    private String disposition;
    private Integer lengthOfStay;
    private String fromDate;
    private String toDate;
    private String ageString;
    private Integer currentTriage;

    private Character stemi;
    private Character stroke;
    private Character truma;
    private Character sepsis;
    private Character shock;
    private String code;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getModeOfArrival() {
        return modeOfArrival;
    }

    public void setModeOfArrival(String modeOfArrival) {
        this.modeOfArrival = modeOfArrival;
    }

    public String getMlc() {
        return mlc;
    }

    public void setMlc(String mlc) {
        this.mlc = mlc;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public Integer getTriageCategoryId() {
        return triageCategoryId;
    }

    public void setTriageCategoryId(Integer triageCategoryId) {
        this.triageCategoryId = triageCategoryId;
    }

//    public String getDoorToTraige() {
//        return doorToTraige;
//    }
//
//    public void setDoorToTraige(String doorToTraige) {
//        this.doorToTraige = doorToTraige;
//    }
//
//    public String getDoorToDoctor() {
//        return doorToDoctor;
//    }
//
//    public void setDoorToDoctor(String doorToDoctor) {
//        this.doorToDoctor = doorToDoctor;
//    }

    public String getAbcdeDesc() {
        return abcdeDesc;
    }

    public void setAbcdeDesc(String abcdeDesc) {
        this.abcdeDesc = abcdeDesc;
    }

    public String getPocTest() {
        return pocTest;
    }

    public void setPocTest(String pocTest) {
        this.pocTest = pocTest;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getReferrals() {
        return referrals;
    }

    public void setReferrals(String referrals) {
        this.referrals = referrals;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

//    public String getLengthOfStay() {
//        return lengthOfStay;
//    }
//
//    public void setLengthOfStay(String lengthOfStay) {
//        this.lengthOfStay = lengthOfStay;
//    }

    public Integer getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Integer admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getDoorToTraige() {
        return doorToTraige;
    }

    public void setDoorToTraige(Integer doorToTraige) {
        this.doorToTraige = doorToTraige;
    }

    public Integer getDoorToDoctor() {
        return doorToDoctor;
    }

    public void setDoorToDoctor(Integer doorToDoctor) {
        this.doorToDoctor = doorToDoctor;
    }

    public Integer getLengthOfStay() {
        return lengthOfStay;
    }

    public void setLengthOfStay(Integer lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }

    public String getAgeString() {
        return ageString;
    }

    public void setAgeString(String ageString) {
        this.ageString = ageString;
    }

    public Integer getCurrentTriage() {
        return currentTriage;
    }

    public void setCurrentTriage(Integer currentTriage) {
        this.currentTriage = currentTriage;
    }

    public Character getStroke() {
        return stroke;
    }

    public void setStroke(Character stroke) {
        this.stroke = stroke;
    }

    public Character getStemi() {
        return stemi;
    }

    public void setStemi(Character stemi) {
        this.stemi = stemi;
    }

    public Character getTruma() {
        return truma;
    }

    public void setTruma(Character truma) {
        this.truma = truma;
    }

    public Character getSepsis() {
        return sepsis;
    }

    public void setSepsis(Character sepsis) {
        this.sepsis = sepsis;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Character getShock() {
        return shock;
    }

    public void setShock(Character shock) {
        this.shock = shock;
    }
}
