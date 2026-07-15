package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@Entity
@Table(name="t_intra_transfusion_monitoring_mapper", schema="blood_bank")
@SequenceGenerator(name="t_intra_transfusion_monitoring_mapper_seq", sequenceName="blood_bank.t_intra_transfusion_monitoring_mapper_seq", allocationSize= 1)
public class IntraTransfusionMonitoring {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_intra_transfusion_monitoring_mapper_seq")
	@Column(name = "intra_transfusion_monitoring_mapper_id")
	private Integer intraTransfusionMonitoringMapperId;
	
	
	@Column(name = "transfusion_detail_id")
	private Integer transfusionDetailId;

	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "start_time")
	private String incubationStartTime;
	
	@Column(name = "donor_pulse")
	private Double pulse;
	
	@Column(name = "donor_temperature")
	private Double temperature;
	
	@Column(name = "donor_bp_systolic")
	private Double bpSystolic;
	
	@Column(name = "donor_bp_diastolic")
	private Double bpDiastolic;
	
	@Column(name = "donor_respiration_rate")
	private Integer respirationRate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name ="updated_by")
	private Integer updatedBy;
	
	@Column(name ="updated_date")
	private Date updatedDate;
	
	@Column(name = "status")
	private Character status;

	public Integer getIntraTransfusionMonitoringMapperId() {
		return intraTransfusionMonitoringMapperId;
	}

	public void setIntraTransfusionMonitoringMapperId(Integer intraTransfusionMonitoringMapperId) {
		this.intraTransfusionMonitoringMapperId = intraTransfusionMonitoringMapperId;
	}

	public Integer getTransfusionDetailId() {
		return transfusionDetailId;
	}

	public void setTransfusionDetailId(Integer transfusionDetailId) {
		this.transfusionDetailId = transfusionDetailId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getStartTime() {
		return incubationStartTime;
	}

	public void setStartTime(String incubationStartTime) {
		this.incubationStartTime = incubationStartTime;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Double getPulse() {
		return pulse;
	}

	public void setPulse(Double pulse) {
		this.pulse = pulse;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getBpSystolic() {
		return bpSystolic;
	}

	public void setBpSystolic(Double bpSystolic) {
		this.bpSystolic = bpSystolic;
	}

	public Double getBpDiastolic() {
		return bpDiastolic;
	}

	public void setBpDiastolic(Double bpDiastolic) {
		this.bpDiastolic = bpDiastolic;
	}

	public Integer getRespirationRate() {
		return respirationRate;
	}

	public void setRespirationRate(Integer respirationRate) {
		this.respirationRate = respirationRate;
	}

	public String getIncubationStartTime() {
		return incubationStartTime;
	}

	public void setIncubationStartTime(String string) {
		this.incubationStartTime = string;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	    
}
