package com.param.pharmacy.nursing.workstation.dto.drugAdministration;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;

public class DrugAdministrationRequestItem
{
	 private Integer id;
	
	 private String administrationTime;
	 
	 private Integer reasonId;
	 
	 private Long prescriptionDetailId;
	 
	 private String note;
	 
	 private Integer frequencyDetailId;
	 
	 private Integer batchId;
	 private Date addedDate;

	 private Integer orderQty;
	 private Integer acceptQty;
	 
	 private String startAdministrationTime;
		
	 private String stopAdministrationTime;
		
	 private String startAdministrationDate;
		
	 private String stopAdministrationDate;
		
	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Long getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Long prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getFrequencyDetailId() {
		return frequencyDetailId;
	}

	public void setFrequencyDetailId(Integer frequencyDetailId) {
		this.frequencyDetailId = frequencyDetailId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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

	public String getStartAdministrationTime() {
		return startAdministrationTime;
	}

	public void setStartAdministrationTime(String startAdministrationTime) {
		this.startAdministrationTime = startAdministrationTime;
	}

	public String getStopAdministrationTime() {
		return stopAdministrationTime;
	}

	public void setStopAdministrationTime(String stopAdministrationTime) {
		this.stopAdministrationTime = stopAdministrationTime;
	}

	public String getStartAdministrationDate() {
		return startAdministrationDate;
	}

	public void setStartAdministrationDate(String startAdministrationDate) {
		this.startAdministrationDate = startAdministrationDate;
	}

	public String getStopAdministrationDate() {
		return stopAdministrationDate;
	}

	public void setStopAdministrationDate(String stopAdministrationDate) {
		this.stopAdministrationDate = stopAdministrationDate;
	}
	
	
}