package com.param.global.billing.dto;

import java.util.List;

import com.param.entity.model.pharmacy.Sale;
import com.param.service.global.model.CookieObjectDto;

public class PharmacyBillCommonDto extends CookieObjectDto{

	private Integer id;// saleId
	private String billNo;
//	private Integer storeId;
	private Integer contractId;
//	private Integer organizationId;
//	private Integer unitId;
	private Integer visitTypeId;
	private Integer paymentEntitlementId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer billingBedCategoryId;
	private Integer patientCategoryId;
	private Integer defaultSelfTariffId;
	private Integer payeeId;
	private Integer selfPayeeId;
	private Integer patientClassId;
	//private Double patientClassPercentage;
	private Integer patientId;
	private Integer tariffId;
	private boolean taxInclusive;
	private boolean walkInSales;
	private boolean opSales;
	private Integer doctorId;
	private Double totalAmount;
	private double discount;
	private Double roundOff;
	private Double taxAmount;
	private Double netAmount;
	private Double roundOffAmount;
	private String orderDate;
	private boolean addPreAuth;
	private boolean applyAuth;
	private boolean updatePayee;
	private Sale sale;
	private Integer screenIdToSave;
	private boolean recalculation;
	private Integer pharmacyReturnServiceRecal;
	
	public Integer getPharmacyReturnServiceRecal() {
		return pharmacyReturnServiceRecal;
	}

	public void setPharmacyReturnServiceRecal(Integer pharmacyReturnServiceRecal) {
		this.pharmacyReturnServiceRecal = pharmacyReturnServiceRecal;
	}

	public boolean isRecalculation() {
		return recalculation;
	}

	public void setRecalculation(boolean recalculation) {
		this.recalculation = recalculation;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public boolean isUpdatePayee() {
		return updatePayee;
	}

	public void setUpdatePayee(boolean updatePayee) {
		this.updatePayee = updatePayee;
	}

	public boolean isApplyAuth() {
		return applyAuth;
	}

	public void setApplyAuth(boolean applyAuth) {
		this.applyAuth = applyAuth;
	}

	public boolean isAddPreAuth() {
		return addPreAuth;
	}

	public void setAddPreAuth(boolean addPreAuth) {
		this.addPreAuth = addPreAuth;
	}

	public Double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	private Double totalSelfPayable;
	private Double totalCreditPayable;
	private double totalDiscountAmount;
	private Integer saleTypeId;
	private Integer discountTypeId;
	private Integer discountCategoryId;
	private Integer prescriptionId;
	private String remark;
	private Boolean consume;
	private Integer forceAddRemoved;
	private Integer forceAddPackageId;
	private List<CommonBillingItemsDto> itemList;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

//	public Integer getOrganizationId() {
//		return organizationId;
//	}
//
//	public void setOrganizationId(Integer organizationId) {
//		this.organizationId = organizationId;
//	}
//
//	public Integer getUnitId() {
//		return unitId;
//	}
//
//	public void setUnitId(Integer unitId) {
//		this.unitId = unitId;
//	}

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

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	/*	public Double getPatientClassPercentage() {
			return patientClassPercentage;
		}
	
		public void setPatientClassPercentage(Double patientClassPercentage) {
			this.patientClassPercentage = patientClassPercentage;
		}*/

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public List<CommonBillingItemsDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<CommonBillingItemsDto> itemList) {
		this.itemList = itemList;
	}

	public boolean isTaxInclusive() {
		return taxInclusive;
	}

	public void setTaxInclusive(boolean taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

	public boolean isWalkInSales() {
		return walkInSales;
	}

	public void setWalkInSales(boolean walkInSales) {
		this.walkInSales = walkInSales;
	}

	public boolean isOpSales() {
		return opSales;
	}

	public void setOpSales(boolean opSales) {
		this.opSales = opSales;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

//	public Integer getStoreId() {
//		return storeId;
//	}
//
//	public void setStoreId(Integer storeId) {
//		this.storeId = storeId;
//	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getTotalSelfPayable() {
		return totalSelfPayable;
	}

	public void setTotalSelfPayable(Double totalSelfPayable) {
		this.totalSelfPayable = totalSelfPayable;
	}

	public Double getTotalCreditPayable() {
		return totalCreditPayable;
	}

	public void setTotalCreditPayable(Double totalCreditPayable) {
		this.totalCreditPayable = totalCreditPayable;
	}

	public double getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(double totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public Integer getSaleTypeId() {
		return saleTypeId;
	}

	public void setSaleTypeId(Integer saleTypeId) {
		this.saleTypeId = saleTypeId;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public Integer getDiscountCategoryId() {
		return discountCategoryId;
	}

	public void setDiscountCategoryId(Integer discountCategoryId) {
		this.discountCategoryId = discountCategoryId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getConsume() {
		return consume==null?true:consume;	
		}

	public void setConsume(Boolean consume) {
		this.consume = consume==null?true:consume;
	}

	public Integer getForceAddRemoved() {
		return forceAddRemoved!=null?forceAddRemoved:0;
	}

	public void setForceAddRemoved(Integer forceAddRemoved) {
		this.forceAddRemoved = forceAddRemoved!=null?forceAddRemoved:0;
	}

	public Integer getForceAddPackageId() {
		return forceAddPackageId;
	}

	public void setForceAddPackageId(Integer forceAddPackageId) {
		this.forceAddPackageId = forceAddPackageId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getScreenIdToSave() {
		return screenIdToSave;
	}

	public void setScreenIdToSave(Integer screenIdToSave) {
		this.screenIdToSave = screenIdToSave;
	}
}
