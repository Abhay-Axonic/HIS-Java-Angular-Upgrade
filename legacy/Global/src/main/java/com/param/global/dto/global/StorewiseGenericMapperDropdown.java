package com.param.global.dto.global;

import java.util.ArrayList;
import java.util.List;
import com.param.global.dto.global.AssetCategory;
import com.param.global.dto.global.Store;
import com.param.global.dto.global.VedResponseList;

public class StorewiseGenericMapperDropdown {
	protected List<Store> store;
	protected List<AssetCategory> assetCategories;
	protected List<VedResponseList> vedResponseLists;

	public List<Store> getStore() {
		return store;
	}

	public void setStore(List<Store> store) {
		this.store = store;
	}

	public List<AssetCategory> getAssetCategories() {
		return assetCategories;
	}

	public void setAssetCategories(List<AssetCategory> assetCategories) {
		this.assetCategories = assetCategories;
	}

	public List<VedResponseList> getVedResponseLists() {
		return vedResponseLists;
	}

	public void setVedResponseLists(List<VedResponseList> vedResponseLists) {
		this.vedResponseLists = vedResponseLists;
	}

	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();

		this.store.add(store);
	}

	public void addAssetCategory(AssetCategory assetCategory) {
		if (this.assetCategories == null)
			this.assetCategories = new ArrayList<AssetCategory>();

		this.assetCategories.add(assetCategory);
	}

	public void addVedResponseList(VedResponseList vedResponseLists) {
		if (this.vedResponseLists == null)
			this.vedResponseLists = new ArrayList<VedResponseList>();

		this.vedResponseLists.add(vedResponseLists);
	}
}
