package com.param.pharmacy.ip.dto.patientIssueRejection;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class PatientIssueRejectionSearch extends CookieObjectDto
{
	 private Date fromDate;
	 
	 private Date toDate;
	 
	 private String doctorName;
	 
	 private Integer patientId;
	 
	 private String patientName;
	 
	 private Integer visitType;
	 
	 private String uhid;
	 
	 private Integer floor;
	 
	 private String issueRejectNo;

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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public String getIssueRejectNo() {
		return issueRejectNo;
	}

	public void setIssueRejectNo(String issueRejectNo) {
		this.issueRejectNo = issueRejectNo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}
	 
}