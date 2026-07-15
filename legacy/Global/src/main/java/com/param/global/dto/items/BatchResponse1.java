package com.param.global.dto.items;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BatchResponse1 {
	protected BigInteger totalstock;
	protected Boolean isconsignment;
	protected Boolean quarantine;
	protected Double mrp;
	protected Double rate;
	protected String rack;
	protected String shelf;

	protected String batchno;
	protected Date expiry;
	protected String uomtype;
	protected BigInteger availablestockquantity;
	protected String lotno;
	protected BigInteger batchid;
	protected Double cop;
	protected Double actualcost;

	public Double getActualcost() {
		return actualcost;
	}

	public void setActualcost(Double actualcost) {
		this.actualcost = actualcost;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public BigInteger getBatchid() {
		return batchid;
	}

	public void setBatchid(BigInteger batchid) {
		this.batchid = batchid;
	}

	public String getLotno() {
		return lotno;
	}

	public void setLotno(String lotno) {
		this.lotno = lotno;
	}

	public Boolean getIsconsignment() {
		return isconsignment;
	}

	public void setIsconsignment(Boolean isconsignment) {
		this.isconsignment = isconsignment;
	}

	public BigInteger getTotalstock() {
		return totalstock;
	}

	public void setTotalstock(BigInteger totalstock) {
		this.totalstock = totalstock;
	}

	public Boolean getQuarantine() {
		return quarantine;
	}

	public void setQuarantine(Boolean quarantine) {
		this.quarantine = quarantine;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getUomtype() {
		return uomtype;
	}

	public void setUomtype(String uomtype) {
		this.uomtype = uomtype;
	}

	public BigInteger getAvailablestockquantity() {
		return availablestockquantity;
	}

	public void setAvailablestockquantity(BigInteger availablestockquantity) {
		this.availablestockquantity = availablestockquantity;
	}

	public String getUomunit() {
		return uomunit;
	}

	public void setUomunit(String uomunit) {
		this.uomunit = uomunit;
	}

	protected String uomunit;
}
