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
@Table(name="t_encounter_ehc_consultation_master", schema="opd")
@SequenceGenerator(name = "seq_encounter_ehc_consultation_master", sequenceName = "opd.seq_encounter_ehc_consultation_master", allocationSize = 1)
public class EncounterEhcConsultationMaster {
	
	@Id
	@Column(name="consultation_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_encounter_ehc_consultation_master")
	private int consultationMasterId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="order_details_id")
	private Integer orderDtailsId;
	
	@Column(name="package_id")
	private Integer packageId;
	
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

	public int getConsultationMasterId() {
		return consultationMasterId;
	}

	public void setConsultationMasterId(int consultationMasterId) {
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
	
	

}
