package com.param.pharmacy.master.unit.dto.itemRestriction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ItemRestrictionResponse extends CookieObjectDto {

	protected BigInteger id;

	protected Boolean status;

	protected Integer storeId;

	protected Integer itemId;

	protected List<ItemRestrictionResponse> items;

	public ItemRestrictionResponse() {
		super();
	}

	public ItemRestrictionResponse(BigInteger id, Boolean status, Integer storeId, Integer itemId) {
		super();
		this.id = id;
		this.status = status;
		this.storeId = storeId;
		this.itemId = itemId;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public List<ItemRestrictionResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemRestrictionResponse> items) {
		this.items = items;
	}

	public String getIsActive() {
		return null;
	}

	public void addItems(ItemRestrictionResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ItemRestrictionResponse>();

		this.items.add(item);
	}
}
