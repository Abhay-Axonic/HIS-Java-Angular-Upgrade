package com.param.global.billing.dto;

public class CouponTemplateDetailsDto {

	private Integer couponTemplateDetailsId;
	
	private Integer couponTemplateMasterId; 
	
	private Integer specialityId;
	
	private Integer subSpecialityId;
	
	private Integer serviceId;
	
	private Integer percentage;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Integer createdBy;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String specialityName;
	
	private String subSpecialityName;
	
	private String serviceName;
	
	private Double amount;

	public Integer getCouponTemplateDetailsId() {
		return couponTemplateDetailsId;
	}

	public void setCouponTemplateDetailsId(Integer couponTemplateDetailsId) {
		this.couponTemplateDetailsId = couponTemplateDetailsId;
	}

	public Integer getCouponTemplateMasterId() {
		return couponTemplateMasterId;
	}

	public void setCouponTemplateMasterId(Integer couponTemplateMasterId) {
		this.couponTemplateMasterId = couponTemplateMasterId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
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

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
