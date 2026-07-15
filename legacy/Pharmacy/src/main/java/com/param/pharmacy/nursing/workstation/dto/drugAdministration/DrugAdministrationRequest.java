package com.param.pharmacy.nursing.workstation.dto.drugAdministration;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class DrugAdministrationRequest extends CookieObjectDto
{
	 private Long id;
	
	 private Date date;
	 
	 private Date toDate;
	
	 private Integer patientId;
	 
	 private Integer admissionId;
	 
	 private Integer encounterId;
	 
	 private Integer visitTypeId;
	 
	 private Long prescriptionId;
	 private String dateString;

	 private Integer orderQty;
	 private Integer acceptQty;
	 
	 private Integer rejectedQuantity;
	 
	 private List<DrugAdministrationRequestItem> items;
	 
	 private Boolean antibioticTest;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	
	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	
	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DrugAdministrationRequestItem> getItems() {
		return items;
	}

	public void setItems(List<DrugAdministrationRequestItem> items) {
		this.items = items;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getAcceptQty() {
		return acceptQty;
	}

	public void setAcceptQty(Integer acceptQty) {
		this.acceptQty = acceptQty;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Boolean getAntibioticTest() {
		return antibioticTest;
	}

	public void setAntibioticTest(Boolean antibioticTest) {
		this.antibioticTest = antibioticTest;
	}

	
	
}