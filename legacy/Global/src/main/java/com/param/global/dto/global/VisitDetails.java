package com.param.global.dto.global;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VisitDetails
{
	private Integer doctorId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private String visitNo;
	
	private Date addedDate;
	
	private Date encounterDate;
	

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	
	
	public Date getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Date encounterDate) {
		this.encounterDate = encounterDate;
	}

	public VisitDetails() {
		super();
	}
	
	public VisitDetails(Integer doctorId, String visitNo) {
		super();
		this.doctorId = doctorId;
		this.visitNo = visitNo;
	}

	
	public VisitDetails(Integer doctorId, String visitNo , Date encounterDate) {
		super();
		this.doctorId = doctorId;
		this.visitNo = visitNo;
		this.encounterDate = encounterDate;
	}
	
}