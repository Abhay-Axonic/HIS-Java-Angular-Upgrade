package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;
import java.util.List;

public class GRNSettledDetailsResponse {

	private Integer grnid;
	private Integer receiptid;
	private String receiptnumber;
	private Date receiptdate;
	private Integer vendorid;
	private String vendorname;
	private Double paidamount;
	private List<SettledDetails> settledDetails; 

	public List<SettledDetails> getSettledDetails() {
		return settledDetails;
	}

	public void setSettledDetails(List<SettledDetails> settledDetails) {
		this.settledDetails = settledDetails;
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
