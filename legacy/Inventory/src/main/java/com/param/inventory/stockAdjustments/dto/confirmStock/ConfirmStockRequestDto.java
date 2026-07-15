package com.param.inventory.stockAdjustments.dto.confirmStock;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ConfirmStockRequestDto extends CookieObjectDto {
	
	private Integer id;
	
	private Integer statusId;
	
	private Integer cancelReasonId;
	
	private String cancelNote;
	
	private List<Integer> confirmStockIds;
	
	private List<ConfirmStockDetailRequest> items;

	public ConfirmStockRequestDto() {
		super();
	}

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

	public List<ConfirmStockDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<ConfirmStockDetailRequest> items) {
		this.items = items;
	}

	public List<Integer> getConfirmStockIds() {
		return confirmStockIds;
	}

	public void setConfirmStockIds(List<Integer> confirmStockIds) {
		this.confirmStockIds = confirmStockIds;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}
	
}
