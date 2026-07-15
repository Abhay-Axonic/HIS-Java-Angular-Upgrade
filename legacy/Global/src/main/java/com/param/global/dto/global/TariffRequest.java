package com.param.global.dto.global;

import java.util.List;

public class TariffRequest {

	private List<TariffQueryData> items;
	
	private Integer entitlementId;

	
	public List<TariffQueryData> getItems() {
		return items;
	}

	public void setItems(List<TariffQueryData> items) {
		this.items = items;
	}

	public Integer getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(Integer entitlementId) {
		this.entitlementId = entitlementId;
	}
	
	
}
