package com.param.global.dto.global;

import java.util.Date;

public class PurchaseOrderHistory {

	private String poNumber;

	private Date poDate;

	private String grnNumber;

	private Date grnDate;

	private String invoiceNumber;

	private String batchNumber;

	private String productName;

	private String vendorName;

	private Integer receivedQty;

	private Integer approvedReceivedQty;

	private Integer bonusQty;

	private Integer approvedBonusQty;

	private Double cop;
	private Double mrp;

	public Double getMrp() {
		return mrp;
	}


	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}


	public PurchaseOrderHistory() {
		super();
	}
	
	
	public PurchaseOrderHistory(String poNumber, Date poDate, String grnNumber, Date grnDate, String invoiceNumber,
			String batchNumber, String productName, String vendorName, Integer receivedQty, Integer approvedReceivedQty,
			Integer bonusQty, Integer approvedBonusQty, Double cop) {
		super();
		this.poNumber = poNumber;
		this.poDate = poDate;
		this.grnNumber = grnNumber;
		this.grnDate = grnDate;
		this.invoiceNumber = invoiceNumber;
		this.batchNumber = batchNumber;
		this.productName = productName;
		this.vendorName = vendorName;
		this.receivedQty = receivedQty;
		this.approvedReceivedQty = approvedReceivedQty;
		this.bonusQty = bonusQty;
		this.approvedBonusQty = approvedBonusQty;
		this.cop = cop;
	}




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

	public String getGrnNumber() {
		return grnNumber;
	}

	public void setGrnNumber(String grnNumber) {
		this.grnNumber = grnNumber;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(Integer receivedQty) {
		this.receivedQty = receivedQty;
	}

	public Integer getApprovedReceivedQty() {
		return approvedReceivedQty;
	}

	public void setApprovedReceivedQty(Integer approvedReceivedQty) {
		this.approvedReceivedQty = approvedReceivedQty;
	}

	public Integer getBonusQty() {
		return bonusQty;
	}

	public void setBonusQty(Integer bonusQty) {
		this.bonusQty = bonusQty;
	}

	public Integer getApprovedBonusQty() {
		return approvedBonusQty;
	}

	public void setApprovedBonusQty(Integer approvedBonusQty) {
		this.approvedBonusQty = approvedBonusQty;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}
	
}
