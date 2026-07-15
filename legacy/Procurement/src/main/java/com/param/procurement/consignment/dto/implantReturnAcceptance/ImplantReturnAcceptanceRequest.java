package com.param.procurement.consignment.dto.implantReturnAcceptance;

import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class ImplantReturnAcceptanceRequest extends CookieObjectDto
{
	
	private Date fromDate;
	
	private Date toDate;
	
	private Integer visitType;
	
	private Integer patientId;
	
	private String patientName;
	
	private String uhid;
	
	private Integer wardId;
	
	private String doctorName;
	
	private String returnNo;
	
	private Integer store;
	
	private String visitNo;
	
	private Integer floor;
	 
	 private String issueNo;


	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

}
