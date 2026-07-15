package com.param.opd.appointment.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@Entity
@Table(name="t_cyclic_appointment_mapper", schema="opd")
@SequenceGenerator(name = "cyclic_appointment_mapper_seq", sequenceName = "opd.t_cyclic_appointment_mapper_seq", allocationSize = 1)

public class CyclicAppointmentMapper {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cyclic_appointment_mapper_seq")
	private int id;
	
	@Column(name = "appointment_id")
	private Integer appointmentId;
	
	@Column(name = "bill_id")
	private Integer billId;
	
	@Column(name="appointment_time")
	@Convert(converter = InstantConverter.class)
	private Instant appointmentTime;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "order_detail_id")
	private Integer orderDetailId;
	
	@Column(name = "available_quantity")
	private Integer availabelQuantity;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "order_id")
	private Integer orderId;
	
	//@Column(name = "package_order_id")
	//private Integer pckOrderId;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Instant getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(Instant appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getAvailabelQuantity() {
		return availabelQuantity;
	}

	public void setAvailabelQuantity(Integer availabelQuantity) {
		this.availabelQuantity = availabelQuantity;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}


	
	
}
