package com.param.procurement.workOrder.dto.workOrderCreation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkOrderCreationDetailResponse extends WorkOrderCreationDetailRequest{
		
	private String itemName;
	
	private String itemCode;

	private String status;

	private String tax;

	public WorkOrderCreationDetailResponse() {
		super();
	}

	public WorkOrderCreationDetailResponse(Integer id, Double amount, Double discountAmount, Double discountPercentage,
			String exclusionDetails, String inclusionDetails, Integer itemId, Double netAmount, String paymentTerm,
			Integer qoBonusQuantity, String remark, String specification, Integer statusId, Double taxAmount,
			Integer taxId, Double taxPercentage, Double totalAmount, Integer woQuantity,String itemName,String itemCode,String status,String tax) {
		super(id, amount, discountAmount, discountPercentage, exclusionDetails, inclusionDetails, itemId, netAmount,
				paymentTerm, qoBonusQuantity, remark, specification, statusId, taxAmount, taxId, taxPercentage, totalAmount,
				woQuantity);
		
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.status = status;
		this.tax = tax;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}
}
