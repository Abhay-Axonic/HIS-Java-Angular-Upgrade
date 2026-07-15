package com.param.lis.global.dto;


public class TurnAroundTimeMasterDto {

	private Integer tatTimeId;
	private String tatTimeName;
	private Character status;
	private Integer createdBy;
	private Long createdDate;
	private Integer updatedBy;
	private Long updatedDate;
	private Integer orgId;

	public Integer getTatTimeId() {
		return tatTimeId;
	}

	public void setTatTimeId(Integer tatTimeId) {
		this.tatTimeId = tatTimeId;
	}

	public String getTatTimeName() {
		return tatTimeName;
	}

	public void setTatTimeName(String tatTimeName) {
		this.tatTimeName = tatTimeName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
	  this.status = (status == '\u0000') ? 'A' : status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	
	
	
}
