package com.param.billing.global.transaction.dto;

import java.util.List;

public class CancelOrderedServicesDto {
	
	private Integer orderDetailsId;
	
	private Integer ordCancelReasonId;
	
	private Integer servicesCancelReasonId;
	
	private String ordCancelDatetime;
	
	private String ordCancelRemark;
	
	private Integer updatedBy;
	
	private Character status;
	
	private Integer periodicityServiceOrderId;
	
	private List<CancelOrderedServicesDto> orderedServicesDtosList;
	
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getPeriodicityServiceOrderId() {
		return periodicityServiceOrderId;
	}

	public void setPeriodicityServiceOrderId(Integer periodicityServiceOrderId) {
		this.periodicityServiceOrderId = periodicityServiceOrderId;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getOrdCancelReasonId() {
		return ordCancelReasonId;
	}

	public void setOrdCancelReasonId(Integer ordCancelReasonId) {
		this.ordCancelReasonId = ordCancelReasonId;
	}

	public Integer getServicesCancelReasonId() {
		return servicesCancelReasonId;
	}

	public void setServicesCancelReasonId(Integer servicesCancelReasonId) {
		this.servicesCancelReasonId = servicesCancelReasonId;
	}


	public String getOrdCancelRemark() {
		return ordCancelRemark;
	}

	public void setOrdCancelRemark(String ordCancelRemark) {
		this.ordCancelRemark = ordCancelRemark;
	}

	public List<CancelOrderedServicesDto> getOrderedServicesDtosList() {
		return orderedServicesDtosList;
	}

	public void setOrderedServicesDtosList(List<CancelOrderedServicesDto> orderedServicesDtosList) {
		this.orderedServicesDtosList = orderedServicesDtosList;
	}

	public String getOrdCancelDatetime() {
		return ordCancelDatetime;
	}

	public void setOrdCancelDatetime(String ordCancelDatetime) {
		this.ordCancelDatetime = ordCancelDatetime;
	}
	
}
