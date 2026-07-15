package com.param.adt.master.unit.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.param.adt.master.global.dto.VisitTypeMasterDto;

public class CorporateStatementChecklistMasterDto 
{
	private Integer corporateStatementChecklistId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String checkListCode;
	
	private String checkListDesc;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private List<VisitTypeMasterDto> visitTypeMasterDtosList;
	
	private List<VisitTypeMasterDto> newlyAddedVisitTypeList;
	
	private List<VisitTypeMasterDto> removedVisitTypeList;
	
	private List<Integer> visitTypeListIds;
	
	private Integer visitTypeId;
	
	

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public List<Integer> getVisitTypeListIds() {
		return visitTypeListIds;
	}

	public void setVisitTypeListIds(List<Integer> visitTypeListIds) {
		this.visitTypeListIds = visitTypeListIds;
	}


	public List<VisitTypeMasterDto> getRemovedVisitTypeList() {
		return removedVisitTypeList;
	}

	public void setRemovedVisitTypeList(List<VisitTypeMasterDto> removedVisitTypeList) {
		this.removedVisitTypeList = removedVisitTypeList;
	}

	public List<VisitTypeMasterDto> getNewlyAddedVisitTypeList() {
		return newlyAddedVisitTypeList;
	}

	public void setNewlyAddedVisitTypeList(List<VisitTypeMasterDto> newlyAddedVisitTypeList) {
		this.newlyAddedVisitTypeList = newlyAddedVisitTypeList;
	}

	public List<VisitTypeMasterDto> getVisitTypeMasterDtosList() {
		return visitTypeMasterDtosList;
	}

	public void setVisitTypeMasterDtosList(List<VisitTypeMasterDto> visitTypeMasterDtosList) {
		this.visitTypeMasterDtosList = visitTypeMasterDtosList;
	}

	public Integer getCorporateStatementChecklistId() {
		return corporateStatementChecklistId;
	}

	public void setCorporateStatementChecklistId(Integer corporateStatementChecklistId) {
		this.corporateStatementChecklistId = corporateStatementChecklistId;
	}

	public String getCheckListCode() {
		return checkListCode;
	}

	public void setCheckListCode(String checkListCode) {
		this.checkListCode = checkListCode;
	}

	public String getCheckListDesc() {
		return checkListDesc;
	}

	public void setCheckListDesc(String checkListDesc) {
		this.checkListDesc = checkListDesc;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}



	
	
}
