package com.param.pharmacy.inPatient.dto.patientConsumableIssue;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientConsumableIssueListResponse extends PatientConsumableIssueResponse {

	private String issueNo;

	private Date issueDate;
	private String patientName;

	public PatientConsumableIssueListResponse() {
		super();
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public PatientConsumableIssueListResponse(Integer id, String indentNo, Date indentDate, String visitType,
			String visitNo, String uhid, String patientDetail, String doctorDetail, String storeName,String toStoreName, Integer statusId,
			String status) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.storeName = storeName;
		this.toStoreName = toStoreName;
		this.statusId = statusId;
		this.status = status;
	}
	
	public PatientConsumableIssueListResponse(Integer id, String indentNo, Date indentDate, String visitType,
			String visitNo, String uhid, String patientDetail, String doctorDetail, String storeName,String toStoreName, Integer statusId,
			String status, Integer age, String patientName) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.storeName = storeName;
		this.toStoreName = toStoreName;
		this.statusId = statusId;
		this.status = status;
		this.age = age;
		this.patientName = patientName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

}