package com.param.global.pharmacy.dto;

import java.util.List;

import com.param.global.billing.dto.PharmacyBillCommonDto;

public class PatientConsumableIssueSave extends PharmacyBillCommonDto
{
	private Integer patientIndentId;
	
	private Integer id;
	
	private Integer visitType;
	
	//private Integer admissionId;
	
	//private Integer encounterId;
	
	//private Integer doctorId;
	
	//private Integer storeId;

	private Integer toStoreId;
	
	private Integer carrierId;
	
	//private String remark;
	
	private Integer statusId;
	
	private boolean atCop;
	
	//private Integer patientId;
	
	private List<PatientConsumableIssueSaveItem> items;

	public Integer getPatientIndentId() {
		return patientIndentId;
	}

	public void setPatientIndentId(Integer patientIndentId) {
		this.patientIndentId = patientIndentId;
	}

	public Integer getVisitType() {
		return visitType;
	}
	
	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}
	
	/*public Integer getAdmissionId() {
		return admissionId;
	}
	
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}*/
	
	/*public Integer getEncounterId() {
		return encounterId;
	}
	
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}*/

	/*public Integer getDoctorId() {
		return doctorId;
	}
	
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}*/

	/*public Integer getStoreId() {
		return storeId;
	}
	
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}*/

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	/*public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}*/

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<PatientConsumableIssueSaveItem> getItems() {
		return items;
	}

	public void setItems(List<PatientConsumableIssueSaveItem> items) {
		this.items = items;
	}

	public boolean getAtCop() {
		return atCop;
	}

	public void setAtCop(boolean atCop) {
		this.atCop = atCop;
	}

	/*public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}*/
	
	
}