package com.param.pharmacy.nursing.pharmacy.dto.patientIndent;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientIndentList
{
	private Integer id;
	
	private String indentNo;
	
	private Date indentDate;
	
	private String visitType;
	
	private String visitNo;
	
	private String uhid;
	
	private String patientDetail;
	
	private String doctorDetail;
	
	private String store;

	private String toStore;
	
	private String wardName;
	
	private String bedNo;
	
	private String status;
	
	private Integer statusId;
	
	private String approvalStatus;
	
	private Integer approvalStatusId;
	
	private Integer age;
	private String  genderCode;	
	private String imagePath;
	 private String addedBy;
	 private Integer patientId;

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

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

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public PatientIndentList()
	{
		super();
	}

	public PatientIndentList(Integer id, String indentNo, Date indentDate, String visitType, String visitNo,
			String uhid, String patientDetail, Integer age, String genderCode,String imagePath, String doctorDetail, String store, String toStore, String status, Integer statusId) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.age=age;
		this.genderCode=genderCode;
		this.imagePath=imagePath;
		this.doctorDetail = doctorDetail;
		this.store = store;
		this.toStore = toStore;
		this.status = status;
		this.statusId = statusId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
}