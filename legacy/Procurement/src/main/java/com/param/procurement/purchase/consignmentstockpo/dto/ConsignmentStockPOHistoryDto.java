package com.param.procurement.purchase.consignmentstockpo.dto;

import java.util.Date;

public class ConsignmentStockPOHistoryDto {

	private String poNumber;
	
	private Date poDate;
	
	private Integer poApprovedQuantity;	
	
	private Double cop;
	
	private Double mrp;	
	
	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public Integer getPoApprovedQuantity() {
		return poApprovedQuantity;
	}

	public void setPoApprovedQuantity(Integer poApprovedQuantity) {
		this.poApprovedQuantity = poApprovedQuantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public ConsignmentStockPOHistoryDto() {
		super();
	}

	public ConsignmentStockPOHistoryDto(String poNumber, Date poDate, Integer poApprovedQuantity, Double cop,
			Double mrp) {
		super();
		this.poNumber = poNumber;
		this.poDate = poDate;
		this.poApprovedQuantity = poApprovedQuantity;
		this.cop = cop;
		this.mrp = mrp;
	}
	
}
