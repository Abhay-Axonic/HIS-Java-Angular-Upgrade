package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_pos_machine_response_mapper", schema = "billing")
@SequenceGenerator(name = "t_pos_machine_response_mapper_seq", sequenceName = "billing.t_pos_machine_response_mapper_seq", allocationSize = 1)
public class POSMachineResponseMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pos_machine_response_mapper_seq")
	@Column(name = "pos_machine_response_id")
	private int posMachineResponseId;
	
	@Column(name = "requested_unique_id")
	private String requestedUniqueId;
	
	@Column(name = "device_id")
	private String deviceId;
	
	@Column(name = "generated_reponse")
	private String generatedReponse;

	@Column(name = "requested_json")
	private String requestedJson;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "is_consumed")
	private boolean isConsumed;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "transaction_amount")
	private double transactionAmount;
	
	public int getPosMachineResponseId() {
		return posMachineResponseId;
	}

	public void setPosMachineResponseId(int posMachineResponseId) {
		this.posMachineResponseId = posMachineResponseId;
	}

	public String getGeneratedReponse() {
		return generatedReponse;
	}

	public void setGeneratedReponse(String generatedReponse) {
		this.generatedReponse = generatedReponse;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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

	public String getRequestedJson() {
		return requestedJson;
	}

	public void setRequestedJson(String requestedJson) {
		this.requestedJson = requestedJson;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getRequestedUniqueId() {
		return requestedUniqueId;
	}

	public void setRequestedUniqueId(String requestedUniqueId) {
		this.requestedUniqueId = requestedUniqueId;
	}

	public boolean getIsConsumed() {
		return isConsumed;
	}

	public void setIsConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	
}
