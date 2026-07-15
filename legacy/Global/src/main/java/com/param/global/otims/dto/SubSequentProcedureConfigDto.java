package com.param.global.otims.dto;

import java.util.List;

public class SubSequentProcedureConfigDto {
	private Integer subSequentProcConfigId;
	
	private Integer specialityId;
	
	private char status;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private String specialityName;
	
	private List<SubSequentDetailsDto> subSequentDetailsList;
	
	public Integer getSubSequentProcConfigId() {
		return subSequentProcConfigId;
	}

	public void setSubSequentProcConfigId(Integer subSequentProcConfigId) {
		this.subSequentProcConfigId = subSequentProcConfigId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public List<SubSequentDetailsDto> getSubSequentDetailsList() {
		return subSequentDetailsList;
	}

	public void setSubSequentDetailsList(List<SubSequentDetailsDto> subSequentDetailsList) {
		this.subSequentDetailsList = subSequentDetailsList;
	}

	

	
}
