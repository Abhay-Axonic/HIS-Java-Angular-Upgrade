package com.param.assetManagement.indent.assetIssue.dto;

import java.util.List;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIssueAcceptance.dto.ItemRequest;

public class AssetIssueRequest extends AssetIndentRequest {

	protected List<AssetIssueDetailRequest> item;

	protected List<ItemRequest> itemRequest;

	protected Integer assetIssueId;

	protected Integer assetReturnId;

	public List<AssetIssueDetailRequest> getItem() {
		return item;
	}

	public void setItem(List<AssetIssueDetailRequest> item) {
		this.item = item;
	}

	public List<ItemRequest> getItemRequest() {
		return itemRequest;
	}

	public void setItemRequest(List<ItemRequest> itemRequest) {
		this.itemRequest = itemRequest;
	}

	public Integer getAssetIssueId() {
		return assetIssueId;
	}

	public void setAssetIssueId(Integer assetIssueId) {
		this.assetIssueId = assetIssueId;
	}

	public Integer getAssetReturnId() {
		return assetReturnId;
	}

	public void setAssetReturnId(Integer assetReturnId) {
		this.assetReturnId = assetReturnId;
	}

}
