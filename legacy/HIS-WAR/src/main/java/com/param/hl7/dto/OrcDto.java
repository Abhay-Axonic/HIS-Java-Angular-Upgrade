package com.param.hl7.dto;

public class OrcDto {

    private String orderNo;
    private String orderDate;
    private String orderDetailsId;
    private String procedureOrderId;
    private String resultStatus;
    private String procedureName;
    private String oderProviderIdNumber;
    private String oderProviderFamilyName;
    private String observationStartTime;
    private String observationEndTime;

    public String getOrderNo() {
        return orderNo !=null? orderNo : "";
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate !=null? orderDate : "";
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDetailsId() {
        return orderDetailsId !=null? orderDetailsId : "";
    }

    public void setOrderDetailsId(String orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public String getResultStatus() {
        return resultStatus !=null? resultStatus : "";
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getProcedureName() {
        return procedureName !=null? procedureName : "";
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getOderProviderIdNumber() {
        return  oderProviderIdNumber !=null? oderProviderIdNumber : "";
    }

    public void setOderProviderIdNumber(String oderProviderIdNumber) {
        this.oderProviderIdNumber = oderProviderIdNumber;
    }

    public String getOderProviderFamilyName() {
        return  oderProviderFamilyName !=null? oderProviderFamilyName : "";
    }

    public void setOderProviderFamilyName(String oderProviderFamilyName) {
        this.oderProviderFamilyName = oderProviderFamilyName;
    }

    public String getObservationStartTime() {
        return observationStartTime !=null? observationStartTime : "";
    }

    public void setObservationStartTime(String observationStartTime) {
        this.observationStartTime = observationStartTime;
    }

    public String getObservationEndTime() {
        return observationEndTime !=null? observationEndTime : "";
    }

    public void setObservationEndTime(String observationEndTime) {
        this.observationEndTime = observationEndTime;
    }

    public String getProcedureOrderId() {
        return procedureOrderId !=null? procedureOrderId : "";
    }

    public void setProcedureOrderId(String procedureOrderId) {
        this.procedureOrderId = procedureOrderId;
    }
}
