package com.param.global.unit.dto;

import java.util.Date;

public class UHIDMergeDto {
	
	
	private Integer organizationId;
	
	private Integer userId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private String requestDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private Character isUHIDMergeApproval;
	
	public Integer discountRightId;
	
	private String username;
	
	private Integer fromPatientId;
	
	private Integer toPatientId;
	
	private Integer requestFrom;
	
	private Integer requestTo;
	
	private Integer requestType;
	
	private Integer requestTouserId;
	
	private Integer reqStatus;
	
	private Integer organisationId;
	
	
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	
	public Integer getReqStatus() {
		return reqStatus;
	}
	public void setReqStatus(Integer reqStatus) {
		this.reqStatus = reqStatus;
	}
	public Integer getRequestTouserId() {
		return requestTouserId;
	}
	public void setRequestTouserId(Integer requestTouserId) {
		this.requestTouserId = requestTouserId;
	}
	public Integer getRequestType() {
		return requestType;
	}
	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}
	public Integer getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	
	public Integer getFromPatientId() {
		return fromPatientId;
	}
	public void setFromPatientId(Integer fromPatientId) {
		this.fromPatientId = fromPatientId;
	}
	public Integer getToPatientId() {
		return toPatientId;
	}
	public void setToPatientId(Integer toPatientId) {
		this.toPatientId = toPatientId;
	}
	public Integer getRequestFrom() {
		return requestFrom;
	}
	public void setRequestFrom(Integer requestFrom) {
		this.requestFrom = requestFrom;
	}
	public Integer getRequestTo() {
		return requestTo;
	}
	public void setRequestTo(Integer requestTo) {
		this.requestTo = requestTo;
	}
	
	public Integer getDiscountRightId() {
		return discountRightId;
	}
	public void setDiscountRightId(Integer discountRightId) {
		this.discountRightId = discountRightId;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
	public Character getIsUHIDMergeApproval() {
		return isUHIDMergeApproval;
	}
	public void setIsUHIDMergeApproval(Character isUHIDMergeApproval) {
		this.isUHIDMergeApproval = isUHIDMergeApproval;
	}
	
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
	
	
}
