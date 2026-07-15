package com.param.procurement.purchase.dto.itemRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseReqDetailsResponseDto;

public class ItemRequestResponse {

	private Integer id;

	private Integer approvalStatusId;
	
	private String approvalStatus;

	private Integer assetTypeId;
	
	private String assetType;

	private String requestNumber;
	
	private Date requestDate;

	private Integer statusId;
	
	private String status;
	
	private List<ItemRequestDetailResponse> items;
	
	
	public ItemRequestResponse(Integer id, Integer approvalStatusId, String approvalStatus, Integer assetTypeId,
			String assetType, String requestNumber,Date requestDate, Integer statusId, String status) {
		super();
		this.id = id;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.requestNumber = requestNumber;
		this.requestDate = requestDate;
		this.statusId = statusId;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ItemRequestDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemRequestDetailResponse> items) {
		this.items = items;
	}
	
	public void addItems(ItemRequestDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ItemRequestDetailResponse>();
		
		this.items.add(item);
	}
	
	

}
