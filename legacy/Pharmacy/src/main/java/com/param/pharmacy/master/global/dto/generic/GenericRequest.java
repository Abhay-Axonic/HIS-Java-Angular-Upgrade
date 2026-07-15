package com.param.pharmacy.master.global.dto.generic;

import java.util.List;
import com.param.pharmacy.master.global.dto.generic.GenericRequestItem;


public class GenericRequest extends GenericResponse {

	protected Integer productCategoryId;

	protected Boolean isCombination;

	private List<GenericRequestItem> items;

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public List<GenericRequestItem> getItems() {
		return items;
	}

	public void setItems(List<GenericRequestItem> items) {
		this.items = items;
	}

	public Boolean getIsCombination() {
		return isCombination;
	}

	public void setIsCombination(Boolean isCombination) {
		this.isCombination = isCombination;
	}

}
