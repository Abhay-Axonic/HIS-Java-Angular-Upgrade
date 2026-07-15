package com.param.bloodBank.dto;

import java.util.Date;

import javax.persistence.Column;

public class BloodGroupMasterDto {
	private Integer bloodGroupId;
	
	private String bloodGroupCode;
	
	private String bloodGroupDesc;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;

	/**
	 * @return the bloodGroupId
	 */
	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	/**
	 * @param bloodGroupId the bloodGroupId to set
	 */
	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	/**
	 * @return the bloodGroupCode
	 */
	public String getBloodGroupCode() {
		return bloodGroupCode;
	}

	/**
	 * @param bloodGroupCode the bloodGroupCode to set
	 */
	public void setBloodGroupCode(String bloodGroupCode) {
		this.bloodGroupCode = bloodGroupCode;
	}

	/**
	 * @return the bloodGroupDesc
	 */
	public String getBloodGroupDesc() {
		return bloodGroupDesc;
	}

	/**
	 * @param bloodGroupDesc the bloodGroupDesc to set
	 */
	public void setBloodGroupDesc(String bloodGroupDesc) {
		this.bloodGroupDesc = bloodGroupDesc;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	

	/**
	 * @return the status
	 */
	public Character getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Character status) {
		this.status = status;
	}

	/**
	 * @return the updatedBy
	 */
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedDate
	 */
	
	/**
	 * @return the unitId
	 */
	public Integer getUnitId() {
		return unitId;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the updatedDate
	 */
	public String getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the noOfRecordsPerPage
	 */
	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	/**
	 * @param noOfRecordsPerPage the noOfRecordsPerPage to set
	 */
	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
}
