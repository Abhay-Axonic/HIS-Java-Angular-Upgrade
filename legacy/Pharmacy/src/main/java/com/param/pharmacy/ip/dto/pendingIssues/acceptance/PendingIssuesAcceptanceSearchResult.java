package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PendingIssuesAcceptanceSearchResult
{
	private Integer id;
	
	private String issueNo;
		
	private Date issueDate;
	
	private String uhid;
	
	private String  patientDetail;
	
	private String doctorDetail;
	
	private Integer priorityId;
	
	private String visitType;
	
	private String store;
	private String toStoreName;
	
	private Integer medicationType;

	public String getToStoreName() {
		return toStoreName;
	}

	public void setToStoreName(String toStoreName) {
		this.toStoreName = toStoreName;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	private Integer toStoreId;
	
	private String wardName;
	
	private String bedNo;
	
	private Boolean consignment;
	
	private String indentNo;
	
	private String visitNo;

	private String priority;
	
	private String status;
	
	private Integer statusId;
	
	private Integer age;
	private String  genderCode;	
	private String imagePath;
	private Date prescriptionDate;
	private Integer indentId;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	public Boolean getConsignment() {
		return consignment;
	}

	public void setConsignment(Boolean consignment) {
		this.consignment = consignment;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	public PendingIssuesAcceptanceSearchResult(Integer id, String issueNo, Date issueDate, String uhid,
			String patientDetail, Integer age, String genderCode,String imagePath ,String doctorDetail, String visitType, String store) {
		super();
		this.id = id;
		this.issueNo = issueNo;
		this.issueDate = issueDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.age=age;
		this.genderCode=genderCode;
		this.imagePath=imagePath;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.store = store;
	}

	public PendingIssuesAcceptanceSearchResult(Integer id, String issueNo, String uhid, String patientDetail,
			String doctorDetail, String visitType, String priority, String status) {
		super();
		this.id = id;
		this.issueNo = issueNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.priority = priority;
		this.status = status;
	}

	public Integer getMedicationType() {
		return medicationType;
	}

	public void setMedicationType(Integer medicationType) {
		this.medicationType = medicationType;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public Integer getIndentId() {
		return indentId;
	}

	public void setIndentId(Integer indentId) {
		this.indentId = indentId;
	}
}