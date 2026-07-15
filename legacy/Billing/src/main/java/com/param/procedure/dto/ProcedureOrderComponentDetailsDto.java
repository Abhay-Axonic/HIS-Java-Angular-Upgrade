package com.param.procedure.dto;

public class ProcedureOrderComponentDetailsDto {
	private Integer procedureOrderComponentDetailsId;
	private Integer orderDetailId;
	private Integer procedureId;
	private Integer serviceId;
	private Double amount;
	private Double discount;
	private Double netAmount;
	private Integer doctorId;
	private Integer specialityId;
	private Integer dependentId;
	private Integer surgeonPer;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	public Integer getProcedureOrderComponentDetailsId() {
		return procedureOrderComponentDetailsId;
	}
	public void setProcedureOrderComponentDetailsId(Integer procedureOrderComponentDetailsId) {
		this.procedureOrderComponentDetailsId = procedureOrderComponentDetailsId;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Integer getProcedureId() {
		return procedureId;
	}
	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getDependentId() {
		return dependentId;
	}
	public void setDependentId(Integer dependentId) {
		this.dependentId = dependentId;
	}
	public Integer getSurgeonPer() {
		return surgeonPer;
	}
	public void setSurgeonPer(Integer surgeonPer) {
		this.surgeonPer = surgeonPer;
	}
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
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
