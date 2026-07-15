package com.param.pharmacy.master.unit.dto.storeItemMapper;

import java.math.BigInteger;

import com.param.service.global.model.CookieObjectDto;

public class GetStoreItemResponse  extends CookieObjectDto{

	protected BigInteger id;
	
	protected Integer itemId;
	
	protected Integer storeId;
	
	protected Integer minQty;
	
	protected Integer maxQty;
	
	protected Boolean status;
	
	protected Boolean isSellable;
	protected Boolean isActive;


	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Boolean getIsSellable() {
		return isSellable;
	}

	public void setIsSellable(Boolean isSellable) {
		this.isSellable = isSellable;
	}
	
	
}
