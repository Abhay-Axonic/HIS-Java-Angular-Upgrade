package com.param.global.dto;

import java.math.BigDecimal;
import java.util.List;

public class ContractConsumptionDto {

	private Integer priorityId;

	private Integer contractId;

	private Integer companyId;

	private Double finalPrice;

	private Integer organizationId;

	private Integer unitId;

	private Integer patientTypeId;

	private Integer visitTypeId;

	private Integer paymentEntitlementId;

	private Integer serviceId;

	private String currentDate;

	private Integer encounterId;

	private Integer admissionId;

	private Integer createdBy;

	private Double selfPayable;

	private Double creaditPayable;

	private Double coShare;

	private Double balanceCapAmount;

	private Integer subSpecialityId;

	private Integer billingBedCategoryId;

	private Integer patientCategoryId;

	private Integer defaultSelfTariffId;

	private Integer payeeId;

	private Integer selfPayeeId;

	private Integer taxId;

	private Double taxPercentage;

	private Double serviceRate;

	private Integer systemDefiedVisitType;

	private Double taxAmt;

	private String serviceName;

	private Double netAmount;

	private Double ordTotalAmount;

	private String specialityName;

	private Integer patientClassId;

	private Double patientClassPercentage;

	private Double concession;

	private Boolean markupStatus;

	private Boolean tillDischarge;

	private Double minRateEditable;

	private Double maxRateEditable;

	private Integer isFromEncounter;

	private String companyName;
	private String contractName;
	private String insuranceName;
	private String paymentEntitlementName;
	private String insuranceNumber;
	private BigDecimal insuredAmount;
	private String startDate;
	private String endDate;
	private Integer priority;
	private Double balanceAmount;// Ganesh
	private Double balanceQuantity;// Ganesh
	private Integer itemId;// Ganesh
	private Integer productCategoryId; // Ganesh
	private Integer patientId;// Ganesh
	private Integer patientSponsorDetailsId;// Ganesh;
	private boolean isPackage;// yogesh
	private Integer applicableDays;

	private double ordDiscount;// for patient class

	private double ordDiscountPer;

	private Integer tariffId;

	private Integer dayDiff;

	private Integer specialityId;

	private Integer type;

	private Integer billingBedExist;

	private String applicableDate;

	private Double amount;

	private Double quantity;

	private Character isRateEditable;

	private Character isQuantityEditable;

	private Double consultationAmount;

	private Character isTariffRateApplicable;

	private Character isContractChanged;// Ganesh

	private Character isPayeeUpdated;// Ganesh

	private Character isSmartApplicable;// Ganesh

	private BigDecimal packageConsumptionAmt;

	private Integer orderDetailsId;

	private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;

	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;

	private List<OrderDetailsMasterDto> listOrderDetailsMasterDto;

	private Character isFromPackage;

	private Integer packageMasterId;

	private Character isConsultationService;

	private Character isPeriodicityService;

	private Character updateCap;

	private int consumeService;

	private Integer serviceChargeStatus;

	private Character isDocReq;

	private Character isRefReq;

	private Double changedPrice;

	private Double preAuthBalAmt;

	private Integer preauthId;

	private Integer procedureId;

	private Integer id;

	private Integer userId;

	private Integer ordCancelReasonId;
	
	private String ordCancelRemark;

	private Character isBOM;
	
	private Character isDiscount;
	
	private Character isSchemeDiscount;
	
	private Character isAutoRender;
	
	private Character status;
	
	private Integer associateCompanyId;
	
	private Integer forceAddRemoved;
	
	private String orderDate;
	
	private String doa;
	
	private Character doesNotExist;
	
	private boolean applyAuth;
	
	private Integer saleId;
	
	private boolean patientClassApplied;
	
	private Integer ambulanceChargesServiceId;
	
	private Character isBedService;
	
    private boolean isGstApplicable;
    
    private Integer gstAmountRange;
    
    private Integer gstPercentage;
    
    private Double serviceRateForPackageAdd;
    
    private Integer docSplId;
    
    private Integer keyIndex;
    
	
	public Integer getDocSplId() {
		return docSplId;
	}

	public void setDocSplId(Integer docSplId) {
		this.docSplId = docSplId;
	}

	public Integer getAmbulanceChargesServiceId() {
		return ambulanceChargesServiceId;
	}

