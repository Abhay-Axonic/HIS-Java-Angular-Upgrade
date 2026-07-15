package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

public class GRNSettledSearchResponse {
	private Integer grnid;
	private Integer receiptid;
	private String receiptnumber;
	private String paymentStatus;
	private String grnnumber;
	public String getGrnnumber() {
		return grnnumber;
	}

	public void setGrnnumber(String grnnumber) {
		this.grnnumber = grnnumber;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	private Date receiptdate;
	private Integer vendorid;
	private String vendorname;
	private Double paidamount;
	private Double balanceamount;
	private Double netamount;
	public Double getNetamount() {
		return netamount;
	}

	public void setNetamount(Double netamount) {
		this.netamount = netamount;
	}

	public Double getBalanceamount() {
		return balanceamount;
	}

	public void setBalanceamount(Double balanceamount) {
		this.balanceamount = balanceamount;
	}

	public Double getPaidamount() {
		return paidamount;
	}

	public Integer getGrnid() {
		return grnid;
	}

	public void setGrnid(Integer grnid) {
		this.grnid = grnid;
	}

	public Integer getReceiptid() {
		return receiptid;
	}

	public void setReceiptid(Integer receiptid) {
		this.receiptid = receiptid;
	}

	public String getReceiptnumber() {
		return receiptnumber;
	}

	public void setReceiptnumber(String receiptnumber) {
		this.receiptnumber = receiptnumber;
	}

	public Date getReceiptdate() {
		return receiptdate;
	}

	public void setReceiptdate(Date receiptdate) {
		this.receiptdate = receiptdate;
	}

	

	public Integer getVendorid() {
		return vendorid;
	}

	public void setVendorid(Integer vendorid) {
		this.vendorid = vendorid;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public void setPaidamount(Double paidamount) {
		this.paidamount = paidamount;
	}
}
