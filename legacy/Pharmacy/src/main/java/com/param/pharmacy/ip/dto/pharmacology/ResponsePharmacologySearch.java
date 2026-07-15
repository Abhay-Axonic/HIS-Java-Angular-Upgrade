package com.param.pharmacy.ip.dto.pharmacology;

import java.math.BigInteger;
import java.util.Date;

public class ResponsePharmacologySearch {
	
	private BigInteger id;
	
	private String visitType;
	
	private Date visitDate;
	
	private Date prescriptionDateTime;
	
	private String uhid;
	
	private String patientDetail;
	
	private String doctorDetail;
	
	private String priority;
	
	private String status;
	
	private String wardName;
	   
	private String bedNo;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Date getPrescriptionDateTime() {
		return prescriptionDateTime;
	}

	public void setPrescriptionDateTime(Date prescriptionDateTime) {
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

	public ResponsePharmacologySearch(BigInteger id, String visitType, Date visitDate, Date prescriptionDateTime,
			String uhid, String patientDetail, String doctorDetail, String priority, String status, String wardName,
			String bedNo) {
		super();
		this.id = id;
		this.visitType = visitType;
		this.visitDate = visitDate;
		this.prescriptionDateTime = prescriptionDateTime;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.priority = priority;
		this.status = status;
		this.wardName = wardName;
		this.bedNo = bedNo;
	}

}
