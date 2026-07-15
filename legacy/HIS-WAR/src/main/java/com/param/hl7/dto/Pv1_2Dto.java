package com.param.hl7.dto;

public class Pv1_2Dto {
    private String patientType;
    private String visitNumber;
    private String admissionId;
    private String admissionDate;
    private String admissionType;
    private String hospitalType;
    private String hospitalService;
    private String wardName;
    private String roomNo;
    private String bedNo;
    private String reasonForAdmission;
    private String dischargeDate;
    private Integer bedCategoryId;
    private Integer patientId;

    public String getPatientType() {
        return patientType!=null?patientType:"";
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getVisitNumber() {
        return visitNumber!=null?visitNumber:"";
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getAdmissionDate() {
        return admissionDate!=null?admissionDate:"";
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getAdmissionType() {
        return admissionType!=null?admissionType:"";
    }

    public void setAdmissionType(String admissionType) {
        this.admissionType = admissionType;
    }

    public String getHospitalType() {
        return hospitalType!=null?hospitalType:"";
    }

    public void setHospitalType(String hospitalType) {
        this.hospitalType = hospitalType;
    }

    public String getHospitalService() {
        return hospitalService!=null?hospitalService:"";
    }

    public void setHospitalService(String hospitalService) {
        this.hospitalService = hospitalService;
    }

    public String getWardName() {
        return wardName!=null?wardName:"";
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBedNo() {
        return bedNo!=null?bedNo:"";
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getReasonForAdmission() {
        return reasonForAdmission!=null?reasonForAdmission:"";
    }

    public void setReasonForAdmission(String reasonForAdmission) {
        this.reasonForAdmission = reasonForAdmission;
    }

    public String getDischargeDate() {
        return dischargeDate!=null?dischargeDate:"";
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getRoomNo() {
        return roomNo!=null?roomNo:"";
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getBedCategoryId() {
        return bedCategoryId;
    }

    public void setBedCategoryId(Integer bedCategoryId) {
        this.bedCategoryId = bedCategoryId;
    }

    public String getAdmissionId() {
        return admissionId!=null?admissionId:"";
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
