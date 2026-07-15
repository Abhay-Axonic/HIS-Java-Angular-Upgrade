 package com.param.pharmacy.ip.dto.pharmacology;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class PharmacologyUpdate extends CookieObjectDto
{
	private Long id;
	
	private boolean isValidate;
	
	private Integer statusId;
	
	private Integer visitType;
	
	private Integer doctorId;
	
	private Integer admissionId;
	
	private Integer encounterId;
	private Integer infusionTotalVolume;

	private List<PharmacologyUpdateItem> items;
	
	private Integer issueId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isValidate() {
		return isValidate;
	}

	public void setValidate(boolean isValidate) {
		this.isValidate = isValidate;
	}

	public List<PharmacologyUpdateItem> getItems() {
		return items;
	}

	public void setItems(List<PharmacologyUpdateItem> items) {
		this.items = items;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
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

	public Integer getInfusionTotalVolume() {
		return infusionTotalVolume;
	}

	public void setInfusionTotalVolume(Integer infusionTotalVolume) {
		this.infusionTotalVolume = infusionTotalVolume;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	
}