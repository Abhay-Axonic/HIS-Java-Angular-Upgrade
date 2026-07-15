package com.param.pharmacy.nursing.pharmacy.dto.billableConsumption;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ConsumptionIds extends CookieObjectDto{
	private List<Integer> ids;

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	
	
}
