package com.param.global.procedure.VO;

import java.math.BigInteger;
import java.util.List;

public class AssetVO {

	private Integer assetTypeId;

	private Integer assetCategoryId;

	private String itemName;

	private Integer itemId;
	
	private BigInteger batchId;
	
	private List<AssetVO> assetList;
	
	private String equipmentName;
	
	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}

	public List<AssetVO> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<AssetVO> assetList) {
		this.assetList = assetList;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	
	
	
}
