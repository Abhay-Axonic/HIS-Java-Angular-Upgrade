package com.param.entity.model.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_hiu_fhir_details", schema = "abdm")
@SequenceGenerator(name = "m_hiu_fhir_details_seq", sequenceName = "abdm.m_hiu_fhir_details_seq", allocationSize = 1)
public class HiuFhirDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_hiu_fhir_details_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "encounter_number")
	private String encounterNumber;
	
	@Column(name = "carecontex_reference")
	private String carecontexReference;

	@Column(name = "encrypted_data")
	private String encryptedData;
	
	@Column(name = "decrypted_data")
	private String decryptedData;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "transaction_id")
	private String transactionId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public String getCarecontexReference() {
		return carecontexReference;
	}

	public void setCarecontexReference(String carecontexReference) {
		this.carecontexReference = carecontexReference;
	}

	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getDecryptedData() {
		return decryptedData;
	}

	public void setDecryptedData(String decryptedData) {
		this.decryptedData = decryptedData;
	}


	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	
	

	
}
