package com.param.lis.global.dto;

public class BarcodeDto {

	
	private Integer patientId;
	private Integer labSampleId;
	private String sampleId;
	private String uhid;
	private String visitType;
	private Integer visitTypeId;
	private String deptName;
	private String patientDetails;
	private String generationDateTime;
	private String generationTime;
	private String sampleName;
	private Integer orderDetailsId;
	private Integer labSampleDtlsId;
	
	
	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}
	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getUhid() {
		return uhid;
	}
	public void setUhid(String uhid) {
		this.uhid = uhid;
	}
	public String getPatientDetails() {
		return patientDetails;
	}
	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}
	public String getGenerationDateTime() {
		return generationDateTime;
	}
	public void setGenerationDateTime(String generationDateTime) {
		this.generationDateTime = generationDateTime;
	}
	public String getSampleId() {
		return sampleId;
	}
	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}
	public Integer getLabSampleId() {
		return labSampleId;
	}
	public void setLabSampleId(Integer labSampleId) {
		this.labSampleId = labSampleId;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
		if(this.visitType!=null)
		this.visitType = (visitTypeId==1)?"OP":"IP";
	}
	public String getSampleName() {
		return sampleName;
	}
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}
	public String getGenerationTime() {
		return generationTime;
	}
	public void setGenerationTime(String generationTime) {
		this.generationTime = generationTime;
	}
	
}
