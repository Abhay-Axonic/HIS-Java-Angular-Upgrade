package com.param.lis.global.dto;

import java.util.List;

public class TestCostingMasterDto {
	
private Integer testCostingMasterId;
	
	private Integer specialityId;
	
	private Integer subSpecialityId;
	
	private Integer serviceId;
	
	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;	

	private Integer unitId;
	
	private Integer organizationId;	
	
	private Double cost;
	
	private Integer costHeadId;
	
	private Double sumCost;
	
	private String specialityName;
	
	private String subSpecialityMasterName;
	
	private String serviceLongDescripton;
	
	private Integer testCostingMapperId;
	
	private Integer testCostHeadId;
	
	
	private List<TestCostingMapperDto> costHeadList;

	public Integer getTestCostingMasterId() {
		return testCostingMasterId;
	}

	public void setTestCostingMasterId(Integer testCostingMasterId) {
		this.testCostingMasterId = testCostingMasterId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public List<TestCostingMapperDto> getCostHeadList() {
		return costHeadList;
	}

	public void setCostHeadList(List<TestCostingMapperDto> costHeadList) {
		this.costHeadList = costHeadList;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getCostHeadId() {
		return costHeadId;
	}

	public void setCostHeadId(Integer costHeadId) {
		this.costHeadId = costHeadId;
	}

	public Double getSumCost() {
		return sumCost;
	}

	public void setSumCost(Double sumCost) {
		this.sumCost = sumCost;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityMasterName() {
		return subSpecialityMasterName;
	}

	public void setSubSpecialityMasterName(String subSpecialityMasterName) {
		this.subSpecialityMasterName = subSpecialityMasterName;
	}

	public String getServiceLongDescripton() {
		return serviceLongDescripton;
	}

	public void setServiceLongDescripton(String serviceLongDescripton) {
		this.serviceLongDescripton = serviceLongDescripton;
	}

	public Integer getTestCostingMapperId() {
		return testCostingMapperId;
	}

	public void setTestCostingMapperId(Integer testCostingMapperId) {
		this.testCostingMapperId = testCostingMapperId;
	}

	public Integer getTestCostHeadId() {
		return testCostHeadId;
	}

	public void setTestCostHeadId(Integer testCostHeadId) {
		this.testCostHeadId = testCostHeadId;
	}
	
	
}