	public void setAmbulanceChargesServiceId(Integer ambulanceChargesServiceId) {
		this.ambulanceChargesServiceId = ambulanceChargesServiceId;
	}

	public boolean isPatientClassApplied() {
		return patientClassApplied;
	}

	public void setPatientClassApplied(boolean patientClassApplied) {
		this.patientClassApplied = patientClassApplied;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public String getOrdCancelRemark() {
		return ordCancelRemark;
	}

	public void setOrdCancelRemark(String ordCancelRemark) {
		this.ordCancelRemark = ordCancelRemark;
	}

	public boolean isApplyAuth() {
		return applyAuth;
	}

	public void setApplyAuth(boolean applyAuth) {
		this.applyAuth = applyAuth;
	}

	public Character getDoesNotExist() {
		return doesNotExist;
	}

	public void setDoesNotExist(Character doesNotExist) {
		this.doesNotExist = doesNotExist;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Character getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(Character isDiscount) {
		this.isDiscount = isDiscount;
	}

	public Character getIsSchemeDiscount() {
		return isSchemeDiscount;
	}

	public void setIsSchemeDiscount(Character isSchemeDiscount) {
		this.isSchemeDiscount = isSchemeDiscount;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrdCancelReasonId() {
		return ordCancelReasonId;
	}

	public void setOrdCancelReasonId(Integer ordCancelReasonId) {
		this.ordCancelReasonId = ordCancelReasonId;
	}

	public Character getIsBOM() {
		return isBOM;
	}

	public void setIsBOM(Character isBOM) {
		this.isBOM = isBOM;
	}

	public Character getIsPeriodicityService() {
		return isPeriodicityService;
	}

	public void setIsPeriodicityService(Character isPeriodicityService) {
		this.isPeriodicityService = isPeriodicityService;
	}

	public double getOrdDiscountPer() {
		return ordDiscountPer;
	}

	public void setOrdDiscountPer(double ordDiscountPer) {
		this.ordDiscountPer = ordDiscountPer;
	}

	public Integer getIsFromEncounter() {
		return isFromEncounter;
	}

	public void setIsFromEncounter(Integer isFromEncounter) {
		this.isFromEncounter = isFromEncounter;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public Integer getPreauthId() {
		return preauthId;
	}

	public void setPreauthId(Integer preauthId) {
		this.preauthId = preauthId;
	}

	public Double getPreAuthBalAmt() {
		return preAuthBalAmt;
	}

	public void setPreAuthBalAmt(Double preAuthBalAmt) {
		this.preAuthBalAmt = preAuthBalAmt;
	}

	/*
	 * public Double getChangedPrice() { return changedPrice!=null ?
	 * changedPrice:0.00 ; }
	 * 
	 * public void setChangedPrice(Double changedPrice) { this.changedPrice =
	 * changedPrice!=null ? changedPrice : 0.00; }
	 */

	public Character getIsDocReq() {
		return isDocReq;
	}

	public Double getChangedPrice() {
		return changedPrice;
	}

	public void setChangedPrice(Double changedPrice) {
		this.changedPrice = changedPrice;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = isDocReq;
	}

	public Character getIsRefReq() {
		return isRefReq;
	}

	public void setIsRefReq(Character isRefReq) {
		this.isRefReq = isRefReq;
	}

	public Integer getServiceChargeStatus() {
		return serviceChargeStatus;
	}

	public void setServiceChargeStatus(Integer serviceChargeStatus) {
		this.serviceChargeStatus = serviceChargeStatus;
	}

	public Character getUpdateCap() {
		return updateCap;
	}

	public void setUpdateCap(Character updateCap) {
		this.updateCap = updateCap;
	}

	public Character getIsConsultationService() {
		return isConsultationService;
	}

	public void setIsConsultationService(Character isConsultationService) {
		this.isConsultationService = (isConsultationService == null ? 'N' : isConsultationService);
	}

	public Integer getPackageMasterId() {
		return packageMasterId;
	}

	public void setPackageMasterId(Integer packageMasterId) {
		this.packageMasterId = packageMasterId;
	}

	public Character getIsFromPackage() {
		return isFromPackage;
	}

	public void setIsFromPackage(Character isFromPackage) {
		this.isFromPackage = isFromPackage;
	}

	public Character getIsTariffRateApplicable() {
		return isTariffRateApplicable;
	}

	public void setIsTariffRateApplicable(Character isTariffRateApplicable) {
		this.isTariffRateApplicable = isTariffRateApplicable;
	}

	public Double getConsultationAmount() {
		return consultationAmount;
	}

	public void setConsultationAmount(Double consultationAmount) {
		this.consultationAmount = consultationAmount;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = isQuantityEditable;
	}

	public Character getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(Character isRateEditable) {
		this.isRateEditable = isRateEditable;
	}

	public Boolean getTillDischarge() {
		return tillDischarge;
	}

	public void setTillDischarge(Boolean tillDischarge) {
		this.tillDischarge = tillDischarge;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getApplicableDate() {
		return applicableDate;
	}

	public void setApplicableDate(String applicableDate) {
		this.applicableDate = applicableDate;
	}

	public Integer getBillingBedExist() {
		return billingBedExist;
	}

	public void setBillingBedExist(Integer billingBedExist) {
		this.billingBedExist = billingBedExist;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}

	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}

	public Integer getApplicableDays() {
		return applicableDays;
	}

	public void setApplicableDays(Integer applicableDays) {
		this.applicableDays = applicableDays;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getDayDiff() {
		return dayDiff;
	}

	public void setDayDiff(Integer dayDiff) {
		this.dayDiff = dayDiff;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Double getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public void setPatientClassPercentage(Double patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage == null ? 0.00 : patientClassPercentage;
	}

	public Boolean getMarkupStatus() {
		return markupStatus;
	}

	public void setMarkupStatus(Boolean markupStatus) {
		this.markupStatus = markupStatus;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(Double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public Integer getSystemDefiedVisitType() {
		return systemDefiedVisitType;
	}

	public void setSystemDefiedVisitType(Integer systemDefiedVisitType) {
		this.systemDefiedVisitType = systemDefiedVisitType;
	}

	public Double getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(Double serviceRate) {
		this.serviceRate = serviceRate;
	}

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

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Double getCoShare() {
		return coShare==null?0.00:coShare;
	}

	public void setCoShare(Double coShare) {
		this.coShare = coShare==null?0.00:coShare;
	}

	public Double getBalanceCapAmount() {
		return balanceCapAmount;
	}

	public void setBalanceCapAmount(Double balanceCapAmount) {
		this.balanceCapAmount = balanceCapAmount;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreaditPayable() {
		return creaditPayable;
	}

	public void setCreaditPayable(Double creaditPayable) {
		this.creaditPayable = creaditPayable;
	}

	public Integer getEncounterId() {
		return (encounterId != null && encounterId > 0) ? encounterId : null;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = (encounterId != null && encounterId > 0) ? encounterId : null;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
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

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Double getMinRateEditable() {
		return minRateEditable;
	}

	public void setMinRateEditable(Double minRateEditable) {
		this.minRateEditable = minRateEditable;
	}

	public Double getMaxRateEditable() {
		return maxRateEditable;
	}

	public void setMaxRateEditable(Double maxRateEditable) {
		this.maxRateEditable = maxRateEditable;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getPaymentEntitlementName() {
		return paymentEntitlementName;
	}

	public void setPaymentEntitlementName(String paymentEntitlementName) {
		this.paymentEntitlementName = paymentEntitlementName;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public Double getBalanceAmount() {
		return balanceAmount == null ? 0 : balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Double getBalanceQuantity() {
		return balanceQuantity;
	}

	public void setBalanceQuantity(Double balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public BigDecimal getInsuredAmount() {
		return insuredAmount;
	}

	public void setInsuredAmount(BigDecimal insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPatientSponsorDetailsId() {
		return patientSponsorDetailsId;
	}

	public void setPatientSponsorDetailsId(Integer patientSponsorDetailsId) {
		this.patientSponsorDetailsId = patientSponsorDetailsId;
	}

	public Character getIsContractChanged() {
		return isContractChanged;
	}

	public void setIsContractChanged(Character isContractChanged) {
		this.isContractChanged = (isContractChanged == null) ? 'N' : isContractChanged;
	}

	public Character getIsPayeeUpdated() {
		return isPayeeUpdated;
	}

	public void setIsPayeeUpdated(Character isPayeeUpdated) {
		this.isPayeeUpdated = (isPayeeUpdated == null) ? 'N' : isPayeeUpdated;
	}

	public Character getIsSmartApplicable() {
		return isSmartApplicable;
	}

	public void setIsSmartApplicable(Character isSmartApplicable) {
		this.isSmartApplicable = (isSmartApplicable == null) ? 'N' : isSmartApplicable;
	}

	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		/*
		 * if (orderDetailsPackageMapperDtosList == null ||
		 * orderDetailsPackageMapperDtosList.size() == 0) {
		 * List<OrderDetailsPackageMapperDto> mpprList = new
		 * ArrayList<OrderDetailsPackageMapperDto>(); OrderDetailsPackageMapperDto mppr
		 * = new OrderDetailsPackageMapperDto(); mppr.setIsPackageService('N');
		 * mppr.setIsCapService('N'); mppr.setPackageAmount(BigDecimal.ZERO);
		 * mppr.setServiceId(serviceId);
		 * mppr.setServiceAmount(BigDecimal.valueOf(serviceRate));
		 * mppr.setOrganizationId(organizationId); mppr.setUnitId(unitId);
		 * mppr.setStatus('A'); mpprList.add(mppr); return mpprList; } else
		 */
		return orderDetailsPackageMapperDtosList;
	}

	public void setOrderDetailsPackageMapperDtosList(
			List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}

	public BigDecimal getPackageConsumptionAmt() {
		return (packageConsumptionAmt == null ? BigDecimal.ZERO : packageConsumptionAmt);
	}

	public void setPackageConsumptionAmt(BigDecimal packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public List<OrderDetailsMasterDto> getListOrderDetailsMasterDto() {
		return listOrderDetailsMasterDto;
	}

	public void setListOrderDetailsMasterDto(List<OrderDetailsMasterDto> listOrderDetailsMasterDto) {
		this.listOrderDetailsMasterDto = listOrderDetailsMasterDto;
	}

	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public void setIsPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public boolean isPackage() {
		return isPackage;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public int getConsumeService() {
		return consumeService;
	}

	public void setConsumeService(int consumeService) {
		this.consumeService = consumeService;
	}

	public Double getConcession() {
		return concession != null ? concession : 0.00;
	}

	public void setConcession(Double concession) {
		this.concession = concession != null ? concession : 0.00;
	}

	public Double getNetAmount() {
		return netAmount != null ? netAmount : 0.00;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount != null ? netAmount : 0.00;
	}

	public Double getOrdTotalAmount() {
		return ordTotalAmount;
	}

	public void setOrdTotalAmount(Double ordTotalAmount) {
		this.ordTotalAmount = ordTotalAmount;
	}

	public double getOrdDiscount() {
		return ordDiscount;
	}

	public void setOrdDiscount(double ordDiscount) {
		this.ordDiscount = ordDiscount;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Character getStatus() {
		return status==null?'I':status;
	}

	public void setStatus(Character status) {
		this.status = status==null?'I':status;
	}
	public Integer getForceAddRemoved() {
		return forceAddRemoved!=null?forceAddRemoved:0;
	}

	public void setForceAddRemoved(Integer forceAddRemoved) {
		this.forceAddRemoved = forceAddRemoved;
	}

	public Character getIsBedService() {
		return isBedService;
	}

	public void setIsBedService(Character isBedService) {
		this.isBedService = isBedService;
	}

	public boolean getIsGstApplicable() {
		return isGstApplicable;
	}

	public void setIsGstApplicable(boolean isGstApplicable) {
		this.isGstApplicable = isGstApplicable;
	}

	public Integer getGstAmountRange() {
		return gstAmountRange;
	}

	public void setGstAmountRange(Integer gstAmountRange) {
		this.gstAmountRange = gstAmountRange;
	}

	public Integer getGstPercentage() {
		return gstPercentage;
	}

	public void setGstPercentage(Integer gstPercentage) {
		this.gstPercentage = gstPercentage;
	}

	public Double getServiceRateForPackageAdd() {
		return serviceRateForPackageAdd;
	}

	public void setServiceRateForPackageAdd(Double serviceRateForPackageAdd) {
		this.serviceRateForPackageAdd = serviceRateForPackageAdd;
	}

	public Integer getKeyIndex() {
		return keyIndex;
	}

	public void setKeyIndex(Integer keyIndex) {
		this.keyIndex = keyIndex;
	}
}
