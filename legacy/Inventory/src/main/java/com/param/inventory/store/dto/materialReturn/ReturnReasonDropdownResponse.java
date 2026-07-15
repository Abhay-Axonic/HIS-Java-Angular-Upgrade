package com.param.inventory.store.dto.materialReturn;

import java.util.ArrayList;
import java.util.List;

public class ReturnReasonDropdownResponse {

	protected List<ReturnReason> returnReason;

	public ReturnReasonDropdownResponse() {
		super();
	}

	public List<ReturnReason> getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(List<ReturnReason> returnReason) {
		this.returnReason = returnReason;
	}

	public void addReason(ReturnReason returnReason) {
		if (this.returnReason == null)
			this.returnReason = new ArrayList<ReturnReason>();

		this.returnReason.add(returnReason);
		
	}
	
	
}
