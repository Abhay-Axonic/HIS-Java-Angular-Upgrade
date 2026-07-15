package com.param.opd.coversheet.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@Entity
@Table(name="t_encounter_ehc_consultation_mppr", schema="opd")
@SequenceGenerator(name = "seq_encounter_ehc_consultation_mppr", sequenceName = "opd.seq_encounter_ehc_consultation_mppr", allocationSize = 1)
public class EncounterEhcConsultationMppr {
	
	@Id
	@Column(name="consultation_mppr_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_encounter_ehc_consultation_mppr")
	private int consultationMpprId;
	
	@Column(name="consultation_master_id")
	private Integer consultationMasterId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="order_details_id")
	private Integer orderDtailsId;
	
	@Column(name="consultation_speciality_id")
	private Integer consultationSpecialityId;
	
	@Column(name="package_id")
	private Integer packageId;
	
	@Column(name="doctor_id")
	private Integer doctorId;

	@Column(name="appointment_status_id")
	private Integer appointmentStatusId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="slot_id")
	private Integer slotId;
	
	@Column(name="child_encounter_id")
	private Integer childEncounterId;


	public int getConsultationMpprId() {
		return consultationMpprId;
	}

	public void setConsultationMpprId(int consultationMpprId) {
		this.consultationMpprId = consultationMpprId;
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

	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
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

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	public Integer getOrderDtailsId() {
		return orderDtailsId;
	}

	public void setOrderDtailsId(Integer orderDtailsId) {
		this.orderDtailsId = orderDtailsId;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getConsultationMasterId() {
		return consultationMasterId;
	}

	public void setConsultationMasterId(Integer consultationMasterId) {
		this.consultationMasterId = consultationMasterId;
	}

	public Integer getChildEncounterId() {
		return childEncounterId;
	}

	public void setChildEncounterId(Integer childEncounterId) {
		this.childEncounterId = childEncounterId;
	}
	

}
