package com.param.entity.model.master;

import javax.persistence.Column;
import java.util.Date;

public class DifferentTypesScreenImageDTO {

	private Integer screenImageId;

	private byte[] image;

	private String imageURL;

	private Integer imageTypeId;

	private Integer createdBy;

	private String createdDate;

	private Character status;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organizationId;

	private Integer unitId;

	private String logoName;

	public Integer getScreenImageId() {
		return screenImageId;
	}

	public void setScreenImageId(Integer screenImageId) {
		this.screenImageId = screenImageId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Integer getImageTypeId() {
		return imageTypeId;
	}

	public void setImageTypeId(Integer imageTypeId) {
		this.imageTypeId = imageTypeId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
}
