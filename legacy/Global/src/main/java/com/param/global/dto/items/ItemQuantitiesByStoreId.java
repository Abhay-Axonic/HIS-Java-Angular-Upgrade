package com.param.global.dto.items;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemQuantitiesByStoreId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long transitQty;
	
	private Long availableQty;

	private Long lastMonyhSale;
	
	private Long currMonthSale;
	
	private Integer maxOrderQty;
	
	private Long itemId;

	public Long getTransitQty() {
		return transitQty;
	}

	public void setTransitQty(Long transitQty) {
		this.transitQty = transitQty;
	}

	public Long getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Long availableQty) {
		this.availableQty = availableQty;
	}

	public Long getLastMonyhSale() {
		return lastMonyhSale;
	}

	public void setLastMonyhSale(Long lastMonyhSale) {
		this.lastMonyhSale = lastMonyhSale;
	}

	public Long getCurrMonthSale() {
		return currMonthSale;
	}

	public void setCurrMonthSale(Long currMonthSale) {
		this.currMonthSale = currMonthSale;
	}

	public Integer getMaxOrderQty() {
		return maxOrderQty;
	}

	public void setMaxOrderQty(Integer maxOrderQty) {
		this.maxOrderQty = maxOrderQty;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public ItemQuantitiesByStoreId() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
