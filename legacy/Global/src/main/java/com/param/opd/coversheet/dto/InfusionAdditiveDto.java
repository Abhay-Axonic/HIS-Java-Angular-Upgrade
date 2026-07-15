package com.param.opd.coversheet.dto;

import java.math.BigInteger;

import scala.math.BigInt;

public class InfusionAdditiveDto {
	
	private Integer infusionadditiveId;
	
	private Integer itemId;
	
	private Integer genericId;
	
	private Integer strengthUnitId;
	
	private Integer roaId;
	
	private Double volume;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String additiveName;

	private Integer infusionSolutionId;
	
	private Double solutionVolume;
	private String additiveUnit;
	private BigInteger id;

	public Integer getInfusionadditiveId() {
		return infusionadditiveId;
	}

	public void setInfusionadditiveId(Integer infusionadditiveId) {
		this.infusionadditiveId = infusionadditiveId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

	public String getAdditiveName() {
		return additiveName;
	}

	public void setAdditiveName(String additiveName) {
		this.additiveName = additiveName;
	}

	

	public String getAdditiveUnit() {
		return additiveUnit;
	}

	public void setAdditiveUnit(String additiveUnit) {
		this.additiveUnit = additiveUnit;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getSolutionVolume() {
		return solutionVolume;
	}

	public void setSolutionVolume(Double solutionVolume) {
		this.solutionVolume = solutionVolume;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}
	
}
