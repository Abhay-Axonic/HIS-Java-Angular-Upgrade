package com.param.procurement.purchase.dto.localPurchaseRequest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocPurchaseRequestMasterResponseDto {

	private Long id;

	private Date prDate;

	private String prNo;

	private Integer storeId;

	private String storeName;

	private Integer requestingStoreId;

	private String requestingStoreName;

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public String getRequestingStoreName() {
		return requestingStoreName;
	}

	public void setRequestingStoreName(String requestingStoreName) {
		this.requestingStoreName = requestingStoreName;
	}

	private Integer assetTypeId;

	private String assetType;

	private Integer prTypeId;

	private String prType;

	private Integer priorityId;

	private String priority;

	private Integer approvalStatusId;

	private String approvalStatus;

	private Integer statusId;

	private String status;

	private String remark;

	List<LocPurchaseReqDetailsResponseDto> items;

	public LocPurchaseRequestMasterResponseDto(Long id, Date prDate, String prNo, Integer storeId, String storeName,
			Integer assetTypeId, String assetType, Integer prTypeId, String prType, Integer priorityId, String priority,
			Integer approvalStatusId, String approvalStatus, Integer statusId, String status, String remark , Integer requestingStoreId , String requestingStoreName) {
		super();
		this.id = id;
		this.prDate = prDate;
		this.prNo = prNo;
		this.storeId = storeId;
		this.storeName = storeName;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.prTypeId = prTypeId;
		this.prType = prType;
		this.priorityId = priorityId;
		this.priority = priority;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
		this.statusId = statusId;
		this.status = status;
		this.remark = remark;
		this.requestingStoreId = requestingStoreId;
		this.requestingStoreName = requestingStoreName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPrDate() {
		return prDate;
	}

	public void setPrDate(Date prDate) {
		this.prDate = prDate;
	}

	public String getPrNo() {
		return prNo;
	}

	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public Integer getPrTypeId() {
		return prTypeId;
	}

	public void setPrTypeId(Integer prTypeId) {
		this.prTypeId = prTypeId;
	}

	public String getPrType() {
		return prType;
	}

	public void setPrType(String prType) {
		this.prType = prType;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<LocPurchaseReqDetailsResponseDto> getItems() {
		return items;
	}

	public void setItems(List<LocPurchaseReqDetailsResponseDto> items) {
		this.items = items;
	}

	public void addItems(LocPurchaseReqDetailsResponseDto item) {
		if (this.items == null)
			this.items = new ArrayList<LocPurchaseReqDetailsResponseDto>();

		this.items.add(item);
	}

}
