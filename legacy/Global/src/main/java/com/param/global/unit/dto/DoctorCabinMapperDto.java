package com.param.global.unit.dto;


import java.util.List;


public class DoctorCabinMapperDto {
	
	private Integer doctorCabinMpprId;
	
	private Integer cabinId;
	
	private Integer doctorId;
	
	private Integer organisationId;
	
	private Integer unitId;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;

	private Character isDeleted;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	private String doctorName;
	
	private String cabinDesc;

	private List<Integer> selectDoctorCabinMapperList;

	private Character isRMO;

	public Integer getDoctorCabinMpprId() {
		return doctorCabinMpprId;
	}

	public void setDoctorCabinMpprId(Integer doctorCabinMpprId) {
		this.doctorCabinMpprId = doctorCabinMpprId;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	
	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getCabinDesc() {
		return cabinDesc;
	}

	public void setCabinDesc(String cabinDesc) {
		this.cabinDesc = cabinDesc;
	}

	public List<Integer> getSelectDoctorCabinMapperList() {
		return selectDoctorCabinMapperList;
	}

	public void setSelectDoctorCabinMapperList(List<Integer> selectDoctorCabinMapperList) {
		this.selectDoctorCabinMapperList = selectDoctorCabinMapperList;
	}

	public Character getIsRMO() {
		return isRMO;
	}

	public void setIsRMO(Character isRMO) {
		this.isRMO = isRMO;
	}

	
	
	
}
