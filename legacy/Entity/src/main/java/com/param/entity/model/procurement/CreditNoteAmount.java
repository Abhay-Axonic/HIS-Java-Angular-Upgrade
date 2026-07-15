package com.param.entity.model.procurement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "CreditNoteAmount")
@Table(name = "t_grn_detail_creditnote_amount", schema = "procurement")
public class CreditNoteAmount extends BaseEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "mate_inward_id")
	private Integer mateinwardid;
	@Column(name = "mate_inwarddetails_id")
	private Integer mateinwarddetailsid;
	@Column(name = "tax_amount")
	private Double taxamount;
	@Column(name = "tax_percentage")
	private Double taxpercentage;
	@Column(name = "total_amount")
	private Double totalamount;
	@Column(name = "credit_note_amount")
	private Double creditnoteamount;
	
	@Column(name = "available_net_amount")
	private Double availablenetamount;
	
	public Double getAvailablenetamount() {
		return availablenetamount;
	}
	public void setAvailablenetamount(Double availablenetamount) {
		this.availablenetamount = availablenetamount;
	}
	@Column(name = "credit_tax_amount")
	private Double credittaxamount;
	@Column(name = "rg_number")
	private String rgnumber;
	@Column(name = "rg_date")
	private Date rgdate;
	@Column(name = "replacement_number")
	private String replacenumber;
	@Column(name = "credit_net_amount")
	private Double creditnetamount;
	@Column(name = "is_selected_credit")
	private Boolean isSelectedCredit;
	@Column(name = "is_checked")
	private Boolean isChecked;
	@Column(name = "consume_net_amount")
	private Double consumeNetAmount;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_id")
	private GoodReceiptNote goodReceiptNote;
	public GoodReceiptNote getGoodReceiptNote() {
		return goodReceiptNote;
	}
	public void setGoodReceiptNote(GoodReceiptNote goodReceiptNote) {
		this.goodReceiptNote = goodReceiptNote;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Double getConsumeNetAmount() {
		return consumeNetAmount;
	}
	
	public void setConsumeNetAmount(Double consumeNetAmount) {
		this.consumeNetAmount = consumeNetAmount;
	}
	/*public GoodReceiptNoteDetail getGrnDetails() {
		return grnDetails;
	}
	public void setGrnDetails(GoodReceiptNoteDetail grnDetails) {
		this.grnDetails = grnDetails;
	}
	*/
	public void updateCreditNoteAmount(Integer mateinwardid2, Integer mateinwarddetailsid2, Double taxamount2,
			Double taxpercentage, Double totalamount2, Double creditnoteamount2, Double credittaxamount2, String rgnumber2,
			Date rgdate2, String replacenumber2, Double creditnetamount2, Double consumeNetAmount2,
			Boolean isSelectedCredit2, Boolean isChecked2, Double availablenetamount2) {
	this.mateinwardid=mateinwardid2;
	this.mateinwarddetailsid= mateinwarddetailsid2;
	this.taxamount= taxamount2;
	this.taxpercentage= taxpercentage;
	this.totalamount= totalamount2;
	this.creditnoteamount= this.creditnoteamount;
	this.credittaxamount= credittaxamount2;
	this.rgnumber= rgnumber2;
	this.rgdate= rgdate2;
	this.replacenumber= replacenumber2;
	this.creditnetamount= creditnetamount2;
	this.consumeNetAmount= consumeNetAmount2;
	this.isSelectedCredit= isSelectedCredit2;
	this.isChecked= isChecked2;
	this.availablenetamount= availablenetamount2;
		
	}
}
