package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class CreditNoteAmountList {
	protected Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	protected Integer mateinwardid;
	protected Integer mateinwarddetailsid;
	protected Double taxamount;
	protected Double taxpercentage;
	protected Double totalamount;
	protected String inwordno;
	public String getInwordno() {
		return inwordno;
	}
	public void setInwordno(String inwordno) {
		this.inwordno = inwordno;
	}
	public List<CreditNoteVendorWise> getRgDetails() {
		return rgDetails;
	}
	public void setRgDetails(List<CreditNoteVendorWise> rgDetails) {
		this.rgDetails = rgDetails;
	}
	protected Double availablenetamount;
	protected Double balance;
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Double getAvailablenetamount() {
		return availablenetamount;
	}
	public void setAvailablenetamount(Double availablenetamount) {
		this.availablenetamount = availablenetamount;
	}
	protected Double creditnoteamount;
	protected Double credittaxamount;
	protected String rgnumber;
	protected Date rgdate;
	protected String replacenumber;
	protected Double creditnetamount;
	protected Boolean isSelectedCredit;
	protected Boolean isChecked;
	protected Double consumenetamount;
	protected List<CreditNoteVendorWise> rgDetails;
	public Integer getMateinwardid() {
		return mateinwardid;
	}
	public void setMateinwardid(Integer mateinwardid) {
		this.mateinwardid = mateinwardid;
	}
	public Integer getMateinwarddetailsid() {
		return mateinwarddetailsid;
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
	public Double getCreditnoteamount() {
		return creditnoteamount;
	}
	public void setCreditnoteamount(Double creditnoteamount) {
		this.creditnoteamount = creditnoteamount;
	}
	public Double getCredittaxamount() {
		return credittaxamount;
	}
	public void setCredittaxamount(Double credittaxamount) {
		this.credittaxamount = credittaxamount;
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
	public String getReplacenumber() {
		return replacenumber;
	}
	public void setReplacenumber(String replacenumber) {
		this.replacenumber = replacenumber;
	}
	public Double getCreditnetamount() {
		return creditnetamount;
	}
	public void setCreditnetamount(Double creditnetamount) {
		this.creditnetamount = creditnetamount;
	}
	public Boolean getIsSelectedCredit() {
		return isSelectedCredit;
	}
	public void setIsSelectedCredit(Boolean isSelectedCredit) {
		this.isSelectedCredit = isSelectedCredit;
	}
	public Boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	public Double getConsumenetamount() {
		return consumenetamount;
	}
	public void setConsumenetamount(Double consumenetamount) {
		this.consumenetamount = consumenetamount;
	}
	

}
