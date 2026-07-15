package com.param.hl7.dto;

public class Hl7QueueDetailsDto {

    private Integer Id;

    private Integer queueId;

    private Integer orderDetailsId;

    private Integer orderId;

    private Integer labResultId;

    private Integer labResultDtlsId;

    private Integer patientDiagnosisId;

    private Integer proReqId;

    private Integer allergyPatientMappId;

    private Character status;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public Integer getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(Integer orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getLabResultId() {
        return labResultId;
    }

    public void setLabResultId(Integer labResultId) {
        this.labResultId = labResultId;
    }

    public Integer getLabResultDtlsId() {
        return labResultDtlsId;
    }

    public void setLabResultDtlsId(Integer labResultDtlsId) {
        this.labResultDtlsId = labResultDtlsId;
    }

    public Integer getPatientDiagnosisId() {
        return patientDiagnosisId;
    }

    public void setPatientDiagnosisId(Integer patientDiagnosisId) {
        this.patientDiagnosisId = patientDiagnosisId;
    }

    public Integer getProReqId() {
        return proReqId;
    }

    public void setProReqId(Integer proReqId) {
        this.proReqId = proReqId;
    }

    public Integer getAllergyPatientMappId() {
        return allergyPatientMappId;
    }

    public void setAllergyPatientMappId(Integer allergyPatientMappId) {
        this.allergyPatientMappId = allergyPatientMappId;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
