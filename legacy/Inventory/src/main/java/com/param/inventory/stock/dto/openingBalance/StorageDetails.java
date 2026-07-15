package com.param.inventory.stock.dto.openingBalance;

public class StorageDetails {


	private Integer id;
	

	private Integer rackId;
	
	private Integer shelfId;
	
	private String remark;
	private Integer shelfItemId;
	private String  shelfName;
	private String  rackName;
	
	private Integer openingBalDetId;
	
	public Integer getShelfItemId() {
		return shelfItemId;
	}

	public void setShelfItemId(Integer shelfItemId) {
		this.shelfItemId = shelfItemId;
	}

	public String getShelfName() {
		return shelfName;
	}

	public void setShelfName(String shelfName) {
		this.shelfName = shelfName;
	}

	public String getRackName() {
		return rackName;
	}

	public void setRackName(String rackName) {
		this.rackName = rackName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRackId() {
		return rackId;
	}

	public void setRackId(Integer rackId) {
		this.rackId = rackId;
	}

	public Integer getShelfId() {
		return shelfId;
	}

	public void setShelfId(Integer shelfId) {
		this.shelfId = shelfId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOpeningBalDetId() {
		return openingBalDetId;
	}

	public void setOpeningBalDetId(Integer openingBalDetId) {
		this.openingBalDetId = openingBalDetId;
	}
}
