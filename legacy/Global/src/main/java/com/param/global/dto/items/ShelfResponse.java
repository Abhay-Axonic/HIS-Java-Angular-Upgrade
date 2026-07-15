package com.param.global.dto.items;

import java.util.List;

public class ShelfResponse
{
	private Integer id;
	
	private String shelf;
	
	private List<OpeningBalanceItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public List<OpeningBalanceItem> getItems() {
		return items;
	}

	public void setItems(List<OpeningBalanceItem> items) {
		this.items = items;
	}

	public ShelfResponse(Integer id, String shelf, List<OpeningBalanceItem> items) {
		super();
		this.id = id;
		this.shelf = shelf;
		this.items = items;
	}
}