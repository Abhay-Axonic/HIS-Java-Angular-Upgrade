package com.param.billing.global.transaction.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_intrim_bill_details_payee_mapper",schema = "billing")
public class IntrimBillPayeeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",unique = true, nullable = false)
	private Integer id;
	
	@Column(name="intrim_bill_details_id")
	private Integer intrimBillDetailsId;
	
	@Column(name="order_details_id")
	private Integer orderDetailsId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="contract_id")
	private Integer contractId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="payee_amount")
	private BigDecimal payeeAmount;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="t_sale_details_id")
	private Integer tSaleDetailsId;
	
	@Column(name="item_id")
	private Integer itemId;

	public Integer getIntrimBillDetailsId() {
		return intrimBillDetailsId;
	}

	public void setIntrimBillDetailsId(Integer intrimBillDetailsId) {
		this.intrimBillDetailsId = intrimBillDetailsId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public BigDecimal getPayeeAmount() {
		return payeeAmount;
	}

	public void setPayeeAmount(BigDecimal payeeAmount) {
		this.payeeAmount = payeeAmount;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer gettSaleDetailsId() {
		return tSaleDetailsId;
	}

	public void settSaleDetailsId(Integer tSaleDetailsId) {
		this.tSaleDetailsId = tSaleDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
}
