package com.param.inventory.store.dto.storeIndent;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.IndentType;
import com.param.global.dto.global.Priority;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;

@JsonInclude(Include.NON_NULL)
public class StoreIndentDropdownResponse {
	
	protected List<Store> fromStore;
	
	protected List<Store> toStore;
	
	protected List<Status> status;
	
	protected List<IndentType> indentType;
	
	protected List<Priority> priority;

	public StoreIndentDropdownResponse() {
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

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

	public List<IndentType> getIndentType() {
		return indentType;
	}

	public void setIndentType(List<IndentType> indentType) {
		this.indentType = indentType;
	}

	public List<Priority> getPriority() {
		return priority;
	}

	public void setPriority(List<Priority> priority) {
		this.priority = priority;
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
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();

		this.status.add(status);
	}
	
	public void addIndentType(IndentType indentType) {
		if (this.indentType == null)
			this.indentType = new ArrayList<IndentType>();

		this.indentType.add(indentType);
	}
	
	public void addPriority(Priority priority) {
		if (this.priority == null)
			this.priority = new ArrayList<Priority>();

		this.priority.add(priority);
	}
}