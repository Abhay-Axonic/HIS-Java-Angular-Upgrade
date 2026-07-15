package com.param.global.dto.kit.Items;

import java.util.List;

import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;

public class KitItemsDetailsListResponse extends KitItemsDetail {
	
	protected Long stockAvailable;

	protected Long stockInTransit;

	protected Long lastMonthSale;

	protected Long currentMonthSale;
	
	protected Long maxOrderQuantity;

	protected Integer maxStoreQuantity;

	//protected Long Quantity;

	protected List<UomResponse> uom;

	protected List<BatchResponse> batch;
	
	public KitItemsDetailsListResponse()
	{
		super();
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getStockInTransit() {
		return stockInTransit;
	}

	public void setStockInTransit(Long stockInTransit) {
		this.stockInTransit = stockInTransit;
	}

	public Long getLastMonthSale() {
		return lastMonthSale;
	}

	public void setLastMonthSale(Long lastMonthSale) {
		this.lastMonthSale = lastMonthSale;
	}

	public Long getCurrentMonthSale() {
		return currentMonthSale;
	}

	public void setCurrentMonthSale(Long currentMonthSale) {
		this.currentMonthSale = currentMonthSale;
	}

/*	public Long getQuantity() {
		return Quantity;
	}

	public void setQuantity(Long Quantity) {
		this.Quantity = Quantity;
	}*/

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Integer getMaxStoreQuantity() {
		return maxStoreQuantity;
	}

	public void setMaxStoreQuantity(Integer maxStoreQuantity) {
		this.maxStoreQuantity = maxStoreQuantity;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}


}
