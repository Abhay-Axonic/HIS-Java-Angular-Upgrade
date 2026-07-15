package com.param.procurement.consignment.dto.implantIndent;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.IndentType;
import com.param.global.dto.global.Priority;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;
import com.param.global.dto.global.VisitType;

public class ImplantIndentDropdown {

	protected List<Store> toStore;
	
	protected List<Store> store;

	protected List<Status> status;

	protected List<IndentType> indentType;

	protected List<Priority> priority;
	
	protected List<VisitType> visitType;

	public ImplantIndentDropdown() {
		super();
	}

	
	public List<Store> getStore() {
		return store;
	}


	public void setStore(List<Store> store) {
		this.store = store;
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
	
	public void addVisitType(VisitType visitType) {
		if (this.visitType == null)
			this.visitType = new ArrayList<VisitType>();

		this.visitType.add(visitType);
	}

	public List<VisitType> getVisitType() {
		return visitType;
	}

	public void setVisitType(List<VisitType> visitType) {
		this.visitType = visitType;
	}
	
	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();

		this.store.add(store);
	}

}
