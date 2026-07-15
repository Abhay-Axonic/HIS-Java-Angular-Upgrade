package com.param.global.dto;

public class ServiceOrdDto {

	private Integer serviceId;
	
	private Integer orderSetId;
	
	private Character isInvestigationOrder;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrderSetId() {
		return orderSetId;
	}

	public void setOrderSetId(Integer orderSetId) {
		this.orderSetId = orderSetId;
	}

	public Character getIsInvestigationOrder() {
		return isInvestigationOrder;
	}

	public void setIsInvestigationOrder(Character isInvestigationOrder) {
		this.isInvestigationOrder = isInvestigationOrder;
	}
	
}
