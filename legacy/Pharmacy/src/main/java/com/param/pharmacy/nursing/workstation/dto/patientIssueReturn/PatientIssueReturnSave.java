package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.List;

public class PatientIssueReturnSave extends PatientIssueReturnSaveItem
{
	private Integer patientId;
	
	private Integer visitType;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer wardId;
	
	private Integer bedId;
	
	private Integer storeId;
	private Integer toStoreId;
	
	private String returnDate;
	
	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	private Integer statusId;
	
	private Integer id;
	
	private List<PatientIssueReturnSaveItem> items;
	private boolean fromMedication;

	private Boolean fromMedicineIssue;

	public boolean getFromMedication() {
		return fromMedication;
	}

	public void setFromMedication(boolean fromMedication) {
		this.fromMedication = fromMedication;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	
	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<PatientIssueReturnSaveItem> getItems() {
		return items;
	}

	public void setItems(List<PatientIssueReturnSaveItem> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}


	public Boolean getFromMedicineIssue() {
		return fromMedicineIssue;
	}

	public void setFromMedicineIssue(Boolean fromMedicineIssue) {
		this.fromMedicineIssue = fromMedicineIssue;
	}
}