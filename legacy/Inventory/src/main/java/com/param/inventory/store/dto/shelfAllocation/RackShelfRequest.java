package com.param.inventory.store.dto.shelfAllocation;

public class RackShelfRequest {
	protected Integer id;
	
	protected Integer rackId;

	protected Integer shelfId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRackId() {
		return rackId;
	}

	public void setRackId(Integer rackId) {
		this.rackId = rackId;
	}

	public Integer getShelfId() {
		return shelfId;
	}

	public void setShelfId(Integer shelfId) {
		this.shelfId = shelfId;
	}
}