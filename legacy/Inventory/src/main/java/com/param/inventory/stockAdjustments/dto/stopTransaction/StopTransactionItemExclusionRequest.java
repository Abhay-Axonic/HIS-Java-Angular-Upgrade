package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.List;

public class StopTransactionItemExclusionRequest {
	
	protected List<Integer> itemId;
	
	

	public StopTransactionItemExclusionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Integer> getItemId() {
		return itemId;
	}

	public void setItemId(List<Integer> itemId) {
		this.itemId = itemId;
	}

	

}
