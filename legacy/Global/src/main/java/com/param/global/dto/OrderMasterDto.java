package com.param.global.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderMasterDto 
{
	private Integer orderId;
	private Integer orgId;
	private Integer orgUnitId;
	private String orderNo;
	private Long orderDate;
	private Integer visitTypeId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer ordDoctorId;
	private Integer patientId;
	private Integer priorityId;
	private String patientType;
	private Integer wardId;
	private Integer drCrId;
	private String ordRemarks;
	private Integer createdBy;
	private Long createdDate;
	private Integer updatedBy;
	private Long updatedDate;
	private Character orderStatus;
	private Integer orderStatusId;
	private Integer paymentEntitlementTypeId;
	private Integer defaultSelfTariffId;
	private double discountPercentage;
	private List<OrderDetailsMasterDto> listOrderDetailsMasterDto;
	private Integer doctorId;
	private String currentDate;
	private Integer patientTypeId;
	private Integer patientClassId;
	private Integer billingBedCategoryId;
	private Integer contractId;
	private Integer payeeId;
	private BigDecimal reqDisAmt;
	private BigDecimal netAmount;
	private Integer discType;
	private Integer requestSendTo;
	private Character isPharmacy;
	private Integer intrimBillId;
	private String intrimBillNo;
	private Integer saleId;
	private Integer referralId;
	private Character isPreOrPostBilling;
	private Integer pharmacyServiceId;
	private List<OrderDetailsMasterDto> lineLevelDiscountOrderList;
	private List<SlotMasterDto> slotMasterDtoList;
	private Integer tariffId;
	private Integer patientCategoryId;
	private Integer ordDocSplId ;
	private Integer systemDefiedVisitType;
    private OrderDetailsMasterDto orderDetailsMasterDto;
    private Integer defaultDiscountOwnership;
    private Integer selfPayeeId;
    private boolean homeCollection;
    private boolean addToBill;
    private Integer paymentEntitlementId;
    private Character blockReportPrint;
    private Integer insuranceId;
    private String branchName;
	private String orderTs;
	private String ipoptype;
	private String uHID;
	private String iPOPNo;
	private String salutation;
	private String patientName;
	private String gender;
	private String birthDate;
	private String ward;
	private String patMobileNo;
	private String patEmailId;
	private String patAddress;
	private String patCity;
	private String refDrCode;
	private String refDrName;
	private Boolean feesPaid;
	private String neuronPlusUrl;
	private BigDecimal pendingSelfPayable;
	private Double depositAmount;
	private List<OrderDetailsMasterDto> orderTests;
	private String returnDate;
	private Boolean isFutureService = false;


	public boolean isAddToBill() {
		return addToBill;
	}

	public void setAddToBill(boolean addToBill) {
		this.addToBill = addToBill;
	}

	public boolean isHomeCollection() {
		return homeCollection;
	}

	public void setHomeCollection(boolean homeCollection) {
		this.homeCollection = homeCollection;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getDefaultDiscountOwnership() {
		return defaultDiscountOwnership;
	}

	public void setDefaultDiscountOwnership(Integer defaultDiscountOwnership) {
		this.defaultDiscountOwnership = defaultDiscountOwnership;
	}

	public OrderDetailsMasterDto getOrderDetailsMasterDto() {
		return orderDetailsMasterDto;
	}

	public void setOrderDetailsMasterDto(OrderDetailsMasterDto orderDetailsMasterDto) {
		this.orderDetailsMasterDto = orderDetailsMasterDto;
	}

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}
	public Integer getSystemDefiedVisitType() {
		return systemDefiedVisitType;
	}

	public void setSystemDefiedVisitType(Integer systemDefiedVisitType) {
		this.systemDefiedVisitType = systemDefiedVisitType;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public List<OrderDetailsMasterDto> getLineLevelDiscountOrderList() {
		return lineLevelDiscountOrderList;
	}

	public void setLineLevelDiscountOrderList(List<OrderDetailsMasterDto> lineLevelDiscountOrderList) {
		this.lineLevelDiscountOrderList = lineLevelDiscountOrderList;
	}

	public Integer getIntrimBillId() {
		return intrimBillId;
	}

	public void setIntrimBillId(Integer intrimBillId) {
		this.intrimBillId = intrimBillId;
	}

	public String getIntrimBillNo() {
		return intrimBillNo;
	}

	public void setIntrimBillNo(String intrimBillNo) {
		this.intrimBillNo = intrimBillNo;
	}

	private List<OrderMasterDto> listOrderMasterDto;

	public Integer getDiscType() {
		return discType;
	}

	public void setDiscType(Integer discType) {
		this.discType = discType;
	}

	public Integer getRequestSendTo() {
		return requestSendTo;
	}

	public void setRequestSendTo(Integer requestSendTo) {
		this.requestSendTo = requestSendTo;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public List<OrderMasterDto> getListOrderMasterDto() {
		return listOrderMasterDto;
	}

	public void setListOrderMasterDto(List<OrderMasterDto> listOrderMasterDto) {
		this.listOrderMasterDto = listOrderMasterDto;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getOrdDoctorId() {
		return ordDoctorId;
	}

	public void setOrdDoctorId(Integer ordDoctorId) {
		this.ordDoctorId = ordDoctorId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getDrCrId() {
		return drCrId;
	}

	public void setDrCrId(Integer drCrId) {
		this.drCrId = drCrId;
	}

	public String getOrdRemarks() {
		return ordRemarks;
	}

	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderDetailsMasterDto> getListOrderDetailsMasterDto() {
		return listOrderDetailsMasterDto;
	}

	public void setListOrderDetailsMasterDto(List<OrderDetailsMasterDto> listOrderDetailsMasterDto) {
		this.listOrderDetailsMasterDto = listOrderDetailsMasterDto;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Character getIsPharmacy() {
		return isPharmacy;
	}

	public void setIsPharmacy(Character isPharmacy) {
		this.isPharmacy = (isPharmacy != null && isPharmacy != '\u0000') ? isPharmacy : 'N';
	}

	public List<SlotMasterDto> getSlotMasterDtoList() {
		return slotMasterDtoList;
	}

	public void setSlotMasterDtoList(List<SlotMasterDto> slotMasterDtoList) {
		this.slotMasterDtoList = slotMasterDtoList;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getPharmacyServiceId() {
		return pharmacyServiceId;
	}

	public void setPharmacyServiceId(Integer pharmacyServiceId) {
		this.pharmacyServiceId = pharmacyServiceId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	

	public String getIpoptype() {
		return ipoptype;
	}

	public void setIpoptype(String ipoptype) {
		this.ipoptype = ipoptype;
	}

	public String getuHID() {
		return uHID;
	}

	public void setuHID(String uHID) {
		this.uHID = uHID;
	}

	public String getiPOPNo() {
		return iPOPNo;
	}

	public void setiPOPNo(String iPOPNo) {
		this.iPOPNo = iPOPNo;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	

	public String getOrderTs() {
		return orderTs;
	}

	public void setOrderTs(String orderTs) {
		this.orderTs = orderTs;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getPatMobileNo() {
		return patMobileNo;
	}

	public void setPatMobileNo(String patMobileNo) {
		this.patMobileNo = patMobileNo;
	}

	public String getPatEmailId() {
		return patEmailId;
	}

	public void setPatEmailId(String patEmailId) {
		this.patEmailId = patEmailId;
	}

	public String getPatAddress() {
		return patAddress;
	}

	public void setPatAddress(String patAddress) {
		this.patAddress = patAddress;
	}

	public String getPatCity() {
		return patCity;
	}

	public void setPatCity(String patCity) {
		this.patCity = patCity;
	}

	public String getRefDrCode() {
		return refDrCode;
	}

	public void setRefDrCode(String refDrCode) {
		this.refDrCode = refDrCode;
	}

	public String getRefDrName() {
		return refDrName;
	}

	public void setRefDrName(String refDrName) {
		this.refDrName = refDrName;
	}

	public Boolean getFeesPaid() {
		return feesPaid;
	}

	public void setFeesPaid(Boolean feesPaid) {
		this.feesPaid = feesPaid;
	}

	public List<OrderDetailsMasterDto> getOrderTests() {
		return orderTests;
	}

	public void setOrderTests(List<OrderDetailsMasterDto> orderTests) {
		this.orderTests = orderTests;
	}

	public String getNeuronPlusUrl() {
		return neuronPlusUrl;
	}

	public void setNeuronPlusUrl(String neuronPlusUrl) {
		this.neuronPlusUrl = neuronPlusUrl;
	}

	public BigDecimal getPendingSelfPayable() {
		return pendingSelfPayable;
	}

	public void setPendingSelfPayable(BigDecimal pendingSelfPayable) {
		this.pendingSelfPayable = pendingSelfPayable;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public Boolean getIsFutureService() {
		return isFutureService;
	}

	public void setIsFutureService(Boolean isFutureService) {
		this.isFutureService = isFutureService;
	}
}
