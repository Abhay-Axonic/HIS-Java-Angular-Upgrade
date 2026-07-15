package com.param.inventory.nursing.dto.nonBillableConsumption;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

@JsonInclude(Include.NON_NULL)
public class ConsumptionDropdownResponse {

	protected List<Store> store;
	
	protected List<Status> status;
	
	public ConsumptionDropdownResponse() {
		super();
	}

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
