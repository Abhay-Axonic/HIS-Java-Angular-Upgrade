package com.param.pharmacy.op.dto.salesWorkList;

import java.sql.Timestamp;

public class ResponseWorkListSearch {

	private Long id;
	
	private String visitType;
	
	private Timestamp visitDateTime;
	
	private Timestamp prescriptionDateTime;
	
	private String uhid;
	
	private String patientDetail;
	
	private String doctorDetail;
	
	private String priority;
	
	private String status;
	
	private String prescriptionNo;
	
	private Integer statusId;
	
	private Integer doctorId;
	
	private Integer encounterId;
	private String ageStringDet;

	private Timestamp patientDob;
	private String specialityName;
	private Integer genderId;
	private String imagePath;
	private String genderCode;
	private String patientName;
	private Boolean statFlag;
	private String entitlementDesc;
	private String mobileNumber;
	private Integer addedByUser;
	private String  userName;
	private String  countryCallingCode;
	private String  contractName;
	
	private Integer patientId;
	
	public String getEntitlementDesc() {
		return entitlementDesc;
	}

	public void setEntitlementDesc(String entitlementDesc) {
		this.entitlementDesc = entitlementDesc;
	}

	public Long getId() {
		return id;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Timestamp getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Timestamp getVisitDateTime() {
		return visitDateTime;
	}

	public void setVisitDateTime(Timestamp visitDateTime) {
		this.visitDateTime = visitDateTime;
	}

	public Timestamp getPrescriptionDateTime() {
		return prescriptionDateTime;
	}

	public void setPrescriptionDateTime(Timestamp prescriptionDateTime) {
		this.prescriptionDateTime = prescriptionDateTime;
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
	
	public String getPrescriptionNo() {
		return prescriptionNo;
	}

	public void setPrescriptionNo(String prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public ResponseWorkListSearch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public ResponseWorkListSearch(Long id, String visitType, 
			Timestamp prescriptionDateTime, String priority,
			String status, String doctorDetail,Timestamp visitDateTime, String uhid, String patientDetail, String prescriptionNo,
			Integer statusId,Integer encounterId) {
		super();
		this.id = id;
		this.visitType = visitType;
		this.prescriptionDateTime = prescriptionDateTime;
		this.priority = priority;
		this.status = status;
		this.doctorDetail = doctorDetail;
		this.visitDateTime = visitDateTime;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.prescriptionNo = prescriptionNo;
		this.statusId = statusId;
		this.encounterId = encounterId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
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

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAgeStringDet() {
		return ageStringDet;
	}

	public void setAgeStringDet(String ageStringDet) {
		this.ageStringDet = ageStringDet;
	}

	public Boolean getStatFlag() {
		return statFlag;
	}

	public void setStatFlag(Boolean statFlag) {
		this.statFlag = statFlag;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getAddedByUser() {
		return addedByUser;
	}

	public void setAddedByUser(Integer addedByUser) {
		this.addedByUser = addedByUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	
}
