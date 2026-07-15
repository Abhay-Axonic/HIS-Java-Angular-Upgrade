package com.param.billing.global;

import java.util.List;

public class ConvertServicesFromOptoIpDto 
{
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer visitTypeId;
	
	private Integer userId;
	
    private Integer patientId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private List<Integer> orderDetailsIds;

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public List<Integer> getOrderDetailsIds() {
		return orderDetailsIds;
	}

	public void setOrderDetailsIds(List<Integer> orderDetailsIds) {
		this.orderDetailsIds = orderDetailsIds;
	}

}
