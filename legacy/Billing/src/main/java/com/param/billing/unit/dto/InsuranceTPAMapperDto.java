package com.param.billing.unit.dto;

import java.util.List;

public class InsuranceTPAMapperDto {
	
	private Integer insuranceTpaMapperId; 
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer companyId;
	
	private Integer insuranceId;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Character status;
	
	private String createdDate;
	
	private String updatedDate;
	
	private String companyDesc;
	
	private String insuranceDesc;
	
	private String insuranceCode;
	
	private String companyCode;
	
	private String arCode; 
	
	
	
	public String getArCode() {
		return arCode;
	}

	public void setArCode(String arCode) {
		this.arCode = arCode;
	}

	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	List<InsuranceTPAMapperDto> insuranceTPAMapperDtoList;

	public Integer getInsuranceTpaMapperId() {
		return insuranceTpaMapperId;
	}

	public void setInsuranceTpaMapperId(Integer insuranceTpaMapperId) {
		this.insuranceTpaMapperId = insuranceTpaMapperId;
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public List<InsuranceTPAMapperDto> getInsuranceTPAMapperDtoList() {
		return insuranceTPAMapperDtoList;
	}

	public void setInsuranceTPAMapperDtoList(List<InsuranceTPAMapperDto> insuranceTPAMapperDtoList) {
		this.insuranceTPAMapperDtoList = insuranceTPAMapperDtoList;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getInsuranceDesc() {
		return insuranceDesc;
	}

	public void setInsuranceDesc(String insuranceDesc) {
		this.insuranceDesc = insuranceDesc;
	}

	public String getInsuranceCode() {
		return insuranceCode;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}
	
	
}
