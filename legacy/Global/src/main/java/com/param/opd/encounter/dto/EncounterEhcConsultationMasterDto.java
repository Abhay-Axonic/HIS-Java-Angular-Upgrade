package com.param.opd.encounter.dto;

public class EncounterEhcConsultationMasterDto {

	private Integer consultationMasterId;

	private Integer encounterId;

	private Integer serviceId;

	private Integer orderDtailsId;

	private Integer packageId;

	private Integer unitId;

	private Integer organizationId;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private Character status;

	public Integer getConsultationMasterId() {
		return consultationMasterId;
	}

	public void setConsultationMasterId(Integer consultationMasterId) {
		this.consultationMasterId = consultationMasterId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrderDtailsId() {
		return orderDtailsId;
	}

	public void setOrderDtailsId(Integer orderDtailsId) {
		this.orderDtailsId = orderDtailsId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

}
