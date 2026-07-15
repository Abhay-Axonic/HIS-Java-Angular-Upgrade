package com.param.procurement.quotation.dto.physicalInspection;

import java.util.List;

public class ShelfResponse
{
	private Integer id;
	
	private String shelf;
	
	private List<PhyscicalInspectionItem> items;

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

	public List<PhyscicalInspectionItem> getItems() {
		return items;
	}

	public void setItems(List<PhyscicalInspectionItem> items) {
		this.items = items;
	}

	public ShelfResponse(Integer id, String shelf, List<PhyscicalInspectionItem> items) {
		super();
		this.id = id;
		this.shelf = shelf;
		this.items = items;
	}
	
}