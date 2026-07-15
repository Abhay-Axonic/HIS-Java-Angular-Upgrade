package com.param.inventory.store.dto.materialInward;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class CreditNoteVendorWise {
	protected Integer mateinwardid;
	protected Integer mateinwarddetailsid;
	protected Double taxamount;
	protected Double taxpercentage;
	protected Double totalamount;
	protected Double creditnoteamount;
	protected Double credittaxamount;
	protected Double availablenetamount;
	protected Integer creditDays;
	protected List<CreditNoteVendorWise> rgDetails;
	public List<CreditNoteVendorWise> getRgDetails() {
		return rgDetails;
	}
	public void setRgDetails(List<CreditNoteVendorWise> rgDetails) {
		this.rgDetails = rgDetails;
	}
	protected String  inwordno;
	public String getInwordno() {
		return inwordno;
	}
	public void setInwordno(String inwordno) {
		this.inwordno = inwordno;
	}
	public Double getAvailablenetamount() {
		return availablenetamount;
	}
	public void setAvailablenetamount(Double availablenetamount) {
		this.availablenetamount = availablenetamount;
	}
	protected String rgnumber;
	protected Date rgdate;
	public String getReplacenumber() {
		return replacenumber;
	}
	public void setReplacenumber(String replacenumber) {
		this.replacenumber = replacenumber;
	}
	protected String replacenumber;
	public Integer getMateinwardid() {
		return mateinwardid;
	}
	
	public void setMateinwardid(Integer mateinwardid) {
		this.mateinwardid = mateinwardid;
	}
	public Integer getMateinwarddetailsid() {
		return mateinwarddetailsid;
	}
	
	public String getRgnumber() {
		return rgnumber;
	}
	public void setRgnumber(String rgnumber) {
		this.rgnumber = rgnumber;
	}
	public Date getRgdate() {
		return rgdate;
	}
	public void setRgdate(Date rgdate) {
		this.rgdate = rgdate;
	}
	public void setMateinwarddetailsid(Integer mateinwarddetailsid) {
		this.mateinwarddetailsid = mateinwarddetailsid;
	}
	public Double getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(Double taxamount) {
		this.taxamount = taxamount;
	}
	public Double getTaxpercentage() {
		return taxpercentage;
	}
	public void setTaxpercentage(Double taxpercentage) {
		this.taxpercentage = taxpercentage;
	}
	public Double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}
	
	public Double getCredittaxamount() {
		return credittaxamount;
	}
	public void setCredittaxamount(Double credittaxamount) {
		this.credittaxamount = credittaxamount;
	}
	public Double getCreditnetamount() {
		return creditnetamount;
	}
	public void setCreditnetamount(Double creditnetamount) {
		this.creditnetamount = creditnetamount;
	}
	public Double getCreditnoteamount() {
		return creditnoteamount;
	}
	public void setCreditnoteamount(Double creditnoteamount) {
		this.creditnoteamount = creditnoteamount;
	}
	protected Double creditnetamount;
	public Integer getCreditDays() {
		return creditDays;
	}
	public void setCreditDays(Integer creditDays) {
		this.creditDays = creditDays;
	}
}
