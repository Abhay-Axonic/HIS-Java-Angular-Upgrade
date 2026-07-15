package com.param.global.dto;

public class StoreStockRequestDto {
	
	
	Integer storeeId;
	Long batchId;
	Integer uomTypeId;
	Integer consumedQuantity;
	Integer screenId;
	Integer unitId;
	Integer userId;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getScreenId() {
		return screenId;
	}
	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}
	public Integer getStoreeId() {
		return storeeId;
	}
	public void setStoreeId(Integer storeeId) {
		this.storeeId = storeeId;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public Integer getUomTypeId() {
		return uomTypeId;
	}
	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}
	public Integer getConsumedQuantity() {
		return consumedQuantity;
	}
	public void setConsumedQuantity(Integer consumedQuantity) {
		this.consumedQuantity = consumedQuantity;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	
	
	

}
