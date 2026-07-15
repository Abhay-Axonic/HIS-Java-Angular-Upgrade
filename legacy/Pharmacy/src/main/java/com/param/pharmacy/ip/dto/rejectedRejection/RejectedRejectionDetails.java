package com.param.pharmacy.ip.dto.rejectedRejection;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RejectedRejectionDetails
{
	 private Integer id;
	 
	 private String rejectedNo; 
	 
	 private Date rejectedDate; 
	
	 private String doctorName;
	 
	 private String patientName;
	 
	 private String patientDetail;
	 
	 private String uhid;
	 
	 private String visitType;
	 
	 private String visitNo;
	 
	 private String store;
	 
	 private String wardName;
	 
	 private Integer wardId;
	 private Integer storeId;
	 private Integer genderId;
	 private Integer medicationType;
	 private String imagePath;   
	 private String genderCode; 
	 private String requestFrom;
	 private Integer patientId;
	 public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	private String bedNo;
	 
	 private Integer issueId;
	 
	 private Integer statusId;
	   
	 private String status;
	 private Integer ageStringDet;
	 private String patientName1;

	 private List<RejectedRejectionItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRejectedNo() {
		return rejectedNo;
	}

	public void setRejectedNo(String rejectedNo) {
		this.rejectedNo = rejectedNo;
	}

	public Date getRejectedDate() {
		return rejectedDate;
	}

	public void setRejectedDate(Date rejectedDate) {
		this.rejectedDate = rejectedDate;
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
	
	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	
	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public List<RejectedRejectionItem> getItems() {
		return items;
	}

	public void setItems(List<RejectedRejectionItem> items) {
		this.items = items;
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

	public RejectedRejectionDetails(Integer id, String rejectedNo, Date rejectedDate, String patientDetail, String uhid,
			String visitType, String visitNo) {
		super();
		this.id = id;
		this.rejectedNo = rejectedNo;
		this.rejectedDate = rejectedDate;
		this.patientDetail = patientDetail;
		this.uhid = uhid;
		this.visitType = visitType;
		this.visitNo = visitNo;
	}
	
	public RejectedRejectionDetails(Integer id, String rejectedNo, Date rejectedDate, String patientDetail, String uhid,
			String visitType, String visitNo, String patientName1, Integer ageStringDet) {
		super();
		this.id = id;
		this.rejectedNo = rejectedNo;
		this.rejectedDate = rejectedDate;
		this.patientDetail = patientDetail;
		this.uhid = uhid;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.patientName1 = patientName1;
		this.ageStringDet = ageStringDet;
	}
	
	public String getPatientName1() {
		return patientName1;
	}

	public void setPatientName1(String patientName1) {
		this.patientName1 = patientName1;
	}

	public Integer getAgeStringDet() {
		return ageStringDet;
	}

	public void setAgeStringDet(Integer ageStringDet) {
		this.ageStringDet = ageStringDet;
	}

	public RejectedRejectionDetails(Integer id, String rejectedNo, Date rejectedDate, String doctorName,
			String patientName, String uhid, String visitType, String store) {
		super();
		this.id = id;
		this.rejectedNo = rejectedNo;
		this.rejectedDate = rejectedDate;
		this.doctorName = doctorName;
		this.patientName = patientName;
		this.uhid = uhid;
		this.visitType = visitType;
		this.store = store;

	}
	
	public RejectedRejectionDetails(Integer id, String rejectedNo, Date rejectedDate, String doctorName,
			String patientName, String uhid, String visitType, String store, String patientName1, Integer ageStringDet) {
		super();
		this.id = id;
		this.rejectedNo = rejectedNo;
		this.rejectedDate = rejectedDate;
		this.doctorName = doctorName;
		this.patientName = patientName;
		this.uhid = uhid;
		this.visitType = visitType;
		this.store = store;
		this.patientName1 = patientName1;
		this.ageStringDet = ageStringDet;

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

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public RejectedRejectionDetails() {
		super();
	}

	public Integer getMedicationType() {
		return medicationType;
	}

	public void setMedicationType(Integer medicationType) {
		this.medicationType = medicationType;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	
	 
}