package com.param.global.billing.dto;

import java.util.List;

public class CouponTemplateMasterDto {

	private Integer couponTemplateMasterId;
	
	private String couponTemplateName;
	
	private String couponTemplateDescription;
	
	private Integer expireDays;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Integer createdBy;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer serviceId;
	
	private String serviceName;
	
	private Integer screenId;
	
	private Integer patientId;
	
	private Integer orderDetailsId;
	
	private List<CouponTemplateDetailsDto> couponTemplateDetailsDtoList;

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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
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

	public List<CouponTemplateDetailsDto> getCouponTemplateDetailsDtoList() {
		return couponTemplateDetailsDtoList;
	}

	public void setCouponTemplateDetailsDtoList(List<CouponTemplateDetailsDto> couponTemplateDetailsDtoList) {
		this.couponTemplateDetailsDtoList = couponTemplateDetailsDtoList;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	
	
}
