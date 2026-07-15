package com.param.bloodBank.model;

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

import com.param.entity.model.patient.PatientRegistration;

@Entity
@Table(name = "m_transfusion_details", schema = "blood_bank")
@SequenceGenerator(name = "transfusion_details_seq", sequenceName = "blood_bank.m_transfusion_details_seq", allocationSize = 1)
public class TransfusionDetailMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfusion_details_seq")
	@Column(name = "transfusion_detail_id")
	private Integer transfusionDetailId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "v_site_id")
	private Integer vSiteId;
	
	@Column(name = "iv_access_id")
	private Integer ivAccessId;
	
	@Column(name = "donor_pulse")
	private Float donorPulse;
	
	@Column(name = "donor_temperature")
	private Float donorTemperature;
	
	@Column(name = "donor_bp_systolic")
	private Float donorBPSystolic;
	
	@Column(name = "donor_bp_diastolic")
	private Float donorBpDiastolic;
	
	@Column(name = "donor_respiration_rate")
	private Integer donarRespirationRate;
	
	@Column(name = "start_time")
	private String startTime;   
	
	@Column(name = "transfusion_id")
	private Integer transfusionId; 
	
	@Column(name = "termination_id")
	private Integer terminationId;
	
	@Column(name = "save_as_draft_flag")
	private Character saveAsDraftFlag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_blood_bag_id", insertable = false, updatable = false)
	private IssueBloodBagMapper issueBloodBagMapper;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
	private BloodGroupMaster bloodGroupMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id", insertable = false, updatable = false)
	private BloodComponentMaster bloodComponentMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id",insertable = false, updatable = false)
	private BloodBagIssueAcceptance bloodBagIssueAcceptance;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_blood_bag_details_id" ,insertable = false, updatable = false)
	private IssueBloodBagDetailMapper issueBloodBagDetailMapper;
	
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

	public Integer getvSiteId() {
		return vSiteId;
	}

	public void setvSiteId(Integer vSiteId) {
		this.vSiteId = vSiteId;
	}

	public Integer getIvAccessId() {
		return ivAccessId;
	}

	public void setIvAccessId(Integer ivAccessId) {
		this.ivAccessId = ivAccessId;
	}

	public Float getDonorPulse() {
		return donorPulse;
	}

	public void setDonorPulse(Float donorPulse) {
		this.donorPulse = donorPulse;
	}

	public Float getDonorTemperature() {
		return donorTemperature;
	}

	public void setDonorTemperature(Float donorTemperature) {
		this.donorTemperature = donorTemperature;
	}

	public Float getDonorBPSystolic() {
		return donorBPSystolic;
	}

	public void setDonorBPSystolic(Float donorBPSystolic) {
		this.donorBPSystolic = donorBPSystolic;
	}

	public Float getDonorBpDiastolic() {
		return donorBpDiastolic;
	}

	public void setDonorBpDiastolic(Float donorBpDiastolic) {
		this.donorBpDiastolic = donorBpDiastolic;
	}

	public Integer getDonarRespirationRate() {
		return donarRespirationRate;
	}

	public void setDonarRespirationRate(Integer donarRespirationRate) {
		this.donarRespirationRate = donarRespirationRate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public IssueBloodBagMapper getIssueBloodBagMapper() {
		return issueBloodBagMapper;
	}

	public void setIssueBloodBagMapper(IssueBloodBagMapper issueBloodBagMapper) {
		this.issueBloodBagMapper = issueBloodBagMapper;
	}

	public BloodGroupMaster getBloodGroupMaster() {
		return bloodGroupMaster;
	}

	public void setBloodGroupMaster(BloodGroupMaster bloodGroupMaster) {
		this.bloodGroupMaster = bloodGroupMaster;
	}

	public BloodComponentMaster getBloodComponentMaster() {
		return bloodComponentMaster;
	}

	public void setBloodComponentMaster(BloodComponentMaster bloodComponentMaster) {
		this.bloodComponentMaster = bloodComponentMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public BloodBagIssueAcceptance getBloodBagIssueAcceptance() {
		return bloodBagIssueAcceptance;
	}

	public void setBloodBagIssueAcceptance(BloodBagIssueAcceptance bloodBagIssueAcceptance) {
		this.bloodBagIssueAcceptance = bloodBagIssueAcceptance;
	}

	public Integer getTransfusionId() {
		return transfusionId;
	}

	public void setTransfusionId(Integer transfusionId) {
		this.transfusionId = transfusionId;
	}

	public Integer getTerminationId() {
		return terminationId;
	}

	public void setTerminationId(Integer terminationId) {
		this.terminationId = terminationId;
	}

	public Character getSaveAsDraftFlag() {
		return saveAsDraftFlag;
	}

	public void setSaveAsDraftFlag(Character saveAsDraftFlag) {
		this.saveAsDraftFlag = saveAsDraftFlag;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	
}
