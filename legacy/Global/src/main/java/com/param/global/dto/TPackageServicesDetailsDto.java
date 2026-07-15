package com.param.global.dto;

import java.util.Objects;

public class TPackageServicesDetailsDto {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TPackageServicesDetailsDto that = (TPackageServicesDetailsDto) o;
		return serviceId.equals(that.serviceId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceId);
	}


	private Integer packageServiceDetailsId;
	private Integer packageId;
	private Integer serviceId;
	private Integer serviceMasterId;
	private Integer numberToBeUse;
	private Double servicePrice;
	private Double total;
	private Double apportionedPrice;
	private Integer orgnisationId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Character status;
	private Integer packageEitherOrGroupId;
	private Integer isServiceItem;
	private Integer itemId;
	private Integer periodicityId;
	private String serviceName;
	private String serviceCode;
	private Integer specialityId;
	private Integer subSpecialityId;
	private String specialityName;
	private String subSpecialityName;
	private String itemName;
	private String itemCode;
	private Character isRefDoctorShare;
	private Character isDocReq;
	private Double refSharePer;
	private Double docSharePer;
	private Integer consultationSpecialityId;
	private Integer parentPackageId;
	private int isCap;
	private Double serviceCap;
	private Integer productCategoryId;
	private Integer isGroupwiseExlusion;
	
	private Integer balancePackageQuantity;
	private Integer packageQuantity;
	private Double availableCap;
	private Double consumedAmt;
	private Integer consumedQty;
    private Character preRequisite;
    private String preRequisiteDetails;

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getPackageServiceDetailsId() {
		return packageServiceDetailsId;
	}

	public void setPackageServiceDetailsId(Integer packageServiceDetailsId) {
		this.packageServiceDetailsId = packageServiceDetailsId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getNumberToBeUse() {
		return numberToBeUse;
	}

	public void setNumberToBeUse(Integer numberToBeUse) {
		this.numberToBeUse = numberToBeUse;
	}

	public Double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Double getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(Double apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getPackageEitherOrGroupId() {
		return packageEitherOrGroupId;
	}

	public void setPackageEitherOrGroupId(Integer packageEitherOrGroupId) {
		this.packageEitherOrGroupId = packageEitherOrGroupId;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPeriodicityId() {
		return periodicityId;
	}

	public void setPeriodicityId(Integer periodicityId) {
		this.periodicityId = periodicityId;
	}

	public String getItemName() {
		return itemName;
	}

	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = isDocReq;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public int getIsCap() {
		return isCap;
	}

	public void setIsCap(int isCap) {
		this.isCap = isCap;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Double getServiceCap() {
		return serviceCap;
	}

	public void setServiceCap(Double serviceCap) {
		this.serviceCap = serviceCap;
	}

	public Double getRefSharePer() {
		return refSharePer;
	}

	public void setRefSharePer(Double refSharePer) {
		this.refSharePer = refSharePer;
	}

	public Double getDocSharePer() {
		return docSharePer;
	}

	public void setDocSharePer(Double docSharePer) {
		this.docSharePer = docSharePer;
	}

	public Integer getBalancePackageQuantity() {
		return balancePackageQuantity;
	}

	public void setBalancePackageQuantity(Integer balancePackageQuantity) {
		this.balancePackageQuantity = balancePackageQuantity;
	}

	public Integer getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(Integer packageQuantity) {
		this.packageQuantity = packageQuantity;
	}

	public Integer getIsGroupwiseExlusion() {
		return isGroupwiseExlusion;
	}

	public void setIsGroupwiseExlusion(Integer isGroupwiseExlusion) {
		this.isGroupwiseExlusion = isGroupwiseExlusion;
	}

	public Double getAvailableCap() {
		return availableCap;
	}

	public void setAvailableCap(Double availableCap) {
		this.availableCap = availableCap;
	}

	public Double getConsumedAmt() {
		return consumedAmt;
	}

	public void setConsumedAmt(Double consumedAmt) {
		this.consumedAmt = consumedAmt;
	}

	public Integer getConsumedQty() {
		return consumedQty;
	}

	public void setConsumedQty(Integer consumedQty) {
		this.consumedQty = consumedQty;
	}

	public Character getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(Character preRequisite) {
		this.preRequisite = preRequisite;
	}

	public String getPreRequisiteDetails() {
		return preRequisiteDetails;
	}

	public void setPreRequisiteDetails(String preRequisiteDetails) {
		this.preRequisiteDetails = preRequisiteDetails;
	}

	
}
