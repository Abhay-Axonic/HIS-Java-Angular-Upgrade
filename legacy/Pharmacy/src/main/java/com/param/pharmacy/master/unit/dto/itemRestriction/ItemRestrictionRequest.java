package com.param.pharmacy.master.unit.dto.itemRestriction;

import java.util.List;

public class ItemRestrictionRequest extends ItemRestrictionResponse {

	protected Integer storeId;

	protected Integer itemId;

	protected List<Integer> itemsList;

	protected Boolean status;

	public ItemRestrictionRequest() {
		super();
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public List<Integer> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<Integer> itemsList) {
		this.itemsList = itemsList;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
