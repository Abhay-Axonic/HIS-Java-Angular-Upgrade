package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon_template_consume_details", schema = "billing")
@SequenceGenerator(name = "coupon_template_consume_details_seq", sequenceName = "billing.coupon_template_consume_details_seq", allocationSize = 1)
public class CouponTemplateConsumeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_template_consume_details_seq")
	@Column(name = "coupon_template_consume_details_id")
	private Integer couponTemplateConsumeDetailsId;
	
	@Column(name = "patient_coupon_template_mapper_id")
	private Integer patientCouponTemplateMapperId;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "bill_id")
	private Integer billId; 
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="consume_amount")
	private Double consumeAmount;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="status")
	private char status;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	public Integer getCouponTemplateConsumeDetailsId() {
		return couponTemplateConsumeDetailsId;
	}

	public void setCouponTemplateConsumeDetailsId(Integer couponTemplateConsumeDetailsId) {
		this.couponTemplateConsumeDetailsId = couponTemplateConsumeDetailsId;
	}

	public Integer getPatientCouponTemplateMapperId() {
		return patientCouponTemplateMapperId;
	}

	public void setPatientCouponTemplateMapperId(Integer patientCouponTemplateMapperId) {
		this.patientCouponTemplateMapperId = patientCouponTemplateMapperId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(Double consumeAmount) {
		this.consumeAmount = consumeAmount;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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
	
	
	
}
