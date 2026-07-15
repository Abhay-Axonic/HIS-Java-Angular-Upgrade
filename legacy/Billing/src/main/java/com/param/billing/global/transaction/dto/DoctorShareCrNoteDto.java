package com.param.billing.global.transaction.dto;

import java.util.Date;
import java.util.List;

public class DoctorShareCrNoteDto {
	
	private Integer doctorCrId;
	
	private Integer billId;
	
	private Double totalCredit;
	
	private Integer orgUnitId;
	
	private Integer requestStatus;

	private Integer orgId;

	private Character status;
	
	private Date createdDate;

	private Integer createdBy;

	private Date updatedDate;

	private Integer updatedBy;
	
	private String doctorName;
	
	private List<DoctorShareCrNoteDetailsDto> octorShareCrNoteDetailsDtosList;

	public Integer getDoctorCrId() {
		return doctorCrId;
	}

	public void setDoctorCrId(Integer doctorCrId) {
		this.doctorCrId = doctorCrId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(Double totalCredit) {
		this.totalCredit = totalCredit;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Integer requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<DoctorShareCrNoteDetailsDto> getOctorShareCrNoteDetailsDtosList() {
		return octorShareCrNoteDetailsDtosList;
	}

	public void setOctorShareCrNoteDetailsDtosList(List<DoctorShareCrNoteDetailsDto> octorShareCrNoteDetailsDtosList) {
		this.octorShareCrNoteDetailsDtosList = octorShareCrNoteDetailsDtosList;
	}
	
	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	
}
