package com.param.pharmacy.ip.dto.pharmacology;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class PharmacologyValidationSearch extends CookieObjectDto{
	private Date fromDate;

	private Date toDate;

	private Integer visitType;

	private String doctorName;

	private String patientName;

	private Integer patientId;

	private List<Integer> visitTypeIds;

	private String uhid;

	private Integer statusId;

	private String wardName;

	private Integer admissionId;
	
	private Integer encounterId;
	
	private Integer unitId;
	
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}
	
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

	public void setVisitTypeId(Integer visitType) {
		this.visitType = visitType;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public List<Integer> getVisitTypeIds() {
		return visitTypeIds;
	}

	public void setVisitTypeIds(List<Integer> visitTypeIds) {
		this.visitTypeIds = visitTypeIds;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

}
