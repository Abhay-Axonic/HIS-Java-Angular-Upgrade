package com.param.inventory.store.dto.materialIssueList;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.Store;
import com.param.global.dto.global.User;

public class MaterialIssueListDropdownResponse {

	protected List<Store> fromStore;

	protected List<Store> toStore;
	
	protected List<User> carrier;

	public MaterialIssueListDropdownResponse() {
		super();
	}

	public List<Store> getFromStore() {
		return fromStore;
	}

	public void setFromStore(List<Store> fromStore) {
		this.fromStore = fromStore;
	}

	public List<Store> getToStore() {
		return toStore;
	}

	public void setToStore(List<Store> toStore) {
		this.toStore = toStore;
	}

	public List<User> getCarrier() {
		return carrier;
	}

	public void setCarrier(List<User> carrier) {
		this.carrier = carrier;
	}
	
	public void addFromStore(Store fromStore) {
		if (this.fromStore == null)
			this.fromStore = new ArrayList<Store>();

		this.fromStore.add(fromStore);
	}
	
	public void addToStore(Store toStore) {
		if (this.toStore == null)
			this.toStore = new ArrayList<Store>();

		this.toStore.add(toStore);
	}

	public void addToCarrier(User user) {
		if (this.carrier == null)
			this.carrier = new ArrayList<User>();

		this.carrier.add(user);
	}

}
