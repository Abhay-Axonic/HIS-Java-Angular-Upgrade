package com.param.inventory.stockAdjustments.dto.confirmStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfirmStockDto {
	
	private Integer id;
	
	private Integer storeId;
	
	private String storeName;
	
	private Date startDate;
	
	private Date stopDate;
	
	private String computedBy;
	
	private List<ConfirmStockDetailDto> items;

	public ConfirmStockDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConfirmStockDto(Integer id, Integer storeId, String storeName, Date startDate, Date stopDate,
			String computedBy) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.storeName = storeName;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.computedBy = computedBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public String getComputedBy() {
		return computedBy;
	}

	public void setComputedBy(String computedBy) {
		this.computedBy = computedBy;
	}

	public List<ConfirmStockDetailDto> getItems() {
		return items;
	}

	public void setItems(List<ConfirmStockDetailDto> items) {
		this.items = items;
	}
	
	public void addItems(ConfirmStockDetailDto item) {
		if (this.items == null)
			this.items = new ArrayList<ConfirmStockDetailDto>();
		
		this.items.add(item);
	}
	
}
