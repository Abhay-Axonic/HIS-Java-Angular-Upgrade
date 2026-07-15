package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

public class PreviousGRNSearchResponse {

	 protected Date grndate;
	 protected String grnnumber;
	 protected String invoicenumber;
	 protected String dcnumber;
	 
	 protected Integer grnapprovedqty;
	 protected Integer grnreceivedqty;
	 protected String  unit;
	 protected Integer bonusapprovedqty;
	 protected Integer bonusreceivedqty;
	 protected String  bonusunit;
	 protected Double mrp;
	 protected Double cop;
	 protected Date  expirydate;
	 protected String  batchnumber;
	 protected String vendorName;
	public Date getGrndate() {
		return grndate;
	}
	public void setGrndate(Date grndate) {
		this.grndate = grndate;
	}
	public String getGrnnumber() {
		return grnnumber;
	}
	public void setGrnnumber(String grnnumber) {
		this.grnnumber = grnnumber;
	}
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	public String getDcnumber() {
		return dcnumber;
	}
	public void setDcnumber(String dcnumber) {
		this.dcnumber = dcnumber;
	}
	public Integer getGrnapprovedqty() {
		return grnapprovedqty;
	}
	public void setGrnapprovedqty(Integer grnapprovedqty) {
		this.grnapprovedqty = grnapprovedqty;
	}
	public Integer getGrnreceivedqty() {
		return grnreceivedqty;
	}
	public void setGrnreceivedqty(Integer grnreceivedqty) {
		this.grnreceivedqty = grnreceivedqty;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getBonusapprovedqty() {
		return bonusapprovedqty;
	}
	public void setBonusapprovedqty(Integer bonusapprovedqty) {
		this.bonusapprovedqty = bonusapprovedqty;
	}
	public Integer getBonusreceivedqty() {
		return bonusreceivedqty;
	}
	public void setBonusreceivedqty(Integer bonusreceivedqty) {
		this.bonusreceivedqty = bonusreceivedqty;
	}
	public String getBonusunit() {
		return bonusunit;
	}
	public void setBonusunit(String bonusunit) {
		this.bonusunit = bonusunit;
	}
	public Double getMrp() {
		return mrp;
	}
	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}
	public Double getCop() {
		return cop;
	}
	public void setCop(Double cop) {
		this.cop = cop;
	}
	public Date getExpirydate() {
		return expirydate;
	}
	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}
	public String getBatchnumber() {
		return batchnumber;
	}
	public void setBatchnumber(String batchnumber) {
		this.batchnumber = batchnumber;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	
}
