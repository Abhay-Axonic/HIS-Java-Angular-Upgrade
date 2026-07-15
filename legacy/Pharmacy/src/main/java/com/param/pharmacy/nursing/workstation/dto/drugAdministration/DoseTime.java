package com.param.pharmacy.nursing.workstation.dto.drugAdministration;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class DoseTime
{
	 private Integer id; 
	 
	 private Integer drugAdministrationId;
	
	 private Timestamp administrationTime;
	 
	 private Timestamp preBufferAdministrationTime;
	 
	 private Timestamp postBufferAdministrationTime;
	 
	 private Timestamp actualAdministrationTime; 
	 
	 private Integer reasonId;
	 
	 private String reason;
	 
	 private String note;

	public Timestamp getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(Timestamp administrationTime) {
		this.administrationTime = administrationTime;
	}

	public Timestamp getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(Timestamp preBufferAdministrationTime) {
		this.preBufferAdministrationTime = preBufferAdministrationTime;
	}

	public Timestamp getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(Timestamp postBufferAdministrationTime) {
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDrugAdministrationId() {
		return drugAdministrationId;
	}

	public void setDrugAdministrationId(Integer drugAdministrationId) {
		this.drugAdministrationId = drugAdministrationId;
	}

	public Timestamp getActualAdministrationTime() {
		return actualAdministrationTime;
	}

	public void setActualAdministrationTime(Timestamp actualAdministrationTime) {
		this.actualAdministrationTime = actualAdministrationTime;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public DoseTime(Integer id, Timestamp administrationTime,
			Timestamp preBufferAdministrationTime, Timestamp postBufferAdministrationTime,
			Timestamp actualAdministrationTime, Integer reasonId, String reason, String note) {
		super();
		this.id = id;
		this.administrationTime = administrationTime;
		this.preBufferAdministrationTime = preBufferAdministrationTime;
		this.postBufferAdministrationTime = postBufferAdministrationTime;
		this.actualAdministrationTime = actualAdministrationTime;
		this.reasonId = reasonId;
		this.reason = reason;
		this.note = note;
	}

	public DoseTime(Integer id, Timestamp administrationTime, Timestamp preBufferAdministrationTime,
			Timestamp postBufferAdministrationTime) {
		super();
		this.id = id;
		this.administrationTime = administrationTime;
		this.preBufferAdministrationTime = preBufferAdministrationTime;
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public DoseTime() {
		super();
	}
	 
}