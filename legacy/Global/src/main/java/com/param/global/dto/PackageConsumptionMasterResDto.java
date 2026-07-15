package com.param.global.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class PackageConsumptionMasterResDto {

	private Integer packageConsumptionMasterId;
	private String orderNo;
	private Integer packageId;
	private Integer packageServiceId;
	private String packageCode;
	private String packageName;
	private double packagePrice;
	private double packageNetPayable;
	private String orderBy;
	private String orderDate;
	private Date orderDateNew;
	private Integer orderDetailId;
	private String validityFrom;
	private String validityTo;
	private Integer packageTypeId;
	private Integer applicableNoOfVisit;
	private BigInteger totalDeposite;
	private BigInteger balanceDeposite;
	private char isCancelled;
	private char isDiscountinued;
	private String cancelledDate;
	private Integer cancelledBy;
	private Integer cancelledReasonId;
	private String cancelledRemark;
	private Integer discountinuedBy;
	private Integer discountinuedReasonId;
	private String discountinuedDate;
	private String discountinuedRemark;
	private char status;
	private Integer organisationId;
	private Integer unitId;
	private Integer encounterSpecialityId;
	private boolean isVisited;
	private Integer encounterId;
	private Integer tariffId;
	private String userName;
	private String fromDate;
	private String toDate;
    private Integer billingBedCategoryId;
	private Integer isForceClose;
	private List<PackageConsumptionServiceDetailDto> listPackageConsumptionServiceDetailDto;
	private List<TPackageConsumptionCapDetailsDto> listTPackageConsumptionCapDetailsDto;
	private List<PackageConsumptionServiceDetailDto> listOrderedInclusionExclusionServices;
	private List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto;
	private List<Integer> bbcIds;
	private Integer billStatusId;
	private boolean billingBedExist;
	private String packageApplicableDate;
	private String  packageApplicableDateDt;
	private String packageApplicableDateDtold;
	private Integer patientId;
	private String orderDateString;
	private Date packageApplicableDateNew;
	private Character isIntermediateBill;
	private List<PackageConsumptionMasterResDto> listPackageOrder;

	private String addedDate;

	private List<String>dates;

	private String createdDate;

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public List<PackageConsumptionMasterResDto> getListPackageOrder() {
		return listPackageOrder;
	}

	public void setListPackageOrder(List<PackageConsumptionMasterResDto> listPackageOrder) {
		this.listPackageOrder = listPackageOrder;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPackageApplicableDateDtold() {
		return packageApplicableDateDtold;
	}

	public void setPackageApplicableDateDtold(String packageApplicableDateDtold) {
		this.packageApplicableDateDtold = packageApplicableDateDtold;
	}

	public String getPackageApplicableDateDt() {
		return packageApplicableDateDt;
	}

	public void setPackageApplicableDateDt(String packageApplicableDateDt) {
		this.packageApplicableDateDt = packageApplicableDateDt;
	}

	public String getPackageApplicableDate() {
		return packageApplicableDate;
	}

	public void setPackageApplicableDate(String packageApplicableDate) {
		this.packageApplicableDate = packageApplicableDate;
	}

	public boolean isBillingBedExist() {
		return billingBedExist;
	}

	public void setBillingBedExist(boolean billingBedExist) {
		this.billingBedExist = billingBedExist;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getPackageServiceId() {
		return packageServiceId;
	}

	public void setPackageServiceId(Integer packageServiceId) {
		this.packageServiceId = packageServiceId;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public double getPackageNetPayable() {
		return packageNetPayable;
	}

	public void setPackageNetPayable(double packageNetPayable) {
		this.packageNetPayable = packageNetPayable;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getApplicableNoOfVisit() {
		return applicableNoOfVisit;
	}

	public void setApplicableNoOfVisit(Integer applicableNoOfVisit) {
		this.applicableNoOfVisit = applicableNoOfVisit;
	}

	public BigInteger getTotalDeposite() {
		return totalDeposite;
	}

	public void setTotalDeposite(BigInteger totalDeposite) {
		this.totalDeposite = totalDeposite;
	}

	public BigInteger getBalanceDeposite() {
		return balanceDeposite;
	}

	public void setBalanceDeposite(BigInteger balanceDeposite) {
		this.balanceDeposite = balanceDeposite;
	}

	public char getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(char isCancelled) {
		this.isCancelled = isCancelled;
	}

	public char getIsDiscountinued() {
		return isDiscountinued;
	}

	public void setIsDiscountinued(char isDiscountinued) {
		this.isDiscountinued = isDiscountinued;
	}

	public String getCancelledDate() {
		return cancelledDate;
	}

	public void setCancelledDate(String cancelledDate) {
		this.cancelledDate = cancelledDate;
	}

	public Integer getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public Integer getCancelledReasonId() {
		return cancelledReasonId;
	}

	public void setCancelledReasonId(Integer cancelledReasonId) {
		this.cancelledReasonId = cancelledReasonId;
	}

	public String getCancelledRemark() {
		return cancelledRemark;
	}

	public void setCancelledRemark(String cancelledRemark) {
		this.cancelledRemark = cancelledRemark;
	}

	public Integer getDiscountinuedBy() {
		return discountinuedBy;
	}

	public void setDiscountinuedBy(Integer discountinuedBy) {
		this.discountinuedBy = discountinuedBy;
	}

	public Integer getDiscountinuedReasonId() {
		return discountinuedReasonId;
	}

	public void setDiscountinuedReasonId(Integer discountinuedReasonId) {
		this.discountinuedReasonId = discountinuedReasonId;
	}

	public String getDiscountinuedDate() {
		return discountinuedDate;
	}

	public void setDiscountinuedDate(String discountinuedDate) {
		this.discountinuedDate = discountinuedDate;
	}

	public String getDiscountinuedRemark() {
		return discountinuedRemark;
	}

	public void setDiscountinuedRemark(String discountinuedRemark) {
		this.discountinuedRemark = discountinuedRemark;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public List<PackageConsumptionServiceDetailDto> getListPackageConsumptionServiceDetailDto() {
		return listPackageConsumptionServiceDetailDto;
	}

	public void setListPackageConsumptionServiceDetailDto(
			List<PackageConsumptionServiceDetailDto> listPackageConsumptionServiceDetailDto) {
		this.listPackageConsumptionServiceDetailDto = listPackageConsumptionServiceDetailDto;
	}

	public List<TPackageConsumptionCapDetailsDto> getListTPackageConsumptionCapDetailsDto() {
		return listTPackageConsumptionCapDetailsDto;
	}

	public void setListTPackageConsumptionCapDetailsDto(
			List<TPackageConsumptionCapDetailsDto> listTPackageConsumptionCapDetailsDto) {
		this.listTPackageConsumptionCapDetailsDto = listTPackageConsumptionCapDetailsDto;
	}

	public List<PackageConsumptionServiceDetailDto> getListOrderedInclusionExclusionServices() {
		return listOrderedInclusionExclusionServices;
	}

	public void setListOrderedInclusionExclusionServices(
			List<PackageConsumptionServiceDetailDto> listOrderedInclusionExclusionServices) {
		this.listOrderedInclusionExclusionServices = listOrderedInclusionExclusionServices;
	}
	
	public List<TPackageIncExcDetailsDto> getListTPackageIncExcDetailsDto() {
		return listTPackageIncExcDetailsDto;
	}

	public void setListTPackageIncExcDetailsDto(List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto) {
		this.listTPackageIncExcDetailsDto = listTPackageIncExcDetailsDto;
	}

	public Integer getEncounterSpecialityId() {
		return encounterSpecialityId;
	}

	public void setEncounterSpecialityId(Integer encounterSpecialityId) {
		this.encounterSpecialityId = encounterSpecialityId;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIsForceClose() {
		return isForceClose!=null ? isForceClose : 1;
	}

	public void setIsForceClose(Integer isForceClose) {
		this.isForceClose = isForceClose!=null  ? isForceClose : 1;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public List<Integer> getBbcIds() {
		return bbcIds;
	}

	public void setBbcIds(List<Integer> bbcIds) {
		this.bbcIds = bbcIds;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Date getOrderDateNew() {
		return orderDateNew;
	}

	public void setOrderDateNew(Date orderDateNew) {
		this.orderDateNew = orderDateNew;
	}

	public String getOrderDateString() {
		return orderDateString;
	}

	public void setOrderDateString(String orderDateString) {
		this.orderDateString = orderDateString;
	}

	public Date getPackageApplicableDateNew() {
		return packageApplicableDateNew;
	}

	public void setPackageApplicableDateNew(Date packageApplicableDateNew) {
		this.packageApplicableDateNew = packageApplicableDateNew;
	}

	public Character getIsIntermediateBill() {
		return isIntermediateBill;
	}

	public void setIsIntermediateBill(Character isIntermediateBill) {
		this.isIntermediateBill = isIntermediateBill;
	}

}
