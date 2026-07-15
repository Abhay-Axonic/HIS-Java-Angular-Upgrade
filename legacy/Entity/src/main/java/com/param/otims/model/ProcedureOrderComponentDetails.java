package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_procedure_order_component_details", schema = "otims")
@SequenceGenerator(name = "t_procedure_order_component_details_seq", sequenceName = "otims.t_procedure_order_component_details_seq",
    allocationSize = 1)
public class ProcedureOrderComponentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_procedure_order_component_details_seq")
	@Column(name="procedure_order_component_details_id")
	private Integer procedureOrderComponentDetailsId;
	
	@Column(name="order_detail_id")
	private Integer orderDetailId;
	
	@Column(name="procedure_id")
	private Integer procedureId;
	
	@Column(name="service_id") 
	private Integer serviceId;
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name="discount")
	private Double discount;
	
	@Column(name="net_amount")
	private Double netAmount;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="dependent_id")
	private Integer dependentId;
	
	@Column(name="surgeon_per")
	private Double surgeonPer;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;

	public Integer getProcedureOrderComponentDetailsId() {
		return procedureOrderComponentDetailsId;
	}

	public void setProcedureOrderComponentDetailsId(
			Integer procedureOrderComponentDetailsId) {
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

	public Double getSurgeonPer() {
		return surgeonPer;
	}

	public void setSurgeonPer(Double surgeonPer) {
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
}
