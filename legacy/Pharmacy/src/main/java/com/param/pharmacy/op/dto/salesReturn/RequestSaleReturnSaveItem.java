package com.param.pharmacy.op.dto.salesReturn;

public class RequestSaleReturnSaveItem {

	private Long saleDetailId;

	private Integer returnQuantity;

	private Integer leaseReturnQuantity;

	private Double refundAmount;

	private Double returnTaxAmount;

	private String reason;

	private Integer statusId;
	
	private String validationMessage;
	
	private Long saleId;	

	private Double taxPercentage;
	
	private Double discount;
	
	public Long getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Long saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getLeaseReturnQuantity() {
		return leaseReturnQuantity;
	}

	public void setLeaseReturnQuantity(Integer leaseReturnQuantity) {
		this.leaseReturnQuantity = leaseReturnQuantity;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Double getReturnTaxAmount() {
		return returnTaxAmount;
	}

	public void setReturnTaxAmount(Double returnTaxAmount) {
		this.returnTaxAmount = returnTaxAmount;
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

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public Boolean ValidateSave() {
    	Boolean result = true;
    	this.validationMessage = " ";
    
    	if(this.returnQuantity == null || this.returnQuantity < 1 ) {
    		result = false;
    		this.validationMessage += "returnQuantity |";
    	}
    	/*if(this.taxId == null || this.taxId < 1 ) {
    		result = false;
    		this.validationMessage += "taxId |";
    	}
    	if(this.taxPercentage == null || this.taxPercentage < 1 ) {
    		result = false;
    		this.validationMessage += "taxPercentage |";
    	}
    	if(this.taxAmount == null || this.taxAmount < 1 ) {
    		result = false;
    		this.validationMessage += "taxAmount |";
    	}*/
        
        if(this.reason == null || this.reason.isEmpty() ) {
    		result = false;
    		this.validationMessage += "reason |";
    	}
    	return result;
    }
	
	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	
}