package com.param.service.dto;

import java.util.Date;

public class MCompanyContractMasterDto {

	private Integer contractId;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer paymentEntitlementId;
	
	private Integer associateCompanyId;
	
	private String contractName;
	
	private String validityFrom;
	
	private String validityTo;
	
	private Integer gradeId;
	
	private double coSharePercentage;
	
	private Integer tariffId;
	
	private Character isTariffRateApplicable;
	
	private Integer billingBedCategoryId;
	
	private Integer visitTypeId;
	
	private Integer patientTypeId;
	
	private Integer companyId;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Date updatedDate;
	
	private Date createdDate;
	
	private String assCompanyName;
	
	private String tariffName;
	
	private String paymentEntitlementName;
	
	private Character status;
	
	private Character isSmartApplicable;
	
	private Integer versionNo;
	
	private Character isVersioning;
	
	private Integer oldContractId;
	
	private Integer parentContractId;
	
	private Integer generalLedgerId;
	
	private String generalLedgerName;
	
	
	public Integer getParentContractId() {
		return parentContractId;
	}

	public void setParentContractId(Integer parentContractId) {
		this.parentContractId = parentContractId;
	}

	public Integer getOldContractId() {
		return oldContractId;
	}

	public void setOldContractId(Integer oldContractId) {
		this.oldContractId = oldContractId;
	}

	public Character getIsVersioning() {
		return isVersioning;
	}

	public void setIsVersioning(Character isVersioning) {
		this.isVersioning = isVersioning;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getPaymentEntitlementName() {
		return paymentEntitlementName;
	}

	public void setPaymentEntitlementName(String paymentEntitlementName) {
		this.paymentEntitlementName = paymentEntitlementName;
	}

	public String getTariffName() {
		return tariffName;
	}

	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}

	public String getAssCompanyName() {
		return assCompanyName;
	}

	public void setAssCompanyName(String assCompanyName) {
		this.assCompanyName = assCompanyName;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getValidityFrom() {
		return validityFrom;
	}

	public void setValidityFrom(String validityFrom) {
		this.validityFrom = validityFrom;
	}

	public String getValidityTo() {
		return validityTo;
	}

	public void setValidityTo(String validityTo) {
		this.validityTo = validityTo;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public double getCoSharePercentage() {
		return coSharePercentage;
	}

	public void setCoSharePercentage(double coSharePercentage) {
		this.coSharePercentage = coSharePercentage;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Character getIsTariffRateApplicable() {
		return isTariffRateApplicable;
	}

	public void setIsTariffRateApplicable(Character isTariffRateApplicable) {
		this.isTariffRateApplicable = isTariffRateApplicable;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Character getIsSmartApplicable() {
		return isSmartApplicable;
	}

	public void setIsSmartApplicable(Character isSmartApplicable ) {
		 this.isSmartApplicable = (isSmartApplicable == null) ? 'N' : isSmartApplicable;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	public String getGeneralLedgerName() {
		return generalLedgerName;
	}

	public void setGeneralLedgerName(String generalLedgerName) {
		this.generalLedgerName = generalLedgerName;
	}
	
}
