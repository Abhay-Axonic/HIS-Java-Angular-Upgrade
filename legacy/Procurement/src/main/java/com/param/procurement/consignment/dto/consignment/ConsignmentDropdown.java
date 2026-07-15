package com.param.procurement.consignment.dto.consignment;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

public class ConsignmentDropdown {

	private List<Status> status;

	private List<Store> store;

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
}
