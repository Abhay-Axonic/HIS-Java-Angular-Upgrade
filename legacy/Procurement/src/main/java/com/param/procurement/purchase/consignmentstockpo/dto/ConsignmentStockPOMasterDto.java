package com.param.procurement.purchase.consignmentstockpo.dto;

import java.util.Date;
import java.util.List;

import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperRequest;
import com.param.service.global.model.CookieObjectDto;

public class ConsignmentStockPOMasterDto  extends CookieObjectDto{
	
	private Integer id;

	private Integer approvalStatusId;
	
	private Integer approvedBy;

	private String approvedDate;
	
	private Integer assetTypeId;
	
	private Integer poTypeId;

	private String cancelNote;
	
	private Integer cancelReasonId;

	private Integer discountTypeId;

	private Double discount;

	private Boolean isConsignment;

	private Double netAmount;

	private String poNumber;

	private String remark;

	private Double roundOffAmount;
	
	private Integer statusId;

	private Integer storeId;
	
	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	private Integer requestingStoreId;
	
	private Double totalAmount;

	private Double totalDiscount;

	private Double totalOtherAmount;

	private Double totalTaxAmount;

	private Integer vendorId;
	
	private Integer paymentModeId;
	
	private String paymentCode;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Integer screenId;
	
	private String vendorName;
	
	private String paymentTerm;
	
	private List<ConsignmentStockPODetailDto> items;
	private List<PoOtherChargesMapperRequest> otherCharges;

	public List<PoOtherChargesMapperRequest> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<PoOtherChargesMapperRequest> otherCharges) {
		this.otherCharges = otherCharges;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getPoTypeId() {
		return poTypeId;
	}

	public void setPoTypeId(Integer poTypeId) {
		this.poTypeId = poTypeId;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public List<ConsignmentStockPODetailDto> getItems() {
		return items;
	}

	public void setItems(List<ConsignmentStockPODetailDto> items) {
		this.items = items;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	
}
