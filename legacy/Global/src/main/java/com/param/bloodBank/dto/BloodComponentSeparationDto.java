package com.param.bloodBank.dto;

import java.util.List;

public class BloodComponentSeparationDto {

	private Integer bloodComponentSeparationId;
	
	private Integer redCellId;
	
	private Integer transfusionTransmittedDiseasesId;
	
	private Integer technicianId;
	
	private Integer bloodGroupId;
	
	private Integer bagCapacity;
	
	private Integer collectedVolume;
	
	private String seperationDate;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private List<BloodComponentSeparationDetailsDto> bloodComponentSeparationDetailsDtoList;

	public Integer getBloodComponentSeparationId() {
		return bloodComponentSeparationId;
	}

	public void setBloodComponentSeparationId(Integer bloodComponentSeparationId) {
		this.bloodComponentSeparationId = bloodComponentSeparationId;
	}

	public Integer getRedCellId() {
		return redCellId;
	}

	public void setRedCellId(Integer redCellId) {
		this.redCellId = redCellId;
	}

	public Integer getTransfusionTransmittedDiseasesId() {
		return transfusionTransmittedDiseasesId;
	}

	public void setTransfusionTransmittedDiseasesId(Integer transfusionTransmittedDiseasesId) {
		this.transfusionTransmittedDiseasesId = transfusionTransmittedDiseasesId;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public Integer getBloodGroupId() {
		return bloodGroupId;
	}

	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}

	public Integer getBagCapacity() {
		return bagCapacity;
	}

	public void setBagCapacity(Integer bagCapacity) {
		this.bagCapacity = bagCapacity;
	}

	public Integer getCollectedVolume() {
		return collectedVolume;
	}

	public void setCollectedVolume(Integer collectedVolume) {
		this.collectedVolume = collectedVolume;
	}

	public String getSeperationDate() {
		return seperationDate;
	}

	public void setSeperationDate(String seperationDate) {
		this.seperationDate = seperationDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public List<BloodComponentSeparationDetailsDto> getBloodComponentSeparationDetailsDtoList() {
		return bloodComponentSeparationDetailsDtoList;
	}

	public void setBloodComponentSeparationDetailsDtoList(
			List<BloodComponentSeparationDetailsDto> bloodComponentSeparationDetailsDtoList) {
		this.bloodComponentSeparationDetailsDtoList = bloodComponentSeparationDetailsDtoList;
	}
}
