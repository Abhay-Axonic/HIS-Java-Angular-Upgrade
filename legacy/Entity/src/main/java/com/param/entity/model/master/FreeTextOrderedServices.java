package com.param.entity.model.master;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_free_text_ordered_services", schema = "public")
@SequenceGenerator(name = "t_free_text_ordered_services_seq", sequenceName = "public.t_free_text_ordered_services_seq", allocationSize = 1)
public class FreeTextOrderedServices {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_free_text_ordered_services_seq")
	private Integer freeTextOrderedServicesId;

	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "is_consumed")
	private boolean isConsumed;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "service_text")
	private String serviceText;
	
	@Column(name = "cov_template_id")
	private Integer covTemplateId;

	public Integer getFreeTextOrderedServicesId() {
		return freeTextOrderedServicesId;
	}

	public void setFreeTextOrderedServicesId(Integer freeTextOrderedServicesId) {
		this.freeTextOrderedServicesId = freeTextOrderedServicesId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public boolean getIsConsumed() {
		return isConsumed;
	}

	public void setIsConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getServiceText() {
		return serviceText;
	}

	public void setServiceText(String serviceText) {
		this.serviceText = serviceText;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}


}
