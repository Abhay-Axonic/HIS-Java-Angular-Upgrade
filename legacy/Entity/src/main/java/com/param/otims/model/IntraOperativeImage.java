package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_intra_operative_image" , schema = "otims")
@SequenceGenerator(name = "intra_operative_image_seq" , sequenceName = "otims.intra_operative_image_seq" , allocationSize = 1)
public class IntraOperativeImage {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "intra_operative_image_seq")
	@Column(name = "intra_operative_image_id")
	private Integer intraOperativeImageId;
	
	@Column(name = "intra_operative_id")
	private Integer intraOperativeId;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getIntraOperativeImageId() {
		return intraOperativeImageId;
	}

	public void setIntraOperativeImageId(Integer intraOperativeImageId) {
		this.intraOperativeImageId = intraOperativeImageId;
	}

	public Integer getIntraOperativeId() {
		return intraOperativeId;
	}

	public void setIntraOperativeId(Integer intraOperativeId) {
		this.intraOperativeId = intraOperativeId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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
}
