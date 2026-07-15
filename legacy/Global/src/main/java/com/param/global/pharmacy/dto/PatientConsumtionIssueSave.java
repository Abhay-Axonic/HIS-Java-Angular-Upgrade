package com.param.global.pharmacy.dto;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;




public class PatientConsumtionIssueSave extends CookieObjectDto{
	
private Integer implantIndentId;
	
	private Integer id;
	
	private Integer visitType;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private Integer doctorId;
	
	private Integer storeId;
	
	private Integer carrierId;
	
	private String remark;
	
	private Integer statusId;
	
	private Integer patientId;
	
	private List<PatientConsumtionSaveItem> items;

	public Integer getImplantIndentId() {
		return implantIndentId;
	}

	public void setImplantIndentId(Integer implantIndentId) {
		this.implantIndentId = implantIndentId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<PatientConsumtionSaveItem> getItems() {
		return items;
	}

	public void setItems(List<PatientConsumtionSaveItem> items) {
		this.items = items;
	}
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

}
