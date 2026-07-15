package com.param.procurement.consignment.dto.implantReturnAcceptance;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ImplantReturnAcceptanceSave extends CookieObjectDto
{
	
	private Integer id;
	
	private Integer statusId;
	
	private List<ImplantReturnAcceptanceSaveItem> items;

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

	public List<ImplantReturnAcceptanceSaveItem> getItems() {
		return items;
	}

	public void setItems(List<ImplantReturnAcceptanceSaveItem> items) {
		this.items = items;
	}

}