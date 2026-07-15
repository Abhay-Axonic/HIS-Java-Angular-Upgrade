package com.param.inventory.stock.dto.openingBalance;

public class StorageLocationDetails {
    private Integer shelfitemid;
	
	private Integer openingbaldetid;
	private Integer  shelfid;
	private String shelfname;
	private Integer  rackid;
	private String rackname;
	private String remark;
	public Integer getShelfitemid() {
		return shelfitemid;
	}
	public void setShelfitemid(Integer shelfitemid) {
		this.shelfitemid = shelfitemid;
	}
	public Integer getOpeningbaldetid() {
		return openingbaldetid;
	}
	public void setOpeningbaldetid(Integer openingbaldetid) {
		this.openingbaldetid = openingbaldetid;
	}
	public Integer getShelfid() {
		return shelfid;
	}
	public void setShelfid(Integer shelfid) {
		this.shelfid = shelfid;
	}
	public String getShelfname() {
		return shelfname;
	}
	public void setShelfname(String shelfname) {
		this.shelfname = shelfname;
	}
	public Integer getRackid() {
		return rackid;
	}
	public void setRackid(Integer rackid) {
		this.rackid = rackid;
	}
	public String getRackname() {
		return rackname;
	}
	public void setRackname(String rackname) {
		this.rackname = rackname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
