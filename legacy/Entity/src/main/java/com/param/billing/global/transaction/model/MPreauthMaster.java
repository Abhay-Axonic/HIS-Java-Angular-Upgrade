package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_preauth_master", schema = "billing")
public class MPreauthMaster {
	@Id
	@Column(name = "preauth_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer preauthId;

	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "payee_id")
	private Integer payeeId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "patient_sponsor_id")
	private Integer patientSponsorId;
	
	@Column(name = "authorized_amount")
	private Double authorizedAmount;
	
	@Column(name = "balance_amount")
	private Double balanceAmount;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPreauthId() {
		return preauthId;
	}

	public void setPreauthId(Integer preauthId) {
		this.preauthId = preauthId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getPatientSponsorId() {
		return patientSponsorId;
	}

	public void setPatientSponsorId(Integer patientSponsorId) {
		this.patientSponsorId = patientSponsorId;
	}

	public Double getAuthorizedAmount() {
		return authorizedAmount;
	}

	public void setAuthorizedAmount(Double authorizedAmount) {
		this.authorizedAmount = authorizedAmount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
