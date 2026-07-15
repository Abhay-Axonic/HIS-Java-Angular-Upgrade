package com.param.pharmacy.op.dto.salesReturn;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;

public class ReturnItem {

	public ReturnItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Integer returnDetailId;

	private String itemName;

	private String genericName;

	private String batchNo;

	private String expiryDate;

	private String uomUnit;

	private Integer quantityIssued;

	private Double rate;

	private Double totalAmount;

	private Double discountAmount;

	private Double netAmount;

	private Integer returnQuantity;

	private Double returnTaxAmount;

	private Double refundAmount;

	private String reason;

	private Integer statusId;

	private String status;
	
	private Date batchExpiryDate;
	
	private Integer genericId;
	
	private Long saleId;
	
	private String billNo;
	
	private boolean isThreeUOMApplicable;
	
	private Long batchId;
	
	private List<UomResponse> uom;
	
	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public Integer getReturnDetailId() {
		return returnDetailId;
	}

	public void setReturnDetailId(Integer returnDetailId) {
		this.returnDetailId = returnDetailId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public Double getReturnTaxAmount() {
		return returnTaxAmount;
	}

	public void setReturnTaxAmount(Double returnTaxAmount) {
		this.returnTaxAmount = returnTaxAmount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public ReturnItem(Integer returnDetailId, String itemName, String genericName, String batchNo, String expiryDate,
			String uomUnit, Integer quantityIssued, Double rate, Double totalAmount, Double discountAmount,
			Double netAmount, Integer returnQuantity, Double refundAmount, String reason, Double returnTaxAmount) {
		super();
		this.returnDetailId = returnDetailId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.uomUnit = uomUnit;
		this.quantityIssued = quantityIssued;
		this.rate = rate;
		this.totalAmount = totalAmount;
		this.discountAmount = discountAmount;
		this.netAmount = netAmount;
		this.returnQuantity = returnQuantity;
		this.refundAmount = refundAmount;
		this.reason = reason;
		this.returnTaxAmount = returnTaxAmount;
	}

	public Date getBatchExpiryDate() {
		return batchExpiryDate;
	}

	public void setBatchExpiryDate(Date batchExpiryDate) {
		this.batchExpiryDate = batchExpiryDate;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
}