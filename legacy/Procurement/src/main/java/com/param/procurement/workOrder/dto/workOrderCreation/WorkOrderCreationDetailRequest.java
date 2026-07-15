package com.param.procurement.workOrder.dto.workOrderCreation;

import com.param.service.global.model.CookieObjectDto;

public class WorkOrderCreationDetailRequest extends CookieObjectDto{
	
	private Integer id;

	private Double amount;

	private Double discountAmount;

	private Double discountPercentage;

	private String exclusionDetails;

	private String inclusionDetails;

	private Integer itemId;
	
	private Double netAmount;

	private String paymentTerm;

	private Integer qoBonusQuantity;

	private String remark;

	private String specification;

	private Integer statusId;

	private Double taxAmount;

	private Integer taxId;

	private Double taxPercentage;

	private Double totalAmount;

	private Integer woQuantity;

	private Integer woRequestDetail;

	private Integer workOrderQuotationDetail;

	public WorkOrderCreationDetailRequest() {
		super();
	}
	
	
	public WorkOrderCreationDetailRequest(Integer id, Double amount, Double discountAmount, Double discountPercentage,
			String exclusionDetails, String inclusionDetails, Integer itemId, 
			Double netAmount, String paymentTerm, Integer qoBonusQuantity, String remark, String specification,
			Integer statusId, Double taxAmount, Integer taxId, Double taxPercentage, Double totalAmount,
			Integer woQuantity) {
		super();
		this.id = id;
		this.amount = amount;
		this.discountAmount = discountAmount;
		this.discountPercentage = discountPercentage;
		this.exclusionDetails = exclusionDetails;
		this.inclusionDetails = inclusionDetails;
		this.itemId = itemId;
		this.netAmount = netAmount;
		this.paymentTerm = paymentTerm;
		this.qoBonusQuantity = qoBonusQuantity;
		this.remark = remark;
		this.specification = specification;
		this.statusId = statusId;
		this.taxAmount = taxAmount;
		this.taxId = taxId;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.woQuantity = woQuantity;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(String exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public String getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(String inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public Integer getQoBonusQuantity() {
		return qoBonusQuantity;
	}

	public void setQoBonusQuantity(Integer qoBonusQuantity) {
		this.qoBonusQuantity = qoBonusQuantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getWoQuantity() {
		return woQuantity;
	}

	public void setWoQuantity(Integer woQuantity) {
		this.woQuantity = woQuantity;
	}

	public Integer getWoRequestDetail() {
		return woRequestDetail;
	}

	public void setWoRequestDetail(Integer woRequestDetail) {
		this.woRequestDetail = woRequestDetail;
	}

	public Integer getWorkOrderQuotationDetail() {
		return workOrderQuotationDetail;
	}

	public void setWorkOrderQuotationDetail(Integer workOrderQuotationDetail) {
		this.workOrderQuotationDetail = workOrderQuotationDetail;
	}
	
	
	

	

}
