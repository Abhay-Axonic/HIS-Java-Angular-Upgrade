package com.param.pharmacy.qms.dto;

public class QMSDeptMasterDto {
	
	private Integer qmsDeptMasterId ;
	
	private String qmsDeptName ;
	
	private String qmsDesc;
	
	private String startLetter ;
	
	private String startNumber ;
	
	private Integer qmsKioskMasterId ;
	
	private Character isReception ;
	
	private Character isVoice ;
	
	private Character status ;
	
	private Integer unitId ;
	
	private Integer organisationId ;
	
	private Integer createdBy ;
	
	private Integer updatedBy ;
	
	private String createdDate ;
	
	private String updatedDate;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	public Integer getQmsDeptMasterId() {
		return qmsDeptMasterId;
	}

	public void setQmsDeptMasterId(Integer qmsDeptMasterId) {
		this.qmsDeptMasterId = qmsDeptMasterId;
	}

	public String getQmsDeptName() {
		return qmsDeptName;
	}

	public void setQmsDeptName(String qmsDeptName) {
		this.qmsDeptName = qmsDeptName;
	}

	public String getStartLetter() {
		return startLetter;
	}

	public void setStartLetter(String startLetter) {
		this.startLetter = startLetter;
	}

	public String getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(String startNumber) {
		this.startNumber = startNumber;
	}

	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public Character getIsReception() {
		return isReception;
	}

	public void setIsReception(Character isReception) {
		this.isReception = isReception;
	}

	public Character getIsVoice() {
		return isVoice;
	}

	public void setIsVoice(Character isVoice) {
		this.isVoice = isVoice;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getQmsDesc() {
		return qmsDesc;
	}

	public void setQmsDesc(String qmsDesc) {
		this.qmsDesc = qmsDesc;
	}
	 
	
}
