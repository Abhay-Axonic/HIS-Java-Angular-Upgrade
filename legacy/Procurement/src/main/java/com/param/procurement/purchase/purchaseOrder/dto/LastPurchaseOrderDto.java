package com.param.procurement.purchase.purchaseOrder.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastPurchaseOrderDto {
	  // private Integer id;  
	   private String vendorName;
	   private String retype;
	   private String ponumber;
	   private Date podate;
	   private Integer quantity;
	   private String quantityuom;
	   private Integer bonusquantity;
	   private String bonusuomunit;
	   private Double cop;
	   private Double mrp;
	   private Double taxpercentage;
	   private Double netamount;
	   private Double discount;
	   private Double totaldiscount;
	   private Double taxamount;
	   private Double otheramount;
	   private Double totalamount;
	public String getRetype() {
		return retype;
	}
	public void setRetype(String retype) {
		this.retype = retype;
	}
	public String getPonumber() {
		return ponumber;
	}
	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}
	public Date getPodate() {
		return podate;
	}
	public void setPodate(Date podate) {
		this.podate = podate;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getQuantityuom() {
		return quantityuom;
	}
	public void setQuantityuom(String quantityuom) {
		this.quantityuom = quantityuom;
	}
	public Integer getBonusquantity() {
		return bonusquantity;
	}
	public void setBonusquantity(Integer bonusquantity) {
		this.bonusquantity = bonusquantity;
	}
	public String getBonusuomunit() {
		return bonusuomunit;
	}
	public void setBonusuomunit(String bonusuomunit) {
		this.bonusuomunit = bonusuomunit;
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
	public Double getTaxpercentage() {
		return taxpercentage;
	}
	public void setTaxpercentage(Double taxpercentage) {
		this.taxpercentage = taxpercentage;
	}
	public Double getNetamount() {
		return netamount;
	}
	public void setNetamount(Double netamount) {
		this.netamount = netamount;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getTotaldiscount() {
		return totaldiscount;
	}
	public void setTotaldiscount(Double totaldiscount) {
		this.totaldiscount = totaldiscount;
	}
	public Double getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(Double taxamount) {
		this.taxamount = taxamount;
	}
	public Double getOtheramount() {
		return otheramount;
	}
	public void setOtheramount(Double otheramount) {
		this.otheramount = otheramount;
	}
	public Double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

}
