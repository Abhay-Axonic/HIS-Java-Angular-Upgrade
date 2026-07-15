package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_patient_pan_card_doc", schema = "public")
@SequenceGenerator(name = "patient_pan_card_doc_seq", sequenceName = "public.patient_pan_card_doc_seq", allocationSize = 1)
public class PatientPanCardDoc {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_pan_card_doc_seq")
	@Column(name = "patient_pan_card_doc_id")
	private Integer id;
	
	@Column(name = "bill_id")
	private Integer billId;
	
	@Column(name = "doc_url")
	private String docUrl;
	
	@Column(name = "doc_name")
	private String docName;
	
	@Column(name = "is_pharmacy")
	private Boolean isPharmacy;
	
	@Column(name = "is_deposite")
	private Boolean isDeposite;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "is_miscellaneous")
	private Boolean isMiscellaneous;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public Boolean getIsPharmacy() {
		return isPharmacy;
	}

	public void setIsPharmacy(Boolean isPharmacy) {
		this.isPharmacy = isPharmacy;
	}

	public Boolean getIsDeposite() {
		return isDeposite;
	}

	public void setIsDeposite(Boolean isDeposite) {
		this.isDeposite = isDeposite;
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

	public Boolean getIsMiscellaneous() {
		return isMiscellaneous;
	}

	public void setIsMiscellaneous(Boolean isMiscellaneous) {
		this.isMiscellaneous = isMiscellaneous;
	}
	
	
	
}
