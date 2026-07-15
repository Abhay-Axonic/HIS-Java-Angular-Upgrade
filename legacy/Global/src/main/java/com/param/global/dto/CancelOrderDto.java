package com.param.global.dto;

import java.util.List;

public class CancelOrderDto {

	private Integer orderDetailsId;

	private Integer userId;

	private Integer ordCancelReasonId;
	
	private String ordCancelRemark;

	private Integer encounterId;

	private Integer admissionId;

	private Integer specialityId;

	private Character preOrPostBilling;

	private Integer visitTypeId;

	private Integer saleId;

	private Integer screenId;
	private Integer pharmacyServiceId;
	private Integer tariffId;
	private Integer opdBillingCatId;
	
	public String getOrdCancelRemark() {
		return ordCancelRemark;
	}

	public void setOrdCancelRemark(String ordCancelRemark) {
		this.ordCancelRemark = ordCancelRemark;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getPreOrPostBilling() {
		return preOrPostBilling;
	}

	public void setPreOrPostBilling(Character preOrPostBilling) {
		this.preOrPostBilling = preOrPostBilling;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;

	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}

	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public Integer getPharmacyServiceId() {
		return pharmacyServiceId;
	}

	public void setPharmacyServiceId(Integer pharmacyServiceId) {
		this.pharmacyServiceId = pharmacyServiceId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getOpdBillingCatId() {
		return opdBillingCatId;
	}

	public void setOpdBillingCatId(Integer opdBillingCatId) {
		this.opdBillingCatId = opdBillingCatId;
	}

}
