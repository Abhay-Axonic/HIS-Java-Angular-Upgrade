package com.param.global.otims.dto;

import java.util.List;

public class PostOperativeDetailsDto {

	private Integer postOperativeId;
	
	private Integer proReqId;
	
	private String postOpNote;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private List<OTCompletionNoteDto> oTCompletionNoteDtoList;

	public Integer getPostOperativeId() {
		return postOperativeId;
	}

	public void setPostOperativeId(Integer postOperativeId) {
		this.postOperativeId = postOperativeId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getPostOpNote() {
		return postOpNote;
	}

	public void setPostOpNote(String postOpNote) {
		this.postOpNote = postOpNote;
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


	public List<OTCompletionNoteDto> getoTCompletionNoteDtoList() {
		return oTCompletionNoteDtoList;
	}

	public void setoTCompletionNoteDtoList(List<OTCompletionNoteDto> oTCompletionNoteDtoList) {
		this.oTCompletionNoteDtoList = oTCompletionNoteDtoList;
	}
	
	

}
