package com.param.opd.appointment.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ai_appointment_queue", schema = "opd")
@SequenceGenerator(name = "ai_appointment_seq", sequenceName = "opd.ai_appointment_seq", allocationSize = 1)
public class AIAppointmentQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ai_appointment_seq")
    @Column(name = "ai_appointment_id")
    private Integer aiAppointmentId;

    @Column(name = "appointment_id")
    private Integer appointmentId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "consulted_by")
    private Integer consultedBy;

    @Column(name = "status")
    private String status;

    @Column(name = "ai_payload_json")
    private String aiPayloadJson;

    @Column(name = "is_payload_finalize")
    private Character isPayloadFinalize;

    @Column(name = "channel_id")
    protected Integer channelId;

    @Column(name = "created_by" ,updatable = false)
    private Integer createdBy;

    @Column(name = "created_date" ,updatable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private Date updatedDate;


    public Integer getAiAppointmentId() {
        return aiAppointmentId;
    }

    public void setAiAppointmentId(Integer aiAppointmentId) {
        this.aiAppointmentId = aiAppointmentId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getConsultedBy() {
        return consultedBy;
    }

    public void setConsultedBy(Integer consultedBy) {
        this.consultedBy = consultedBy;
    }

    public String getAiPayloadJson() {
        return aiPayloadJson;
    }

    public void setAiPayloadJson(String aiPayloadJson) {
        this.aiPayloadJson = aiPayloadJson;
    }

    public Character getIsPayloadFinalize() {
        return isPayloadFinalize;
    }

    public void setIsPayloadFinalize(Character isPayloadFinalize) {
        this.isPayloadFinalize = isPayloadFinalize;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
