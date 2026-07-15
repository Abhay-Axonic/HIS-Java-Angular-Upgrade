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
@Table(name = "m_coupon_template_master", schema = "billing")
@SequenceGenerator(name = "coupon_template_master_seq", sequenceName = "billing.coupon_template_master_seq", allocationSize = 1)
public class CouponTemplateMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_template_master_seq")
	@Column(name = "coupon_template_master_id")
	private Integer couponTemplateMasterId;
	
	@Column(name="coupon_template_name")
	private String couponTemplateName;
	
	@Column(name="coupon_template_description")
	private String couponTemplateDescription;
	
	@Column(name="expire_days")
	private Integer expireDays;
	
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
	
	@Column(name="service_id")
	private Integer serviceId;

	public Integer getCouponTemplateMasterId() {
		return couponTemplateMasterId;
	}

	public void setCouponTemplateMasterId(Integer couponTemplateMasterId) {
		this.couponTemplateMasterId = couponTemplateMasterId;
	}

	public String getCouponTemplateName() {
		return couponTemplateName;
	}

	public void setCouponTemplateName(String couponTemplateName) {
		this.couponTemplateName = couponTemplateName;
	}

	public String getCouponTemplateDescription() {
		return couponTemplateDescription;
	}

	public void setCouponTemplateDescription(String couponTemplateDescription) {
		this.couponTemplateDescription = couponTemplateDescription;
	}

	public Integer getExpireDays() {
		return expireDays;
	}

	public void setExpireDays(Integer expireDays) {
		this.expireDays = expireDays;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	
	
	
}
