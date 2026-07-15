package com.param.service.dto;

import java.util.Date;

public class ServiceForCompnayContarctReqDto {
	
	private Integer orgId;
	private Integer unitId;
	private Integer tariffId;
	private Integer paymentEntitlementId;
	private Integer companyId;
	private Integer associateCompanyId;
	private Integer contractId;
	private String assCompanyName;
	private String tariffName;
	private String paymentEntitlementName;
	private String contractName;
	private Date validityFrom;
	private Date validityTo;
	private String insuranceName;
	private Double companyShare;
	private String gradeName;
	private String arCodeDesc;
	private Integer arCodeId;
	private Integer gradeId;
	private Integer billingBedCategoryId;
	private Character isTariffRateApplicable;
	private Integer visitTypeId;
	private String billingBedDesc;
	private Character status;
	private Character isSmartApplicable;
	private String visitTypeName;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private Integer versionNo;
	
	private Character isVersioning;
	
	private Integer parentContractId;
	
	private Integer oldContractId;
	
	private String validityFromString;
	
	private String validityToString;
	
	private Integer generalLedgerId;
	
	private String generalLedgerName;
	
	public Integer getOldContractId() {
		return oldContractId;
	}
	public void setOldContractId(Integer oldContractId) {
		this.oldContractId = oldContractId;
	}
	public Integer getParentContractId() {
		return parentContractId;
	}
	public void setParentContractId(Integer parentContractId) {
		this.parentContractId = parentContractId;
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
	public Integer getGradeId() {
		return gradeId;
	}
	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}
	public String getBillingBedDesc() {
		return billingBedDesc;
	}
	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}
	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}
	public Character getIsTariffRateApplicable() {
		return isTariffRateApplicable;
	}
	public void setIsTariffRateApplicable(Character isTariffRateApplicable) {
		this.isTariffRateApplicable = isTariffRateApplicable;
	}
	public Integer getArCodeId() {
		return arCodeId;
	}
	public void setArCodeId(Integer arCodeId) {
		this.arCodeId = arCodeId;
	}
	public String getArCodeDesc() {
		return arCodeDesc;
	}
	public void setArCodeDesc(String arCodeDesc) {
		this.arCodeDesc = arCodeDesc;
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
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}
	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public String getAssCompanyName() {
		return assCompanyName;
	}
	public void setAssCompanyName(String assCompanyName) {
		this.assCompanyName = assCompanyName;
	}
	public String getTariffName() {
		return tariffName;
	}
	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}
	public String getPaymentEntitlementName() {
		return paymentEntitlementName;
	}
	public void setPaymentEntitlementName(String paymentEntitlementName) {
		this.paymentEntitlementName = paymentEntitlementName;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public Date getValidityFrom() {
		return validityFrom;
	}
	public void setValidityFrom(Date validityFrom) {
		this.validityFrom = validityFrom;
	}
	public Date getValidityTo() {
		return validityTo;
	}
	public void setValidityTo(Date validityTo) {
		this.validityTo = validityTo;
	}
	public String getInsuranceName() {
		return insuranceName;
	}
	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}
	
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public Double getCompanyShare() {
		return companyShare;
	}
	public void setCompanyShare(Double companyShare) {
		this.companyShare = companyShare;
	}
	public Character getIsSmartApplicable() {
		return isSmartApplicable;
	}
	public void setIsSmartApplicable(Character isSmartApplicable) {
		this.isSmartApplicable = isSmartApplicable;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
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
	public String getValidityFromString() {
		return validityFromString;
	}
	public void setValidityFromString(String validityFromString) {
		this.validityFromString = validityFromString;
	}
	public String getValidityToString() {
		return validityToString;
	}
	public void setValidityToString(String validityToString) {
		this.validityToString = validityToString;
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
