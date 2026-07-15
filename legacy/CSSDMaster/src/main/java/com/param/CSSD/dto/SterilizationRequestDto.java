package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class SterilizationRequestDto {

	private Integer requestNo;

	private Integer requestingStoreId;

	private Integer issueingStoreId;

	private Integer organizationId;

	private Integer unitId;

	private Integer createdBy;

	private Integer updatedBy;

	private String status;

	private String remark;

	private String carrier;
	
	private String requestingStore;
	
	private String issueingStore;

	private String createdDate;

	private String updatedDate;
	
	private Integer cancelReasonId;
	
	private String cancelReasonNote;
	
	private List<SterilizationRequestTransitionMapperDto> kitList;

	public Integer getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Integer requestNo) {
		this.requestNo = requestNo;
	}

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public Integer getIssueingStoreId() {
		return issueingStoreId;
	}

	public void setIssueingStoreId(Integer issueingStoreId) {
		this.issueingStoreId = issueingStoreId;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
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

	public List<SterilizationRequestTransitionMapperDto> getKitList() {
		return kitList;
	}

	public void setKitList(List<SterilizationRequestTransitionMapperDto> kitList) {
		this.kitList = kitList;
	}

	public String getRequestingStore() {
		return requestingStore;
	}

	public void setRequestingStore(String requestingStore) {
		this.requestingStore = requestingStore;
	}

	public String getIssueingStore() {
		return issueingStore;
	}

	public void setIssueingStore(String issueingStore) {
		this.issueingStore = issueingStore;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelReasonNote() {
		return cancelReasonNote;
	}

	public void setCancelReasonNote(String cancelReasonNote) {
		this.cancelReasonNote = cancelReasonNote;
	}
	
	

}
