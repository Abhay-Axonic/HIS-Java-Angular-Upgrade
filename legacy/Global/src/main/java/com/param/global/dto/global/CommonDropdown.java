package com.param.global.dto.global;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CommonDropdown
{
	private List<VisitType> visitType;
	
	private List<Ward> ward;
	
	private List<Store> store;
	
	private List<Store> fromStore;
	
	private List<Status> status;

	private List<Floor> floor;

	private List<Store> toStore;
	
	public List<VisitType> getVisitType() {
		return visitType;
	}

	public void setVisitType(List<VisitType> visitType) {
		this.visitType = visitType;
	}
	
	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

	public List<Ward> getWard() {
		return ward;
	}

	public void setWard(List<Ward> ward) {
		this.ward = ward;
	}

	public List<Store> getStore() {
		return store;
	}

	public void setStore(List<Store> store) {
		this.store = store;
	}

	public List<Floor> getFloor() {
		return floor;
	}

	public void setFloor(List<Floor> floor) {
		this.floor = floor;
	}

	public List<Store> getFromStore() {
		return fromStore;
	}

	public List<Store> getToStore() {
		return toStore;
	}
	public void setFromStore(List<Store> fromStore) {
		this.fromStore = fromStore;
	}

	public void addVisitType(VisitType visitType) {
		if (this.visitType == null)
			this.visitType = new ArrayList<VisitType>();
		this.visitType.add(visitType);
	}
	
	public void addStatus(Status status) {
		if (this.status == null)
			this.status = new ArrayList<Status>();
		this.status.add(status);
	}
	
	public void addWard(Ward ward) {
		if (this.ward == null)
			this.ward = new ArrayList<Ward>();
		this.ward.add(ward);
	}
	
	public void addStore(Store store) {
		if (this.store == null)
			this.store = new ArrayList<Store>();
		this.store.add(store);
	}
	
	public void addFromStore(Store fromStore) {
		if (this.fromStore == null)
			this.fromStore = new ArrayList<Store>();
		this.fromStore.add(fromStore);
	}
	
	public void addFloor(Floor floor) {
		if (this.floor == null)
			this.floor = new ArrayList<Floor>();
		this.floor.add(floor);
	}

	public void addToStore(Store toStore) {
		if (this.toStore == null)
			this.toStore = new ArrayList<Store>();
		this.toStore.add(toStore);
	}
	
}