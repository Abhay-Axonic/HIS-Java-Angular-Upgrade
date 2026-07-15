package com.param.global.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;


public class BillStatDto {
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer visitTypeId;
	
	private String visitType;
	
	private Integer paymentModeId;
	
	private String paymentMode;
	
	private BigDecimal amount;
	
	private Integer bedCategoryId;
	
	private String bedCategory;
	
	private Integer bedVacantCount;
	
	private Integer houskeepingCount;
	
	private Integer occupiedCount;
	
	private Integer procedureCount;
	
	private Integer ehcCount;
	
	private Integer labCount;
	
	private Integer radiologyCount;
	
    private Integer dischargeCount;
	
	private Integer admissionCount;
	
	private Integer deathCount;
	
	private Integer userDefinedVisitTypeId;

	private Integer totatPatCount;
	
    private Integer creditNoteCount;
	
	private Integer discountCount;
	
	private Integer insuranceCount;
	
	private Integer corporateCompanyCount;
	
	private Integer refundCount;
	
	private List <BillStatDto> billingCollectionList;
	
	private List <BillStatDto> operationList;
	
	private List<BillStatDto> OPBillStatList;
	
	private List<BillStatDto> IPBillStatList;
	
	private List<BillStatDto> DCBillStatList;
	
	private List<BillStatDto> ERBillStatList;
	
	private List<Integer> intcountArr;
	
	private List<Integer> financeCountArr;
 
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

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public void setBedVacantCount(Integer bedVacantCount) {
		this.bedVacantCount = bedVacantCount;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}


	public String getBedCategory() {
		return bedCategory;
	}

	public void setBedCategory(String bedCategory) {
		this.bedCategory = bedCategory;
	}

	public Integer getBedVacantCount() {
		return bedVacantCount;
	}

	public void setBed_vacantCount(Integer bedVacantCount) {
		this.bedVacantCount = bedVacantCount;
	}

	public Integer getHouskeepingCount() {
		return houskeepingCount;
	}

	public void setHouskeepingCount(Integer houskeepingCount) {
		this.houskeepingCount = houskeepingCount;
	}

	public Integer getOccupiedCount() {
		return occupiedCount;
	}

	public void setOccupiedCount(Integer occupiedCount) {
		this.occupiedCount = occupiedCount;
	}

	public List<BillStatDto> getBillingCollectionList() {
		return billingCollectionList;
	}

	public void setBillingCollectionList(List<BillStatDto> billingCollectionList) {
		this.billingCollectionList = billingCollectionList;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<BillStatDto> getOPBillStatList() {
		return OPBillStatList;
	}

	public void setOPBillStatList(List<BillStatDto> oPBillStatList) {
		OPBillStatList = oPBillStatList;
	}

	public List<BillStatDto> getIPBillStatList() {
		return IPBillStatList;
	}

	public void setIPBillStatList(List<BillStatDto> iPBillStatList) {
		IPBillStatList = iPBillStatList;
	}

	public List<BillStatDto> getDCBillStatList() {
		return DCBillStatList;
	}

	public void setDCBillStatList(List<BillStatDto> dCBillStatList) {
		DCBillStatList = dCBillStatList;
	}

	public List<BillStatDto> getERBillStatList() {
		return ERBillStatList;
	}

	public void setERBillStatList(List<BillStatDto> eRBillStatList) {
		ERBillStatList = eRBillStatList;
	}

	public Integer getProcedureCount() {
		return procedureCount;
	}

	public void setProcedureCount(Integer procedureCount) {
		this.procedureCount = procedureCount;
	}

	public Integer getEhcCount() {
		return ehcCount;
	}

	public void setEhcCount(Integer ehcCount) {
		this.ehcCount = ehcCount;
	}

	public Integer getLabCount() {
		return labCount;
	}

	public void setLabCount(Integer labCount) {
		this.labCount = labCount;
	}

	public Integer getRadiologyCount() {
		return radiologyCount;
	}

	public void setRadiologyCount(Integer radiologyCount) {
		this.radiologyCount = radiologyCount;
	}

	public List<BillStatDto> getOperationList() {
		return operationList;
	}

	public void setOperationList(List<BillStatDto> operationList) {
		this.operationList = operationList;
	}

	public List<Integer> getIntcountArr() {
		return intcountArr;
	}

	public void setIntcountArr(List<Integer> intcountArr) {
		this.intcountArr = intcountArr;
	}
	
	public Integer getDischargeCount() {
		return dischargeCount;
	}

	public void setDischargeCount(Integer dischargeCount) {
		this.dischargeCount = dischargeCount;
	}

	public Integer getAdmissionCount() {
		return admissionCount;
	}

	public void setAdmissionCount(Integer admissionCount) {
		this.admissionCount = admissionCount;
	}

	public Integer getDeathCount() {
		return deathCount;
	}

	public void setDeathCount(Integer deathCount) {
		this.deathCount = deathCount;
	}
	
	public Integer getUserDefinedVisitTypeId() {
		return userDefinedVisitTypeId;
	}

	public void setUserDefinedVisitTypeId(Integer userDefinedVisitTypeId) {
		this.userDefinedVisitTypeId = userDefinedVisitTypeId;
	}

	public Integer getTotatPatCount() {
		return totatPatCount;
	}

	public void setTotatPatCount(Integer totatPatCount) {
		this.totatPatCount = totatPatCount;
	}
	

	public Integer getCreditNoteCount() {
		return creditNoteCount;
	}

	public void setCreditNoteCount(Integer creditNoteCount) {
		this.creditNoteCount = creditNoteCount;
	}

	public Integer getDiscountCount() {
		return discountCount;
	}

	public void setDiscountCount(Integer discountCount) {
		this.discountCount = discountCount;
	}

	public Integer getInsuranceCount() {
		return insuranceCount;
	}

	public void setInsuranceCount(Integer insuranceCount) {
		this.insuranceCount = insuranceCount;
	}

	public Integer getCorporateCompanyCount() {
		return corporateCompanyCount;
	}

	public void setCorporateCompanyCount(Integer corporateCompanyCount) {
		this.corporateCompanyCount = corporateCompanyCount;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}

	public List<Integer> getFinanceCountArr() {
		return financeCountArr;
	}

	public void setFinanceCountArr(List<Integer> financeCountArr) {
		this.financeCountArr = financeCountArr;
	}

}
