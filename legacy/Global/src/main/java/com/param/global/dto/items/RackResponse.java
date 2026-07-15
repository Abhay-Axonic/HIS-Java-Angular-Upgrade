package com.param.global.dto.items;

import java.util.List;

public class RackResponse {
	 private Integer rackId; 
	  
	  private String rack;
	  
	  private List<ShelfResponse> shelfs;

	public Integer getRackId() {
		return rackId;
	}

	public void setRackId(Integer rackId) {
		this.rackId = rackId;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public List<ShelfResponse> getShelfs() {
		return shelfs;
	}

	public void setShelfs(List<ShelfResponse> shelfs) {
		this.shelfs = shelfs;
	}

	public RackResponse(Integer rackId, String rack, List<ShelfResponse> shelfs) {
		super();
		this.rackId = rackId;
		this.rack = rack;
		this.shelfs = shelfs;
	}

}
