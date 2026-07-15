package com.param.billing.global;

import java.util.List;

import com.param.global.dto.OrderDetailsPayeeMapperDto;

public class CostEstimationDetailsDto {

	private Integer costEstimationDetailsId;
	private Integer contractId;
	private Integer specialityId;
	private Integer serviceSpecialityId;
	private Integer serviceSubSpecialityId;
	private Integer serviceMasterId;
	private String serviceStandardCode;
	private String serviceStandardName;
	private String specialityName;
	private Double quantity;
	private Double basePrice;
	private Double totalAmt;
	private Double totalAmtOld;
	private Double selfPayable;
	private Double creditPayable;
	private Integer createdBy;
	private Long createdDate;
	private Character isPackageService;
	private Integer packageEitherOrGroupId;
	private Character isCapService;
	private Double packagePayable;
	private Integer subSpecialityId;
	private Integer packageId;
	private Integer packageTypeId;
	private Integer packageMasterId;
	private Integer serviceRendered;
	private Character status;
	private Double ordCancelReasonId;
	private Long ordCancelDatetime;
	private String ordCancelRemark;
	private Long ordRenderDatetime;
	private Integer payeeId;
	private Integer tariffId;
	private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;
	private Integer billingBedCategoryId;
	
	/*basePrice: 1200
	contractId: null
	createdBy: 2
	createdDate: 1570853951229
	creditPayable: 0
	ordCancelDatetime: 1570853951229
	ordCancelReasonId: ""
	ordCancelRemark: ""
	ordRenderDatetime: "1570853951229"
	orderDetailsPayeeMapperDtosList: [{id: null, orderDetailsId: null, serviceId: 48, contractId: null, payeeId: 1, payeeDesc: "Self",�}]
	payeeId: 1
	quantity: 1
	selfPayable: 1200
	serviceRendered: 1
	serviceStandardName: "FIRST CONSULTATION"
	specialityId: null
	specialityName: "Consultation"
	status: "A"
	subSpecialityId: null
	tariffId: 16
	totalAmt: 1200*/
	
	public Integer getCostEstimationDetailsId() {
		return costEstimationDetailsId;
	}
	public void setCostEstimationDetailsId(Integer costEstimationDetailsId) {
		this.costEstimationDetailsId = costEstimationDetailsId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getServiceSpecialityId() {
		return serviceSpecialityId;
	}
	public void setServiceSpecialityId(Integer serviceSpecialityId) {
		this.serviceSpecialityId = serviceSpecialityId;
	}
	public Integer getServiceSubSpecialityId() {
		return serviceSubSpecialityId;
	}
	public void setServiceSubSpecialityId(Integer serviceSubSpecialityId) {
		this.serviceSubSpecialityId = serviceSubSpecialityId;
	}
	public Integer getServiceMasterId() {
		return serviceMasterId;
	}
	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}
	public String getServiceStandardCode() {
		return serviceStandardCode;
	}
	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}
	public String getServiceStandardName() {
		return serviceStandardName;
	}
	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}
	public Double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public Double getTotalAmtOld() {
		return totalAmtOld;
	}
	public void setTotalAmtOld(Double totalAmtOld) {
		this.totalAmtOld = totalAmtOld;
	}
	public Double getSelfPayable() {
		return selfPayable;
	}
	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}
	public Double getCreditPayable() {
		return creditPayable;
	}
	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Character getIsPackageService() {
		return isPackageService;
	}
	public void setIsPackageService(Character isPackageService) {
		this.isPackageService = isPackageService;
	}
	public Integer getPackageEitherOrGroupId() {
		return packageEitherOrGroupId;
	}
	public void setPackageEitherOrGroupId(Integer packageEitherOrGroupId) {
		this.packageEitherOrGroupId = packageEitherOrGroupId;
	}
	public Character getIsCapService() {
		return isCapService;
	}
	public void setIsCapService(Character isCapService) {
		this.isCapService = isCapService;
	}
	public Double getPackagePayable() {
		return packagePayable;
	}
	public void setPackagePayable(Double packagePayable) {
		this.packagePayable = packagePayable;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public Integer getPackageTypeId() {
		return packageTypeId;
	}
	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}
	public Integer getPackageMasterId() {
		return packageMasterId;
	}
	public void setPackageMasterId(Integer packageMasterId) {
		this.packageMasterId = packageMasterId;
	}
	public Integer getServiceRendered() {
		return serviceRendered;
	}
	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Double getOrdCancelReasonId() {
		return ordCancelReasonId;
	}
	public void setOrdCancelReasonId(Double ordCancelReasonId) {
		this.ordCancelReasonId = ordCancelReasonId;
	}
	public String getOrdCancelRemark() {
		return ordCancelRemark;
	}
	public void setOrdCancelRemark(String ordCancelRemark) {
		this.ordCancelRemark = ordCancelRemark;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}
	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}
	public Long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	public Long getOrdCancelDatetime() {
		return ordCancelDatetime;
	}
	public void setOrdCancelDatetime(Long ordCancelDatetime) {
		this.ordCancelDatetime = ordCancelDatetime;
	}
	public Long getOrdRenderDatetime() {
		return ordRenderDatetime;
	}
	public void setOrdRenderDatetime(Long ordRenderDatetime) {
		this.ordRenderDatetime = ordRenderDatetime;
	}
	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}
	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}
	
}
