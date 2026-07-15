package com.param.service.dto;

public class TContractProcedureDetailsDto {

private Integer contractProcedureDetailsId;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer contractId;
	
	private Integer departmentId;
	
	private Integer procedureId;
	
	private Double capAmount;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String createdDate;
	
	private Character status;
	
	private String specialityName;
	
	private String procedureName;
	
	private Integer taxId;
	
	private Double taxPercentage;
	
	
	

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getContractProcedureDetailsId() {
		return contractProcedureDetailsId;
	}

	public void setContractProcedureDetailsId(Integer contractProcedureDetailsId) {
		this.contractProcedureDetailsId = contractProcedureDetailsId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public Double getCapAmount() {
		return capAmount;
	}

	public void setCapAmount(Double capAmount) {
		this.capAmount = capAmount;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

}
