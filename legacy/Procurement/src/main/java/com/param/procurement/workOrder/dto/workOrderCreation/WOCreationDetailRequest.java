package com.param.procurement.workOrder.dto.workOrderCreation;

public class WOCreationDetailRequest {
	
	private Integer id;
	
	private Integer itemId;
	
	private Long batchId;
	
	private Integer woOrQrId;
	
	private Integer woQty;
	
	private Integer bonusQty;
	
	private Double amount;
	
	private Double disPercentage;
	
	private Double disAmount;
	
	private Integer taxId;
	
	private Double taxPercentage;
	
	private Double taxAmount;
	
	private Double totalAmount;
	
	private Double netAmount;
	
	private String inclusionDetails;
	
	private String exclusionDetails;
	
	private String paymentTerms;
	
	private String remark;
	
	private Integer statusId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}



	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getWoOrQrId() {
		return woOrQrId;
	}

	public void setWoOrQrId(Integer woOrQrId) {
		this.woOrQrId = woOrQrId;
	}

	public Integer getWoQty() {
		return woQty;
	}

	public void setWoQty(Integer woQty) {
		this.woQty = woQty;
	}

	public Integer getBonusQty() {
		return bonusQty;
	}

	public void setBonusQty(Integer bonusQty) {
		this.bonusQty = bonusQty;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDisPercentage() {
		return disPercentage;
	}

	public void setDisPercentage(Double disPercentage) {
		this.disPercentage = disPercentage;
	}

	public Double getDisAmount() {
		return disAmount;
	}

	public void setDisAmount(Double disAmount) {
		this.disAmount = disAmount;
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

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(String inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public String getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(String exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	public WOCreationDetailRequest()
	{
		super();
	}
	
	
	
	
	

}
