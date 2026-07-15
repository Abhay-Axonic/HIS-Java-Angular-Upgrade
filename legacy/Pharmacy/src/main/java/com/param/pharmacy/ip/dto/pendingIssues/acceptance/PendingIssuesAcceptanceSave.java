package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class PendingIssuesAcceptanceSave extends CookieObjectDto
{
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	private Integer id;
	private Integer  fromStoreId;
	
	private Integer issueId;
	
	private Integer wardId;
	
	private Integer statusId;
	private Integer storeId;
	
	private List<PendingIssuesAcceptanceSaveItems> items;

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<PendingIssuesAcceptanceSaveItems> getItems() {
		return items;
	}

	public void setItems(List<PendingIssuesAcceptanceSaveItems> items) {
		this.items = items;
	}
	
}