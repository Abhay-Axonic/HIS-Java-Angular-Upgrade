package com.param.procurement.purchase.dto.purchaseOrder;

import java.util.List;

public class PoAccessoriesMapper {

	private Integer id;

	private Integer itemId;

	private String itemName;

	private String itemCode;
	
	private List<Integer> itemIds;
	
	private Integer poDetailId;

	public PoAccessoriesMapper() {
		super();
	}
	
	public PoAccessoriesMapper(Integer id, Integer itemId, String itemName, String itemCode) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}
	
}
