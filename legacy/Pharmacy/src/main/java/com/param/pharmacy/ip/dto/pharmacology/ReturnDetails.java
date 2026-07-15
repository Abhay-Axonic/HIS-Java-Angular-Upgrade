 package com.param.pharmacy.ip.dto.pharmacology;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ReturnDetails
{
	private Integer returnQuantity;
	
	private Double totalAmount;


	private Long saleId;

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ReturnDetails() {
		super();
	}

	public ReturnDetails(Integer returnQuantity, Double totalAmount) {
		super();
		this.returnQuantity = returnQuantity;
		this.totalAmount = totalAmount;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}


}