package com.param.global.dto.global;

public class AIAppointmentQueueDto {
    private Integer appointmentId;

    private Integer doctorId;

    private Integer departmenetId;

    private Integer consultedBy;

    private String status;

    private Integer channelId;

    private Integer createdBy;

    private Integer hospitalId;

    private Integer unitId;

    private String aiPayloadJson;

    private Character isPayloadFinalize;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getDepartmenetId() {
        return departmenetId;
    }

    public void setDepartmenetId(Integer departmenetId) {
        this.departmenetId = departmenetId;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
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
}

