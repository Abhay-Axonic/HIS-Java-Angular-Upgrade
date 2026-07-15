package com.param.procurement.consignment.dto.consignmentReturn;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ImplantReturnSave extends CookieObjectDto{
	
	private Integer patientId;
	
	private Integer visitType;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer wardId;
	
	private Integer bedId;
	
	private Integer storeId;
	
	private Integer statusId;
	
	private List<ImplantReturnSaveItem> items;

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

	public List<ImplantReturnSaveItem> getItems() {
		return items;
	}

	public void setItems(List<ImplantReturnSaveItem> items) {
		this.items = items;
	}

}
