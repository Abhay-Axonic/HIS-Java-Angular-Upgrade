 package com.param.pharmacy.ip.dto.assignPicker;

import java.util.List;

import javax.persistence.Column;

import com.param.service.global.model.CookieObjectDto;

public class AssignPickerSave extends CookieObjectDto
{
	private Long prescriptionId;
	
	private Integer pickerId;
	
	private Integer statusId;
	
	private Integer id;
	
	private Integer doctorId;
	  
	private Integer visitType;
	
	private Integer admissionId;
	  
	private Integer encounterId;
	
	private Integer storeId;
	private Integer itemId;
	
    private Boolean isImplant;
	
	List<AssignPickerDetailItem> items;
	
	
	public AssignPickerSave()
	{
		super();
	}

	public Long getPrescriptionId() {
	
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getPickerId() {
		return pickerId;
	}

	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<AssignPickerDetailItem> getItems() {
		return items;
	}

	public void setItems(List<AssignPickerDetailItem> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	
    public Boolean getIsImplant() {
        return isImplant;
    }
    public void setIsImplant(Boolean isImplant) {
        this.isImplant = isImplant;
    }

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	
}