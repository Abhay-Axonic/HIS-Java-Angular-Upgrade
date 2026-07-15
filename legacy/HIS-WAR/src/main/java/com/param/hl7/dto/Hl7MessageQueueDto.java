package com.param.hl7.dto;

public class Hl7MessageQueueDto {

    private Integer Id;

    private String code;

    private String message;

    private String messageType;

    private String triggerEvent;

    private String status;

    private Integer patientId;

    private Integer encounterId;

    private Integer admissionId;

    private Integer unitId;

    private Integer orgId;

    private String responseCode;

    private String inboundResponseMessage;

    private String outboundResponseMessage;

    private String generateFor;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getInboundResponseMessage() {
        return inboundResponseMessage;
    }

    public void setInboundResponseMessage(String inboundResponseMessage) {
        this.inboundResponseMessage = inboundResponseMessage;
    }

    public String getOutboundResponseMessage() {
        return outboundResponseMessage;
    }

    public void setOutboundResponseMessage(String outboundResponseMessage) {
        this.outboundResponseMessage = outboundResponseMessage;
    }

    public String getGenerateFor() {
        return generateFor;
    }

    public void setGenerateFor(String generateFor) {
        this.generateFor = generateFor;
    }
}
