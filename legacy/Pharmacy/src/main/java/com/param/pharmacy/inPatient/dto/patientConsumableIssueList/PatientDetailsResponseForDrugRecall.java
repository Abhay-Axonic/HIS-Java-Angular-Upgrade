package com.param.pharmacy.inPatient.dto.patientConsumableIssueList;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDetailsResponseForDrugRecall
{
    protected Integer id;

    protected String visitType;

    protected String visitNo;

    protected String uhid;

    protected String patientDetail;

    protected String wardName;

    protected String batchNo;

    protected Date expiryDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getPatientDetail() {
        return patientDetail;
    }

    public void setPatientDetail(String patientDetail) {
        this.patientDetail = patientDetail;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public PatientDetailsResponseForDrugRecall() {
        super();
    }

    public PatientDetailsResponseForDrugRecall(Integer id, String visitType, String visitNo, String uhid, String patientDetail, String wardName, String batchNo, Date expiryDate)
    {
        super();
        this.id=id;
        this.visitType = visitType;
        this.visitNo = visitNo;
        this.uhid = uhid;
        this.patientDetail = patientDetail;
        this.wardName = wardName;
        this.batchNo = batchNo;
        this.expiryDate = expiryDate;
    }
}
