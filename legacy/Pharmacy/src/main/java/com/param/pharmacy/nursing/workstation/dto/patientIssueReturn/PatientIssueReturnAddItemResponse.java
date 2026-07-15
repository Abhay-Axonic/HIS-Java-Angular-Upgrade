package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.ReturnReason;
@JsonInclude(Include.NON_NULL)
public class PatientIssueReturnAddItemResponse
{
	private Integer patientId;
	
	private Integer visitType;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer wardId;
	
	private Integer bedId;
	
	private Integer storeId;
	private Integer toStoreId;
	private String toStoreName;
	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getToStoreName() {
		return toStoreName;
	}

	public void setToStoreName(String toStoreName) {
		this.toStoreName = toStoreName;
	}

	private String store;
	
	private List<Object> items;
	
	private List<ReturnReason> returnReason;

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

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public List<Object> getItems() {
		return items;
	}

	public void setItems(List<Object> items2) {
		this.items = items2;
	}

	public List<ReturnReason> getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(List<ReturnReason> returnReason) {
		this.returnReason = returnReason;
	}

	public PatientIssueReturnAddItemResponse()
	{
		super();
	}
	
	public PatientIssueReturnAddItemResponse(Integer patientId, Integer visitType, Integer storeId, String store) {
		super();
		this.patientId = patientId;
		this.visitType = visitType;
		this.storeId = storeId;
		this.store = store;
	}
	
}