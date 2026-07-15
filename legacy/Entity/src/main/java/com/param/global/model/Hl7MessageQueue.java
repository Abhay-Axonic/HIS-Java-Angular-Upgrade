package com.param.global.model;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_ht7_message_queue", schema = "public")
@SequenceGenerator(name = "t_hl7_message_que_seq", sequenceName = "t_hl7_message_que_seq", allocationSize = 1)
public class Hl7MessageQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_hl7_message_que_seq")
    @Column(name = "id")
    private Integer Id;

    @Column(name = "code")
    private String code;

    @Column(name = "message")
    private String message;

    @Column(name="message_type")
    private String messageType;

    @Column(name="trigger_event")
    private String triggerEvent;

    @Column(name="status")
    private String status;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "admission_id")
    private Integer admissionId;

    @Column(name="created_date")
    @CreationTimestamp
    private Date createdDate;

    @Column(name="updated_date")
    @UpdateTimestamp
    private Date updatedDate;

    @Column(name="unit_id")
    private Integer unitId;

    @Column(name="org_id")
    private Integer orgId;

    @Column(name="response_code")
    private String responseCode;

    @Column(name="inbound_response_message")
    private String inboundResponseMessage;

    @Column(name="outbound_response_message")
    private String outboundResponseMessage;

    @Column(name="generate_for")
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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
