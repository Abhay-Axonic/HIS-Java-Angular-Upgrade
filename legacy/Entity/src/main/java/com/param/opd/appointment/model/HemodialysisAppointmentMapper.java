package com.param.opd.appointment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_hemodialysis_appointment_mapper", schema="opd")
@SequenceGenerator(name = "hemodialysis_appointment_mapper_seq", sequenceName = "opd.hemodialysis_appointment_mapper_seq", allocationSize = 1)
public class HemodialysisAppointmentMapper {
	@Id
	@Column(name="dialysis_app_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hemodialysis_appointment_mapper_seq")
	private Integer dialysisAppMapperId;
	
	@Column(name = "appointment_id")
	private Integer appointmentId;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "chair_id")
	private Integer chairId;
	
	@Column(name = "machine_id")
	private Integer machineId;
	
	@Column(name = "technician_id")
	private Integer technicianId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "workbench_acceptance")
	private Character workbenchAcceptance;
	
	@Column(name = "technician_acceptance")
	private Character technicianAcceptance;
	
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
	
	@Column(name = "dialysis_status_id")
	private Integer dialysisStatusId;
	
	
	public Integer getDialysisAppMapperId() {
		return dialysisAppMapperId;
	}

	public void setDialysisAppMapperId(Integer dialysisAppMapperId) {
		this.dialysisAppMapperId = dialysisAppMapperId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getChairId() {
		return chairId;
	}

	public void setChairId(Integer chairId) {
		this.chairId = chairId;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Character getWorkbenchAcceptance() {
		return workbenchAcceptance;
	}

	public void setWorkbenchAcceptance(Character workbenchAcceptance) {
		this.workbenchAcceptance = workbenchAcceptance;
	}

	public Character getTechnicianAcceptance() {
		return technicianAcceptance;
	}

	public void setTechnicianAcceptance(Character technicianAcceptance) {
		this.technicianAcceptance = technicianAcceptance;
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

	public Integer getDialysisStatusId() {
		return dialysisStatusId;
	}

	public void setDialysisStatusId(Integer dialysisStatusId) {
		this.dialysisStatusId = dialysisStatusId;
	}
	
}
