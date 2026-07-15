package com.param.assetManagement.itemForAuction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class ItemAuctionAccessories {
	
	private Integer id;
	
	private Integer assetId;
	
    private String assetCode;
    
    private String assetName;
    
    private String serialNo;
    
    private Integer manufacturingYear;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getManufacturingYear() {
		return manufacturingYear;
	}

	public void setManufacturingYear(Integer manufacturingYear) {
		this.manufacturingYear = manufacturingYear;
	}

	public ItemAuctionAccessories(Integer id, Integer assetId, String assetCode, String assetName, String serialNo,
			Integer manufacturingYear) {
		super();
		this.id = id;
		this.assetId = assetId;
		this.assetCode = assetCode;
		this.assetName = assetName;
		this.serialNo = serialNo;
		this.manufacturingYear = manufacturingYear;
	}

	public ItemAuctionAccessories() {
		super();
	}
    
}