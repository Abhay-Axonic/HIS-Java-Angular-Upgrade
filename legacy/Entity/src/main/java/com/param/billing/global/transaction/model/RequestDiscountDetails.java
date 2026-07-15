package com.param.billing.global.transaction.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;

@Entity
@Table(name="t_request_discount_details",schema="billing")
@SequenceGenerator(name = "request_discount_details_seq", sequenceName = "billing.request_discount_details_seq", allocationSize = 1)
public class RequestDiscountDetails {

	@Id
	@Column(name="req_disc_details_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="request_discount_details_seq")
	private Integer reqDiscDetailsId;
	
	@Column(name="req_disc_id")
	private Integer reqDiscId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="order_id")
	private Integer orderId;
	
	@Column(name="order_details_id")
	private Integer orderDetailsId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="net_amount")
	private BigDecimal netAmount;
	
	@Column(name="req_dis_amt")
	private BigDecimal reqDisAmt;
	
	@Column(name="approved_disc_amt")
	private BigDecimal approvedDiscAmt;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "req_disc_id", insertable = false, nullable = false, updatable = false)
	private RequestDiscount requestDiscount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;

	public Integer getReqDiscDetailsId() {
		return reqDiscDetailsId;
	}

	public void setReqDiscDetailsId(Integer reqDiscDetailsId) {
		this.reqDiscDetailsId = reqDiscDetailsId;
	}

	public Integer getReqDiscId() {
		return reqDiscId;
	}

	public void setReqDiscId(Integer reqDiscId) {
		this.reqDiscId = reqDiscId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public BigDecimal getApprovedDiscAmt() {
		return approvedDiscAmt;
	}

	public void setApprovedDiscAmt(BigDecimal approvedDiscAmt) {
		this.approvedDiscAmt = approvedDiscAmt;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}