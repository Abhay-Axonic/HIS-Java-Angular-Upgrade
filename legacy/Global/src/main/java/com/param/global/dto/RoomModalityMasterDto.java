package com.param.global.dto;


public class RoomModalityMasterDto {
	
	private Integer roomModalityMasterId;
	
	private Integer roomModalityId;
	
	private Integer specialityId;
	
	private Integer subSpecialityId;
	
	private Integer modalityId;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Character status;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Integer createdBy;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String modalityDesc;
	private String specialityName;
	private String subSpecialityName;
	private String roomName;
	

	public Integer getRoomModalityMasterId() {
		return roomModalityMasterId;
	}

	public Integer getRoomModalityId() {
		return roomModalityId;
	}

	

	public Integer getModalityId() {
		return modalityId;
	}

	

	public Integer getOrganizationId() {
		return organizationId;
	}

	public Character getStatus() {
		return status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setRoomModalityMasterId(Integer roomModalityMasterId) {
		this.roomModalityMasterId = roomModalityMasterId;
	}

	public void setRoomModalityId(Integer roomModalityId) {
		this.roomModalityId = roomModalityId;
	}
	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}


	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getModalityDesc() {
		return modalityDesc;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setModalityDesc(String modalityDesc) {
		this.modalityDesc = modalityDesc;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	
}
