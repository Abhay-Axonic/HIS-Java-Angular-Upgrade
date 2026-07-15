package com.param.inventory.scrap.dto.scrapRequest;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ScrapRequest extends CookieObjectDto{

	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;

	protected Integer assetTypeId;

	protected Boolean nearByExpiry;

	protected Integer scrapType;

	protected String remark;

	protected Integer statusId;

	protected Integer approvalStatusId;

	protected List<ScrapDetailRequest> items;

	protected List<Integer> scrapId;
	
	protected Integer cancelReasonId;

	protected String cancelNote;

	protected List<com.param.inventory.scrap.dto.scrapRequestApproval.ItemRequest> item;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Boolean getNearByExpiry() {
		return nearByExpiry;
	}

	public void setNearByExpiry(Boolean nearByExpiry) {
		this.nearByExpiry = nearByExpiry;
	}

	public Integer getScrapType() {
		return scrapType;
	}

	public void setScrapType(Integer scrapType) {
		this.scrapType = scrapType;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ScrapDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<ScrapDetailRequest> items) {
		this.items = items;
	}

	public List<Integer> getScrapId() {
		return scrapId;
	}

	public void setScrapId(List<Integer> scrapId) {
		this.scrapId = scrapId;
	}

	public List<com.param.inventory.scrap.dto.scrapRequestApproval.ItemRequest> getItem() {
		return item;
	}

	public void setItem(List<com.param.inventory.scrap.dto.scrapRequestApproval.ItemRequest> item) {
		this.item = item;
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
