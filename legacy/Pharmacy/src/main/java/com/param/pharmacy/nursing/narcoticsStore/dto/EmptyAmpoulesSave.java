package com.param.pharmacy.nursing.narcoticsStore.dto;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class EmptyAmpoulesSave extends CookieObjectDto
{
	private Integer id;
	
	private Integer patientId;
	
	private Integer doctorId;
	
	private Integer priorityId;
	
	private Integer wardId;
	
	private Integer bedId;
	
	private Integer statusId;
	
	private List<EmptyAmpoulesSaveItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<EmptyAmpoulesSaveItem> getItems() {
		return items;
	}

	public void setItems(List<EmptyAmpoulesSaveItem> items) {
		this.items = items;
	}
	
}