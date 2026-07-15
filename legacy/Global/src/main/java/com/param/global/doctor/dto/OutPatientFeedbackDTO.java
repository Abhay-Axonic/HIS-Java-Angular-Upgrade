package com.param.global.doctor.dto;

import java.util.List;

public class OutPatientFeedbackDTO {

    private Integer outPatientFeedbackId;

    private Integer inPatientFeedbackId;

    private Integer patientId;

    private String visitType;

    private Integer visitTypeId;

    private Integer key;

    private String value;

    private Integer feedbackKey;

    private String feedbackValue;

    private List<OutPatientFeedbackListDTO> feedbackSelectedList;

    private Integer unitId;

    private Integer organizationId;

    private Character status;

    private String createdDate;

    private String updatedDate;
    
    private Integer encounterId;
    
    private Integer admissionId;

    public Integer getOutPatientFeedbackId() {
        return outPatientFeedbackId;
    }

    public void setOutPatientFeedbackId(Integer outPatientFeedbackId) {
        this.outPatientFeedbackId = outPatientFeedbackId;
    }

    public Integer getInPatientFeedbackId() {
        return inPatientFeedbackId;
    }

    public void setInPatientFeedbackId(Integer inPatientFeedbackId) {
        this.inPatientFeedbackId = inPatientFeedbackId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public Integer getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(Integer visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getFeedbackKey() {
        return feedbackKey;
    }

    public void setFeedbackKey(Integer feedbackKey) {
        this.feedbackKey = feedbackKey;
    }

    public String getFeedbackValue() {
        return feedbackValue;
    }

    public void setFeedbackValue(String feedbackValue) {
        this.feedbackValue = feedbackValue;
    }

    public List<OutPatientFeedbackListDTO> getFeedbackSelectedList() {
        return feedbackSelectedList;
    }

    public void setFeedbackSelectedList(List<OutPatientFeedbackListDTO> feedbackSelectedList) {
        this.feedbackSelectedList = feedbackSelectedList;
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
}
