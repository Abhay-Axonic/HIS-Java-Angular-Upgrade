package com.param.pharmacy.master.global.dto.unitOfMeasurement;

import java.util.List;

public class UnitOfMeasurementRequest extends UnitOfMeasurementResponse {

	protected Integer itemId;

	protected List<UOMResponse> uomList;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public List<UOMResponse> getUomList() {
		return uomList;
	}

	public void setUomList(List<UOMResponse> uomList) {
		this.uomList = uomList;
	}

}
