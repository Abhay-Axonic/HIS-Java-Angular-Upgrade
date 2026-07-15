package com.param.global.doctor.dto;

import java.util.List;

public class OrderSetReqDto {

	private List<Integer> ordersetMasterId;
	private Integer serviceId;
	private int orgId;
	private int unitId;
	private int ordersetTypeId;
	private int diagnosisId;
	private int specialityId;
	private int doctorId;
	
	public List<Integer> getOrdersetMasterId() {
		return ordersetMasterId;
	}
	public void setOrdersetMasterId(List<Integer> ordersetMasterId) {
		this.ordersetMasterId = ordersetMasterId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getOrdersetTypeId() {
		return ordersetTypeId;
	}
	public void setOrdersetTypeId(int ordersetTypeId) {
		this.ordersetTypeId = ordersetTypeId;
	}
	public int getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	public int getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(int diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	
}
