package com.param.adt.master.unit.dto;

import java.util.List;

public class NursingStationWardMapperDto 
{
	private Integer nursingStationId;
	
	private Integer nursingWardId;
	
	private String nursingStationCode;
	
	private String nursingStationDesc;
	
	private String createdDate;

	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String updatedDate;

	private Character status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer wardId;
	
	private String wardName;
	
	private Integer offset;
	
	private Integer recordPerPage;
	
	private Character isDeleted;
	
	List<NursingStationWardMapperDto> nursingWardMapperList;

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public Integer getNursingWardId() {
		return nursingWardId;
	}

	public void setNursingWardId(Integer nursingWardId) {
		this.nursingWardId = nursingWardId;
	}

	public String getNursingStationCode() {
		return nursingStationCode;
	}

	public void setNursingStationCode(String nursingStationCode) {
		this.nursingStationCode = nursingStationCode;
	}

	public String getNursingStationDesc() {
		return nursingStationDesc;
	}

	public void setNursingStationDesc(String nursingStationDesc) {
		this.nursingStationDesc = nursingStationDesc;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public List<NursingStationWardMapperDto> getNursingWardMapperList() {
		return nursingWardMapperList;
	}

	public void setNursingWardMapperList(List<NursingStationWardMapperDto> nursingWardMapperList) {
		this.nursingWardMapperList = nursingWardMapperList;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "NursingStationWardMapperDto [nursingStationId=" + nursingStationId + ", nursingWardId=" + nursingWardId
				+ ", nursingStationCode=" + nursingStationCode + ", nursingStationDesc=" + nursingStationDesc
				+ ", createdDate=" + createdDate + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", status=" + status + ", organizationId=" + organizationId
				+ ", unitId=" + unitId + ", wardId=" + wardId + ", wardName=" + wardName + ", offset=" + offset
				+ ", recordPerPage=" + recordPerPage + ", isDeleted=" + isDeleted + ", nursingWardMapperList="
				+ nursingWardMapperList + "]";
	}
	
	
	
}
