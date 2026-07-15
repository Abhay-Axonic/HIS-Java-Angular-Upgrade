package com.param.inventory.stockAdjustments.dto.stopTransaction;

import java.util.List;

public class StopTransactionProductExclusionRequest {

	protected List<Integer> productCategoryId;
	
	public StopTransactionProductExclusionRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Integer> getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(List<Integer> productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

}
