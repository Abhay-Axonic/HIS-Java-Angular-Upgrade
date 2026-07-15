package com.param.global.otims.dto;

import java.util.List;

public class OTTransferRequestDto {

	private Integer transferRequestId ;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer scheduleId;
	
	private Integer theatreId ;
	
	private String transferTime;
	
	private String surgeryTime;
	
	private Integer otTransferStatus;
	
	private char status;
			
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String uhidNumber;
	
	private String patientName;
	
	private String theatreName;
	
	private String age;
	
	private String preferedDateTime;
	
	private String procedureName;
	
	private String otTransferStatusName;
	
	private String wardName;
	
	private Integer reasonId;
	
	private String reasonNote;
	
	private Integer genderId;
	
	private String genderCode;
	
	private String imagePath;

	private String preferTime;
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	private List<Integer> wardList;
	
	public List<Integer> getWardList() {
		return wardList;
	}

	public void setWardList(List<Integer> wardList) {
		this.wardList = wardList;
	}

	public Integer getTransferRequestId() {
		return transferRequestId;
	}

	public void setTransferRequestId(Integer transferRequestId) {
		this.transferRequestId = transferRequestId;
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

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Integer theatreId) {
		this.theatreId = theatreId;
	}


	public String getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}

	public Integer getOtTransferStatus() {
		return otTransferStatus;
	}

	public void setOtTransferStatus(Integer otTransferStatus) {
		this.otTransferStatus = otTransferStatus;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getSurgeryTime() {
		return surgeryTime;
	}

	public void setSurgeryTime(String surgeryTime) {
		this.surgeryTime = surgeryTime;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPreferedDateTime() {
		return preferedDateTime;
	}

	public void setPreferedDateTime(String preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getOtTransferStatusName() {
		return otTransferStatusName;
	}

	public void setOtTransferStatusName(String otTransferStatusName) {
		this.otTransferStatusName = otTransferStatusName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getReasonNote() {
		return reasonNote;
	}

	public void setReasonNote(String reasonNote) {
		this.reasonNote = reasonNote;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPreferTime() {
		return preferTime;
	}

	public void setPreferTime(String preferTime) {
		this.preferTime = preferTime;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	
}
