package com.param.global.model;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_ht7_message_queue_details", schema = "public")
@SequenceGenerator(name = "t_hl7_message_que_details_seq", sequenceName = "t_hl7_message_que_details_seq", allocationSize = 1)
public class Hl7QueueDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_hl7_message_que_details_seq")
    @Column(name = "id")
    private Integer Id;

    @Column(name = "queue_id")
    private Integer queueId;

    @Column(name = "order_details_id")
    private Integer orderDetailsId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name="lab_result_id")
    private Integer labResultId;

    @Column(name="lab_result_dtls_id")
    private Integer labResultDtlsId;

    @Column(name="patient_diagnosis_id")
    private Integer patientDiagnosisId;

    @Column(name="pro_req_id")
    private Integer proReqId;

    @Column(name="allergy_patient_mapp_id")
    private Integer allergyPatientMappId;

    @Column(name="status")
    private Character status;

    @Column(name="created_date")
    @CreationTimestamp
    private Date createdDate;

    @Column(name="updated_date")
    @UpdateTimestamp
    private Date updatedDate;

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
}
