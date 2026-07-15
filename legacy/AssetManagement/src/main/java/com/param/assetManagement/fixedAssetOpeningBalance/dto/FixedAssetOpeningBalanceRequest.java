package com.param.assetManagement.fixedAssetOpeningBalance.dto;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class FixedAssetOpeningBalanceRequest extends CookieObjectDto{

	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;

	protected Integer statusId;

	protected List<ItemRequest> items;

	protected List<Integer> openingBalanceId;
	
	protected List<Integer> openingBalanceDetailId;

	protected Integer cancelReasonId;

	protected String cancelNote;
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<Integer> getOpeningBalanceId() {
		return openingBalanceId;
	}

	public void setOpeningBalanceId(List<Integer> openingBalanceId) {
		this.openingBalanceId = openingBalanceId;
	}

	public List<Integer> getOpeningBalanceDetailId() {
		return openingBalanceDetailId;
	}

	public void setOpeningBalanceDetailId(List<Integer> openingBalanceDetailId) {
		this.openingBalanceDetailId = openingBalanceDetailId;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

}
