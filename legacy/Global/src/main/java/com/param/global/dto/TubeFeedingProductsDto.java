package com.param.global.dto;

import java.util.Date;

import javax.persistence.Column;

public class TubeFeedingProductsDto {
	
	private Integer  tubeFeedingProductsId;
	
	private String   productsCode;
	
	private String productsDesc;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private Integer noOfRecordsPerPage;

	private Integer offset;

	
	private String updatedDate;

	public Integer getTubeFeedingProductsId() {
		return tubeFeedingProductsId;
	}

	public void setTubeFeedingProductsId(Integer tubeFeedingProductsId) {
		this.tubeFeedingProductsId = tubeFeedingProductsId;
	}

	public String getProductsCode() {
		return productsCode;
	}

	public void setProductsCode(String productsCode) {
		this.productsCode = productsCode;
	}

	public String getProductsDesc() {
		return productsDesc;
	}

	public void setProductsDesc(String productsDesc) {
		this.productsDesc = productsDesc;
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

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	

}
