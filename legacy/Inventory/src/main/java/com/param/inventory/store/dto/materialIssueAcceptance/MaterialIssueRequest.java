package com.param.inventory.store.dto.materialIssueAcceptance;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class MaterialIssueRequest extends CookieObjectDto {

	protected Integer id;

	protected Integer statusId;

	protected Integer approvalStatusId;

	protected List<ItemRequest> items;

	protected Integer issueId;

	protected Integer returnId;
	
	protected Integer storeId;
	
	protected Integer newStatusId;
	
	

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getReturnId() {
		return returnId;
	}

	public void setReturnId(Integer returnId) {
		this.returnId = returnId;
	}

	public Integer getNewStatusId() {
		return newStatusId;
	}

	public void setNewStatusId(Integer newStatusId) {
		this.newStatusId = newStatusId;
	}

}
