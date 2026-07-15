package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;

@JsonInclude(Include.NON_NULL)
public class KitIndentDto {


	private Integer indentNo;

	private String indentDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String fromStore;
	
	private Integer fromStoreId;
	
	private String toStore;
	
	private Integer toStoreId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String status;
	
	private String remarks;
	
	private String reason;
	
	private String userName;
	
	private Integer cancelReasonId;
	
	private String cancelReasonNote;
	
	private Integer priorityId;
	
	private String priorityDesc;
	
	private List<KitIndentKitMapperDto> kitList;
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	
	
	public Integer getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(Integer indentNo) {
		this.indentNo = indentNo;
	}

	public String getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(String indentDate) {
		this.indentDate = indentDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getFromStore() {
		return fromStore;
	}

	public void setFromStore(String fromStore) {
		this.fromStore = fromStore;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<KitIndentKitMapperDto> getKitList() {
		return kitList;
	}

	public void setKitList(List<KitIndentKitMapperDto> kitList) {
		this.kitList = kitList;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriorityDesc() {
		return priorityDesc;
	}

	public void setPriorityDesc(String priorityDesc) {
		this.priorityDesc = priorityDesc;
	}
	
	
}
