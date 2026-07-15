package com.param.billing.unit.dto;

import java.util.List;

public class ChargeConversionDto {
	private String orderNo;
	private String orderDate;
	private String serviceCode;
	private String serviceName;
	private Double serviceAmount;
	private Integer serviceId;
	private Integer orderQyt;
	private Double totalAmount;
	private Double concession;
	private Double discountAmount;
	private Double gstAmt;
	private Double netAmount;
	private Integer contractId;
	private Integer payeeId;
	private Integer encounterId;
	private Integer  orderDetailsId;
	private List<Integer> serviceListIds;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Double getServiceAmount() {
		return serviceAmount;
	}
	public void setServiceAmount(Double serviceAmount) {
		this.serviceAmount = serviceAmount;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getOrderQyt() {
		return orderQyt;
	}
	public void setOrderQyt(Integer orderQyt) {
		this.orderQyt = orderQyt;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getConcession() {
		return concession;
	}
	public void setConcession(Double concession) {
		this.concession = concession;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Double getGstAmt() {
		return gstAmt;
	}
	public void setGstAmt(Double gstAmt) {
		this.gstAmt = gstAmt;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public List<Integer> getServiceListIds() {
		return serviceListIds;
	}
	public void setServiceListIds(List<Integer> serviceListIds) {
		this.serviceListIds = serviceListIds;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

}
