package com.param.pharmacy.master.unit.dto.rack;

public class RackRequest extends RackResponse {

	public RackRequest() {
		super();
	}

	public RackRequest(Integer id, Integer storeId, String storeName, String rackCode, String rackName,
			Boolean isActive) {
		super(id, storeId, storeName, rackCode, rackName, isActive);
	}

}
