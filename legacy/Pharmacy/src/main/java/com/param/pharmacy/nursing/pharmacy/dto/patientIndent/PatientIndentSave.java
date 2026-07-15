package com.param.pharmacy.nursing.pharmacy.dto.patientIndent;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class PatientIndentSave extends CookieObjectDto
{

	private Integer visitType;
	
	private Integer patientId;
	
	private Integer ward;
	
	private Integer bed;

	private Integer storeId;
	
	private Integer toStoreId;
	
	private boolean consignment;
	
	private String remark;
	
	private Integer statusId;
	
	private Integer approvalStatusId;
	
	private Integer doctorId;
	
	private Integer admissionId;
	
	private Integer encounterId;
	private Integer cookieStoreId;
	
	public Integer getCookieStoreId() {
		return cookieStoreId;
	}

	public void setCookieStoreId(Integer cookieStoreId) {
		this.cookieStoreId = cookieStoreId;
	}

	private List<PatientIndentSaveItem> items;

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getWard() {
		return ward;
	}

	public void setWard(Integer ward) {
		this.ward = ward;
	}

	public Integer getBed() {
		return bed;
	}

	public void setBed(Integer bed) {
		this.bed = bed;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public boolean isConsignment() {
		return consignment;
	}

	public void setConsignment(boolean consignment) {
		this.consignment = consignment;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<PatientIndentSaveItem> getItems() {
		return items;
	}

	public void setItems(List<PatientIndentSaveItem> items) {
		this.items = items;
	}
	
}