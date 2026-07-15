package com.param.global.billing.dto;

public class CreditNoteReasonMasterDto {
	
	private Integer creditNoteReasonId;
	
	private String code;
	
	private String reasonDesc;
	
	private Character status;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	public Integer getCreditNoteReasonId() {
		return creditNoteReasonId;
	}
	public void setCreditNoteReasonId(Integer creditNoteReasonId) {
		this.creditNoteReasonId = creditNoteReasonId;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
