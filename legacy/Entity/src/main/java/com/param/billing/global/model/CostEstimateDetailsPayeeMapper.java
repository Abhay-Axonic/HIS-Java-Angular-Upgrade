package com.param.billing.global.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="t_cost_estimate_details_payee_mapper",schema = "billing")
public class CostEstimateDetailsPayeeMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",unique = true, nullable = false)
	private Integer id;
	
	@Column(name="cost_estimate_details_id")
	private Integer costEstimationDetailsId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="contract_id")
	private Integer contractId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="payee_amount")
	private BigDecimal payeeAmount;
	
	@Column(name="status")
	private Character status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCostEstimationDetailsId() {
		return costEstimationDetailsId;
	}

	public void setCostEstimationDetailsId(Integer costEstimationDetailsId) {
		this.costEstimationDetailsId = costEstimationDetailsId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}
