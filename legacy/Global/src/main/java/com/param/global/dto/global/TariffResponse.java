package com.param.global.dto.global;

public class TariffResponse {

	private Integer itemId;
	
	private Integer prodCatId;
	
	protected Integer markUpDown;
	
	protected Double markUpDownPer;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	
}
