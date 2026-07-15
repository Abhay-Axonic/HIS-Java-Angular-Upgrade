package com.param.pharmacy.inPatient.dto.patientConsumableIssue;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientConsumableIssueResponse {
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

	protected Integer id;

	protected String indentNo;

	protected Date indentDate;

	protected String visitType;

	protected String visitNo;

	protected String uhid;

	protected String patientDetail;

	protected String doctorDetail;

	protected String storeName;

	protected String toStoreName;

	protected String wardName;

	protected String bedNo;

	protected Integer statusId;

	protected String status;
	
	protected Integer patientId;
	
	protected String issueNo;
	protected Integer age;

	protected Date issueDate;
	 private String patientName;
	    private String ageString;
	    private String genderCode;
	    private Integer genderId;
		private String imagePath;
		private Character isFinalBill;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
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

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getToStoreName() {
		return toStoreName;
	}

	public void setToStoreName(String toStoreName) {
		this.toStoreName = toStoreName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PatientConsumableIssueResponse() {
		super();
	}

	
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public PatientConsumableIssueResponse(Integer id, String indentNo, Date indentDate, String visitType,
			String visitNo, String uhid, String patientDetail, String doctorDetail, String storeName,
			String toStoreName, Integer statusId,
			String status,Integer patientId) {
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
		this.patientId=patientId;
	}
	
	public PatientConsumableIssueResponse(Integer id, String indentNo, Date indentDate, String visitType,
			String visitNo, String uhid, String patientDetail, String doctorDetail, String storeName,
			String toStoreName, Integer statusId,
			String status,Integer patientId, Integer age, String patientName) {
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
		this.patientId=patientId;
		this.age=age;
		this.patientName=patientName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Character getIsFinalBill() {
		return isFinalBill;
	}

	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}

}