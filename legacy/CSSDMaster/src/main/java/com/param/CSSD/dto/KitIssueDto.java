package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*@JsonInclude(Include.NON_NULL)*/
public class KitIssueDto {

	private Integer issueNo;

	private String createdDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Integer withIndent;
	
	private Boolean againstSterilizationRequest;
	
	private Integer indentNo;
	
	private Integer requestNo;
	
	private String indentDate;
	
	private String requestDate;
	
	private String fromStore;
	
	private Integer fromStoreId;
	
	private String toStore;
	
	private Integer toStoreId;
	
	private String status;
	
	private String remark;
	
	private String carrier;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer cancelReasonId;
	
	private String cancelReasonNote;
	
	private List<IssuedKitTransitionMapperDto> subKitList;
	
	private List<KitIssueKitMapperDto> kitList;

	public Integer getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(Integer issueNo) {
		this.issueNo = issueNo;
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

	public Integer getWithIndent() {
		return withIndent;
	}

	public void setWithIndent(Integer withIndent) {
		this.withIndent = withIndent;
	}

	public Integer getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(Integer indentNo) {
		this.indentNo = indentNo;
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

	public List<KitIssueKitMapperDto> getKitList() {
		return kitList;
	}

	public void setKitList(List<KitIssueKitMapperDto> kitList) {
		this.kitList = kitList;
	}

	public String getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(String indentDate) {
		this.indentDate = indentDate;
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

	public Boolean getAgainstSterilizationRequest() {
		return againstSterilizationRequest;
	}

	public void setAgainstSterilizationRequest(Boolean againstSterilizationRequest) {
		this.againstSterilizationRequest = againstSterilizationRequest;
	}

	public Integer getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Integer requestNo) {
		this.requestNo = requestNo;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public List<IssuedKitTransitionMapperDto> getSubKitList() {
		return subKitList;
	}

	public void setSubKitList(List<IssuedKitTransitionMapperDto> subKitList) {
		this.subKitList = subKitList;
	}

	
	

}
