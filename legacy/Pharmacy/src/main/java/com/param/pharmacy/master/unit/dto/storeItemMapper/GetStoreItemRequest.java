package com.param.pharmacy.master.unit.dto.storeItemMapper;

import java.util.List;

import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;

public class GetStoreItemRequest extends GetStoreItemResponse {

	protected Integer storeId;
	//protected List<MultipleStoreRequest>  stores;
	protected List<GetStoreItemResponse> items;

	protected List<Integer> storeIds;

	public List<Integer> getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(List<Integer> storeIds) {
		this.storeIds = storeIds;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public List<GetStoreItemResponse> getItems() {
		return items;
	}

	/*public List<MultipleStoreRequest> getStores() {
		return stores;
	}

	public void setStores(List<MultipleStoreRequest> stores) {
		this.stores = stores;
	}*/

	public void setItems(List<GetStoreItemResponse> items) {
		this.items = items;
	}
	
}
