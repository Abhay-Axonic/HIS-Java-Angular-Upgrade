package com.param.pharmacy.qms.dto;

import java.util.List;

public class QMSCounterMasterDto {
	
	private Integer qmsCounterMasterId;
	
	private Integer qmsKioskMasterId;
	
	private String qmsCounterCode;
	
	private String qmsCounterDesc;
	
	private String qmsDeptName;
	
	private Integer qmsDeptMasterId;
	
	private Character status ;
	
	private Integer unitId ;
	
	private Integer organisationId ;
	
	private Integer createdBy ;
	
	private Integer updatedBy ;
	
	private String createdDate ;
	
	private String updatedDate;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String qmsDesc;
	
	private Integer qmsCounterId;
	
	private List<QMSCounterMasterDto> departmentList;
	
	private Integer id;

	public Integer getQmsCounterMasterId() {
		return qmsCounterMasterId;
	}

	public void setQmsCounterMasterId(Integer qmsCounterMasterId) {
		this.qmsCounterMasterId = qmsCounterMasterId;
	}

	public String getQmsCounterCode() {
		return qmsCounterCode;
	}

	public void setQmsCounterCode(String qmsCounterCode) {
		this.qmsCounterCode = qmsCounterCode;
	}

	public String getQmsCounterDesc() {
		return qmsCounterDesc;
	}

	public void setQmsCounterDesc(String qmsCounterDesc) {
		this.qmsCounterDesc = qmsCounterDesc;
	}

	public Integer getQmsDeptMasterId() {
		return qmsDeptMasterId;
	}

	public void setQmsDeptMasterId(Integer qmsDeptMasterId) {
		this.qmsDeptMasterId = qmsDeptMasterId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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

	public String getQmsDeptName() {
		return qmsDeptName;
	}

	public void setQmsDeptName(String qmsDeptName) {
		this.qmsDeptName = qmsDeptName;
	}

	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public String getQmsDesc() {
		return qmsDesc;
	}

	public void setQmsDesc(String qmsDesc) {
		this.qmsDesc = qmsDesc;
	}

	public Integer getQmsCounterId() {
		return qmsCounterId;
	}

	public void setQmsCounterId(Integer qmsCounterId) {
		this.qmsCounterId = qmsCounterId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<QMSCounterMasterDto> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<QMSCounterMasterDto> departmentList) {
		this.departmentList = departmentList;
	}
		
}
