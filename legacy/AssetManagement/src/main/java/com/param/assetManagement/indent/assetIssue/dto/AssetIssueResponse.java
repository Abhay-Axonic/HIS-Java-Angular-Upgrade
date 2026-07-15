package com.param.assetManagement.indent.assetIssue.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentResponse;
import com.param.assetManagement.indent.assetIssueList.dto.AssetIssueItemResponse;
import com.param.global.dto.global.PickerResponse;

@JsonInclude(Include.NON_NULL)
public class AssetIssueResponse extends AssetIndentResponse {

	protected Date issueDate;

	protected String issueNo;

	protected Integer carrierId;

	protected String carrierName;

	protected List<AssetIssueDetailResponse> item;

	protected List<PickerResponse> carriers;

	protected List<AssetIssueItemResponse> itemResponse;
	protected Integer statusId;
	
	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public AssetIssueResponse() {
		super();
	}

	public AssetIssueResponse(Integer id, Integer fromStoreId, String fromStore, Integer toStoreId, String toStore,
			String remark, String status, Integer statusId, Date issueDate, String issueNo) {
		super(id, fromStoreId, fromStore, toStoreId, toStore, remark, status, statusId);
		this.issueDate = issueDate;
		this.issueNo = issueNo;
	}

	public AssetIssueResponse(Integer id, String indentNo, Integer fromStoreId, String fromStore, Integer toStoreId,
			String toStore, String priority, String type, String remark, String status, Integer statusId, Date issueDate,
			String issueNo) {
		super(id, indentNo, fromStoreId, fromStore, toStoreId, toStore, priority, type, remark, status, statusId);
		this.issueDate = issueDate;
		this.issueNo = issueNo;
	}

	public AssetIssueResponse(Integer id, String indentNo, Integer fromStoreId, String fromStore, Integer toStoreId,
			String toStore, String priority, String type, String remark, Date issueDate, String issueNo,
			Integer carrierId, String carrierName) {
		super(id, indentNo, fromStoreId, fromStore, toStoreId, toStore, priority, type, remark);
		this.issueDate = issueDate;
		this.issueNo = issueNo;
		this.carrierId = carrierId;
		this.carrierName = carrierName;
	}

	
	public AssetIssueResponse(Integer id, Integer fromStoreId, String fromStore, Integer toStoreId, String toStore,
			String priority, String type, String remark, Date issueDate, String issueNo) {
		super(id, fromStoreId, fromStore, toStoreId, toStore, priority, type, remark);
		this.issueDate = issueDate;
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public List<AssetIssueDetailResponse> getItem() {
		return item;
	}

	public void setItem(List<AssetIssueDetailResponse> item) {
		this.item = item;
	}

	public List<PickerResponse> getCarriers() {
		return carriers;
	}

	public void setCarriers(List<PickerResponse> carriers) {
		this.carriers = carriers;
	}

	public List<AssetIssueItemResponse> getItemResponse() {
		return itemResponse;
	}

	public void setItemResponse(List<AssetIssueItemResponse> itemResponse) {
		this.itemResponse = itemResponse;
	}

	public void addItems(AssetIssueDetailResponse item) {
		if (this.item == null)
			this.item = new ArrayList<AssetIssueDetailResponse>();

		this.item.add(item);
	}

	public void addItems(AssetIssueItemResponse itemResponse) {
		if (this.itemResponse == null)
			this.itemResponse = new ArrayList<AssetIssueItemResponse>();

		this.itemResponse.add(itemResponse);
	}

}
