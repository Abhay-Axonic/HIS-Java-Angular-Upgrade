package com.param.global.unit.dto;

import javax.persistence.Column;
import java.util.Date;

public class AnesthesiaAssessmentDTO {

    private Integer anesthesiaAssessmentId;

    private Integer patientId;

//    private String specialityName;
//
//    private Integer specialityId;
//
//    private String doctorName;
//
//    private Integer doctorId;

    private Integer isPreviousSurgery;

    private String selectedAssessment;

    private String anesthesiaPlan;

    private Character saveAsDraft;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String createdDate;

    private Integer createdBy;

    private Integer updatedBy;

    private String updatedDate;
    
    private Integer encounterId;
    
    private Integer admissionId;

    private String previousSurgeryDesc;

    public Integer getAnesthesiaAssessmentId() {
        return anesthesiaAssessmentId;
    }

    public void setAnesthesiaAssessmentId(Integer anesthesiaAssessmentId) {
        this.anesthesiaAssessmentId = anesthesiaAssessmentId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

//    public String getSpecialityName() {
//        return specialityName;
//    }
//
//    public void setSpecialityName(String specialityName) {
//        this.specialityName = specialityName;
//    }
//
//    public Integer getSpecialityId() {
//        return specialityId;
//    }
//
//    public void setSpecialityId(Integer specialityId) {
//        this.specialityId = specialityId;
//    }
//
//    public String getDoctorName() {
//        return doctorName;
//    }
//
//    public void setDoctorName(String doctorName) {
//        this.doctorName = doctorName;
//    }
//
//    public Integer getDoctorId() {
//        return doctorId;
//    }
//
//    public void setDoctorId(Integer doctorId) {
//        this.doctorId = doctorId;
//    }

    public Integer getIsPreviousSurgery() {
        return isPreviousSurgery;
    }

    public void setIsPreviousSurgery(Integer isPreviousSurgery) {
        this.isPreviousSurgery = isPreviousSurgery;
    }

    public String getSelectedAssessment() {
        return selectedAssessment;
    }

    public void setSelectedAssessment(String selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
    }

    public String getAnesthesiaPlan() {
        return anesthesiaPlan;
    }

    public void setAnesthesiaPlan(String anesthesiaPlan) {
        this.anesthesiaPlan = anesthesiaPlan;
    }

    public Character getSaveAsDraft() {
        return saveAsDraft;
    }

    public void setSaveAsDraft(Character saveAsDraft) {
        this.saveAsDraft = saveAsDraft;
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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

    public String getPreviousSurgeryDesc() {
        return previousSurgeryDesc;
    }

    public void setPreviousSurgeryDesc(String previousSurgeryDesc) {
        this.previousSurgeryDesc = previousSurgeryDesc;
    }
}
