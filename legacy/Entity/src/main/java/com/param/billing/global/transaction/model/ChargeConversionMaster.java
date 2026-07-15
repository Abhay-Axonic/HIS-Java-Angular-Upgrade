package com.param.billing.global.transaction.model;



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
@Table(name="t_charge_conversion_master", schema="billing")
@SequenceGenerator(name = "t_charge_conversion_master_seq", sequenceName = "billing.t_charge_conversion_master_seq", allocationSize = 1)
public class ChargeConversionMaster {
	@Id
	@Column(name="conversion_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_charge_conversion_master_seq")
	private Integer conversionMasterId;
	
	@Column(name = "patinet_id")
	private Integer patinetId;
	
	@Column(name = "visity_type_id")
	private Integer visitTypeId;

	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "from_payee_id")
	private Integer fromPayeeId;
	
	@Column(name = "from_contract_id")
	private Integer fromContractId;
	
	@Column(name = "to_payee_id")
	private Integer toPayeeId;
	
	@Column(name = "to_contract_id")
	private Integer toContractId;
	
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	
	
	public Integer getConversionMasterId() {
		return conversionMasterId;
	}

	public void setConversionMasterId(Integer conversionMasterId) {
		this.conversionMasterId = conversionMasterId;
	}

	public Integer getPatinetId() {
		return patinetId;
	}

	public void setPatinetId(Integer patinetId) {
		this.patinetId = patinetId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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

	public Integer getFromPayeeId() {
		return fromPayeeId;
	}

	public void setFromPayeeId(Integer fromPayeeId) {
		this.fromPayeeId = fromPayeeId;
	}

	public Integer getFromContractId() {
		return fromContractId;
	}

	public void setFromContractId(Integer fromContractId) {
		this.fromContractId = fromContractId;
	}

	public Integer getToPayeeId() {
		return toPayeeId;
	}

	public void setToPayeeId(Integer toPayeeId) {
		this.toPayeeId = toPayeeId;
	}

	public Integer getToContractId() {
		return toContractId;
	}

	public void setToContractId(Integer toContractId) {
		this.toContractId = toContractId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
	
}
