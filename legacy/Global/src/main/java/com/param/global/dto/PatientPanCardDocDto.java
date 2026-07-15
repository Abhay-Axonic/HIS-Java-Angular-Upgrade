package com.param.global.dto;

import org.springframework.web.multipart.MultipartFile;

public class PatientPanCardDocDto {
	
	private Integer id;
	
	private Integer billId;
	
	private String docUrl;
	
	private String docName;
	
	private Boolean isPharmacy;
	
	private Boolean isDeposite;
	
	private String createdDate;
	
	private Character status;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private MultipartFile file;
	
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Boolean getIsMiscellaneous() {
		return isMiscellaneous;
	}

	public void setIsMiscellaneous(Boolean isMiscellaneous) {
		this.isMiscellaneous = isMiscellaneous;
	}
	
	
}
