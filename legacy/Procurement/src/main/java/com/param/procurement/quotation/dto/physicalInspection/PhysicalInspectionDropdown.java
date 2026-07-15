package com.param.procurement.quotation.dto.physicalInspection;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

public class PhysicalInspectionDropdown
{
	private List<Status> status;
	
	private List<Store> store;
	
	private List<AssetType> assetType;

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

	public List<Store> getStore() {
		return store;
	}

	public void setStore(List<Store> store) {
		this.store = store;
	}

	public List<AssetType> getAssetType() {
		return assetType;
	}

	public void setAssetType(List<AssetType> assetType) {
		this.assetType = assetType;
	}

	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();
		this.status.add(status);
	}
	
	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();
		this.store.add(store);
	}
	
	public void addAssetType(AssetType assetType) {
		if (this.assetType == null)
			this.assetType = new ArrayList<AssetType>();
		this.assetType.add(assetType);
	}
	
}

	