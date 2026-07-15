package com.param.adt.master.unit.dto;

import java.util.List;

import com.param.adt.master.global.dto.VisitTypeMasterDto;

public class BillingBedCategoryMasterDto 
{
	private int billingBedCategoryId;
	
	private Integer bedCategoryId;

	private String billingBedCode;
	
	private String billingBedDesc;
	
	private char status;
	
	private int createdBy;
	
	private String createdDate;
	
	private int updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer hierarchyId;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	private String hierarchyDesc;
	
	private Character isPeriodicBilling;
	
	private List<VisitTypeMasterDto> visitTypeMasterDtosList;
	
	private List<VisitTypeMasterDto> newlyAddedVisitTypeList;
	
	private List<VisitTypeMasterDto> removedVisitTypeList;
	
	private List<Integer> visitTypeListIds;
	
	private Integer visitTypeId;
	
	private boolean isGstApplicable;
	
	private Integer gstAmountRange;
	
	private Integer gstPercentage;
	
	

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

	public Character getIsPeriodicBilling() {
		return isPeriodicBilling;
	}

	public void setIsPeriodicBilling(Character isPeriodicBilling) {
		this.isPeriodicBilling = isPeriodicBilling;
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

	public int getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(int billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public String getBillingBedCode() {
		return billingBedCode;
	}

	public void setBillingBedCode(String billingBedCode) {
		this.billingBedCode = billingBedCode;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
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

	public Integer getHierarchyId() {
		return hierarchyId;
	}

	public void setHierarchyId(Integer hierarchyId) {
		this.hierarchyId = hierarchyId;
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

	public String getHierarchyDesc() {
		return hierarchyDesc;
	}

	public void setHierarchyDesc(String hierarchyDesc) {
		this.hierarchyDesc = hierarchyDesc;
	}

	public boolean getIsGstApplicable() {
		return isGstApplicable;
	}

	public void setIsGstApplicable(boolean isGstApplicable) {
		this.isGstApplicable = isGstApplicable;
	}

	public Integer getGstAmountRange() {
		return gstAmountRange;
	}

	public void setGstAmountRange(Integer gstAmountRange) {
		this.gstAmountRange = gstAmountRange;
	}

	public Integer getGstPercentage() {
		return gstPercentage;
	}

	public void setGstPercentage(Integer gstPercentage) {
		this.gstPercentage = gstPercentage;
	}

	
	
}
