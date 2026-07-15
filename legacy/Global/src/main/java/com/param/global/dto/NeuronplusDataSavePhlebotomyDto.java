package com.param.global.dto;

public class NeuronplusDataSavePhlebotomyDto {

	private int id;
	
	private Integer organizationId;
	
	private Integer orgUnitId;

	private Integer orderId;
	
	private String neuronPlusUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getNeuronPlusUrl() {
		return neuronPlusUrl;
	}

	public void setNeuronPlusUrl(String neuronPlusUrl) {
		this.neuronPlusUrl = neuronPlusUrl;
	}



}
