package com.param.pharmacy.master.global.dto.item;

import java.util.List;

import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperListResponse;

public class ItemDetailsResponse extends ItemResponse {
	List<StoreItemMapperListResponse> storeItemMapperListResponse;

	public List<StoreItemMapperListResponse> getStoreItemMapperListResponse() {
		return storeItemMapperListResponse;
	}

	public void setStoreItemMapperListResponse(List<StoreItemMapperListResponse> storeItemMapperListResponse) {
		this.storeItemMapperListResponse = storeItemMapperListResponse;
	}
}
