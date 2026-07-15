package com.param.global.dto;

import java.util.Date;

public class AllDataResponse {
	private String storename;
	private String batchno;
	private Integer currentstock;
	private Double mrp;
	private String location;
	private String  shelf;
	private String  uom;
	private String  uomunit;
	private Double cop;
	
	public Double getCop() {
		return cop;
	}
	public void setCop(Double cop) {
		this.cop = cop;
	}
	public String getShelf() {
		return shelf;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getUomunit() {
		return uomunit;
	}
	public void setUomunit(String uomunit) {
		this.uomunit = uomunit;
	}
	public void setShelf(String shelf) {
		this.shelf = shelf;
	}
	public Integer getCurrentstock() {
		return currentstock;
	}
	public void setCurrentstock(Integer currentstock) {
		this.currentstock = currentstock;
	}
	public Double getMrp() {
		return mrp;
	}
	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	private Date expdate;
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	public Date getExpdate() {
		return expdate;
	}
	public void setExpdate(Date expdate) {
		this.expdate = expdate;
	}
	
	
}
