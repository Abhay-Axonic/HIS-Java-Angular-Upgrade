package com.param.procurement.quotation.dto.vendorSelection;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

public class VendorSelectionDropdown {

	List<Store> store;
	List<Status> status;
	
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
}
