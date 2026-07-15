package com.param.assetManagement.itemForAuction.dto;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ItemForAuctionRequest extends CookieObjectDto{

	protected Integer id;
	
	protected Integer statusId;
	
	protected List<Integer> ids;

	protected List<ItemForAuctionDetailRequest> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<ItemForAuctionDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemForAuctionDetailRequest> items) {
		this.items = items;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	
}