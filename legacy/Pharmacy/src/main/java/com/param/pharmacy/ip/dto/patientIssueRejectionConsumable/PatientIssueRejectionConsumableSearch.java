package com.param.pharmacy.ip.dto.patientIssueRejectionConsumable;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class PatientIssueRejectionConsumableSearch extends CookieObjectDto
{
	 private Date fromDate;
	 
	 private Date toDate;
	 
	 private Integer visitType;
	 
	 private Integer patientId;
	 
	 private String patientName;
	 
	 private String uhid;
	 
	 private String bedNo;
	 
	 private Integer wardId;
	 
	 private String visitNo;
	 
	 private String issueRejectNo;
	 
	 private Integer storeId;

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

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getIssueRejectNo() {
		return issueRejectNo;
	}

	public void setIssueRejectNo(String issueRejectNo) {
		this.issueRejectNo = issueRejectNo;
	}
	 
}