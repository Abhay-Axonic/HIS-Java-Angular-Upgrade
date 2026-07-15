package com.param.procurement.purchase.dto.goodReceiptNote;

import java.math.BigDecimal;
import java.util.Date;

public class SettledDetails {
	private String requestingstore;
	private String  storename;
	private String grntype;
	private String  grnnumber;
	private Date grndate;
	private String  invoicenumber;
	private Date  invoicedate;
	private String  status;
	private Double netamount;
	private Integer id;
	//private Double paidamount;
	private BigDecimal paidamount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public BigDecimal getPaidamount() {
		return paidamount;
	}
	public void setPaidamount(BigDecimal paidamount) {
		this.paidamount = paidamount;
	}
	private Double balanceamount;
	private String  paymentstatus;
	public String getRequestingstore() {
		return requestingstore;
	}
	public void setRequestingstore(String requestingstore) {
		this.requestingstore = requestingstore;
	}
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getGrntype() {
		return grntype;
	}
	public void setGrntype(String grntype) {
		this.grntype = grntype;
	}
	public String getGrnnumber() {
		return grnnumber;
	}
	public void setGrnnumber(String grnnumber) {
		this.grnnumber = grnnumber;
	}
	public Date getGrndate() {
		return grndate;
	}
	public void setGrndate(Date grndate) {
		this.grndate = grndate;
	}
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public Date getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public String getPaymentstatus() {
		return paymentstatus;
	}
	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}
}
