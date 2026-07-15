package com.param.procurement.quotation.dto.quotationFill;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

public class QuotationFillDropDownDto {
	
	List<Store> store;
	List<Status> status;
	List<AssetType> assetType;
	
	
	public List<Store> getStore() {
		return store;
	}
	public void setStore(List<Store> store) {
		this.store = store;
	}
	public List<Status> getStatus() {
		return status;
	}
	public void setStatus(List<Status> status) {
		this.status = status;
	}
	public List<AssetType> getAssetType() {
		return assetType;
	}
	public void setAssetType(List<AssetType> assetType) {
		this.assetType = assetType;
	}
	
	
	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();

		this.store.add(store);
	}
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();

		this.status.add(status);
	}
	
	public void addAssetType(AssetType assetType) {
		if (this.assetType == null)
			this.assetType = new ArrayList<AssetType>();

		this.assetType.add(assetType);
	}


}
