package com.param.pharmacy.nursing.discharge.dto;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MedicationReconciliationSearchDto {
	
	private Long prescriptionId;

	private String  patientUHID;
	
	private String  patientDetail;
	
	private String doctorDetail;
	
	private String wardName;
	
	private String bedNo;
	private Date dischargedate;
	private Integer dischargestatusid;
	private String dischargedesc;
	private Integer patientId;

	private BigInteger acceptedQuantity;

	public Integer getDischargestatusid() {
		return dischargestatusid;
	}

	public void setDischargestatusid(Integer dischargestatusid) {
		this.dischargestatusid = dischargestatusid;
	}

	public String getDischargedesc() {
		return dischargedesc;
	}

	public void setDischargedesc(String dischargedesc) {
		this.dischargedesc = dischargedesc;
	}

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public Date getDischargedate() {
		return dischargedate;
	}

	public void setDischargedate(Date dischargedate) {
		this.dischargedate = dischargedate;
	}

	private String status;
	
	private Integer statusId;
	
	
	public MedicationReconciliationSearchDto() {
		super();
	}

	public MedicationReconciliationSearchDto(Long prescriptionId, String patientUHID, String patientDetail,
			String doctorDetail,String status, Integer statusId) {
		super();
		this.prescriptionId = prescriptionId;
		this.patientUHID = patientUHID;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.status = status;
		this.statusId = statusId;
	}
	public MedicationReconciliationSearchDto(Long prescriptionId, String patientUHID, String patientDetail,
			String doctorDetail,String status, Integer statusId, Date dischargeDate, Integer dischargestatusid, String dischargedesc) {
		super();
		this.prescriptionId = prescriptionId;
		this.patientUHID = patientUHID;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.status = status;
		this.statusId = statusId;
		this.dischargedate = dischargeDate;
		this.dischargestatusid = dischargestatusid;
		this.dischargedesc = dischargedesc;
	}
	public Long getPrescriptionDetailId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getPatientUHID() {
		return patientUHID;
	}

	public void setPatientUHID(String patientUHID) {
		this.patientUHID = patientUHID;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public BigInteger getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(BigInteger acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	
	
	
	
}
