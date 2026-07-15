package com.param.global.otims.dto;

public class SurgeryResourceConfigByCritertiaReqDto {
	
	public SurgeryResourceConfigByCritertiaReqDto(Integer specialityId, Integer surgeryGradeId, Integer doctorGradeId,Integer patientCategoryId, Integer paymentEntitlementId, Integer bedCategoryId, Integer orgId, Integer unitId) {
		this.specialityId = specialityId;
		this.surgeryGradeId = surgeryGradeId;
		this.doctorGradeId = doctorGradeId;
		this.patientCategoryId = patientCategoryId;
		this.paymentEntitlementId = paymentEntitlementId;
		this.bedCategoryId = bedCategoryId;
		this.orgId = orgId;
		this.unitId = unitId;
	}
	
	public SurgeryResourceConfigByCritertiaReqDto(){
		
	}
	
	private Integer specialityId;
	private Integer surgeryGradeId;
	private Integer doctorGradeId;
	private Integer patientCategoryId;
	private Integer paymentEntitlementId;
	private Integer bedCategoryId;
	private Integer orgId;
	private Integer unitId;
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}
	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}
	public Integer getDoctorGradeId() {
		return doctorGradeId;
	}
	public void setDoctorGradeId(Integer doctorGradeId) {
		this.doctorGradeId = doctorGradeId;
	}
	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}
	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getBedCategoryId() {
		return bedCategoryId;
	}
	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}
}