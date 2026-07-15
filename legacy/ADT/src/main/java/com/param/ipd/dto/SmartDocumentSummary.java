package com.param.ipd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartDocumentSummary {

    private int appointmentId;

    private Integer doctorId;

    private String followUpDate;


    private  String followUpTimeSlot;

    private String labMailId;

    private Integer hospitalId;

    private Integer unitId;

    private Integer userId;

    private String zoneDesc;

    private String myLatestMedicalSummary;

    private String currentMedicalCasesheetData;
    private Integer appointmentStatusId;

    private Integer appointmentTypeId;

    private Integer patientId;

    private boolean patientDataSaveTemparary;

    private String servicePriority;

    private HashMap<Integer,String> patientDocumnetList;

    private List<String> coverSheetReportList;

    private String oldComprehensiveClinicalReport;

    private String newComprehensiveClinicalReport;

    private String comprehensiveClinicalReportUrl;

    private String bucketName;

    private String base64PdfForClinicalReport;

    private String inputSendForClinicalReport;

    private String doctorCoverPageDtojsonString;

    private Boolean myMedicalLetestMedicalSummary;

    private Integer channelId;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }



    public String getFollowUpTimeSlot() {
        return followUpTimeSlot;
    }

    public void setFollowUpTimeSlot(String followUpTimeSlot) {
        this.followUpTimeSlot = followUpTimeSlot;
    }

    public String getLabMailId() {
        return labMailId;
    }

    public void setLabMailId(String labMailId) {
        this.labMailId = labMailId;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getZoneDesc() {
        return zoneDesc;
    }

    public void setZoneDesc(String zoneDesc) {
        this.zoneDesc = zoneDesc;
    }

    public String getMyLatestMedicalSummary() {
        return myLatestMedicalSummary;
    }

    public void setMyLatestMedicalSummary(String myLatestMedicalSummary) {
        this.myLatestMedicalSummary = myLatestMedicalSummary;
    }

    public Integer getAppointmentStatusId() {
        return appointmentStatusId;
    }

    public void setAppointmentStatusId(Integer appointmentStatusId) {
        this.appointmentStatusId = appointmentStatusId;
    }

    public String getCurrentMedicalCasesheetData() {
        return currentMedicalCasesheetData;
    }

    public void setCurrentMedicalCasesheetData(String currentMedicalCasesheetData) {
        this.currentMedicalCasesheetData = currentMedicalCasesheetData;
    }

    public Integer getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(Integer appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public boolean isPatientDataSaveTemparary() {
        return patientDataSaveTemparary;
    }

    public void setPatientDataSaveTemparary(boolean patientDataSaveTemparary) {
        this.patientDataSaveTemparary = patientDataSaveTemparary;
    }

    public String getServicePriority() {
        return servicePriority;
    }

    public void setServicePriority(String servicePriority) {
        this.servicePriority = servicePriority;
    }

    public HashMap<Integer, String> getPatientDocumnetList() {
        return patientDocumnetList;
    }

    public void setPatientDocumnetList(HashMap<Integer, String> patientDocumnetList) {
        this.patientDocumnetList = patientDocumnetList;
    }

    public List<String> getCoverSheetReportList() {
        return coverSheetReportList;
    }

    public void setCoverSheetReportList(List<String> coverSheetReportList) {
        this.coverSheetReportList = coverSheetReportList;
    }

    public String getOldComprehensiveClinicalReport() {
        return oldComprehensiveClinicalReport;
    }

    public void setOldComprehensiveClinicalReport(String oldComprehensiveClinicalReport) {
        this.oldComprehensiveClinicalReport = oldComprehensiveClinicalReport;
    }

    public String getNewComprehensiveClinicalReport() {
        return newComprehensiveClinicalReport;
    }

    public void setNewComprehensiveClinicalReport(String newComprehensiveClinicalReport) {
        this.newComprehensiveClinicalReport = newComprehensiveClinicalReport;
    }

    public String getComprehensiveClinicalReportUrl() {
        return comprehensiveClinicalReportUrl;
    }

    public void setComprehensiveClinicalReportUrl(String comprehensiveClinicalReportUrl) {
        this.comprehensiveClinicalReportUrl = comprehensiveClinicalReportUrl;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBase64PdfForClinicalReport() {
        return base64PdfForClinicalReport;
    }

    public void setBase64PdfForClinicalReport(String base64PdfForClinicalReport) {
        this.base64PdfForClinicalReport = base64PdfForClinicalReport;
    }

    public String getInputSendForClinicalReport() {
        return inputSendForClinicalReport;
    }

    public void setInputSendForClinicalReport(String inputSendForClinicalReport) {
        this.inputSendForClinicalReport = inputSendForClinicalReport;
    }

    public String getDoctorCoverPageDtojsonString() {
        return doctorCoverPageDtojsonString;
    }

    public void setDoctorCoverPageDtojsonString(String doctorCoverPageDtojsonString) {
        this.doctorCoverPageDtojsonString = doctorCoverPageDtojsonString;
    }

    public Boolean getMyMedicalLetestMedicalSummary() {
        return myMedicalLetestMedicalSummary;
    }

    public void setMyMedicalLetestMedicalSummary(Boolean myMedicalLetestMedicalSummary) {
        this.myMedicalLetestMedicalSummary = myMedicalLetestMedicalSummary;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
}
