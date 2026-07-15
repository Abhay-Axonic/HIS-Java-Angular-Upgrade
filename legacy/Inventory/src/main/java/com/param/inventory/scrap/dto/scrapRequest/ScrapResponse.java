package com.param.inventory.scrap.dto.scrapRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.inventory.scrap.dto.requestAssestDisposalWorklist.WitnessResponse;

@JsonInclude(Include.NON_NULL)
public class ScrapResponse {

	protected Integer id;

	protected String requestNo;

	protected Date requestDate;

	protected Integer storeId;

	protected String storeName;

	protected Integer assetTypeId;

	protected String assetType;

	protected String requestedBy;

	protected String remark;

	protected String status;

	protected Integer statusId;

	protected String approvalStatus;

	protected Integer approvalStatusId;

	protected Boolean nearByExpiry;

	protected  Integer scrapType;

	protected List<com.param.inventory.scrap.dto.scrapRequest.ItemResponse> items;

	protected List<WitnessResponse> witnessDetails;

	protected Integer witnessId;

	protected String witnessName;

	public ScrapResponse() {
		super();
	}

	public ScrapResponse(Integer id) {
		super();
		this.id = id;
	}

	public ScrapResponse(Integer id, String requestNo, Date requestDate, Integer storeId, String storeName,
			Integer assetTypeId, String assetType, String requestedBy, String remark, String status, Integer statusId,
			String approvalStatus, Integer approvalStatusId, Boolean nearByExpiry) {
		super();
		this.id = id;
		this.requestNo = requestNo;
		this.requestDate = requestDate;
		this.storeId = storeId;
		this.storeName = storeName;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.requestedBy = requestedBy;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
		this.nearByExpiry = nearByExpiry;
	}

	public ScrapResponse(Integer id, String requestNo, Date requestDate, Integer storeId, String storeName,
			Integer assetTypeId, String assetType, String requestedBy, String status, Integer statusId,
			String approvalStatus, Integer approvalStatusId) {
		super();
		this.id = id;
		this.requestNo = requestNo;
		this.requestDate = requestDate;
		this.storeId = storeId;
		this.storeName = storeName;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.requestedBy = requestedBy;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
	}

	public List<com.param.inventory.scrap.dto.scrapRequest.ItemResponse> getItems() {
		return items;
	}

	public void setItems(List<com.param.inventory.scrap.dto.scrapRequest.ItemResponse> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
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

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Boolean getNearByExpiry() {
		return nearByExpiry;
	}

	public void setNearByExpiry(Boolean nearByExpiry) {
		this.nearByExpiry = nearByExpiry;
	}

	public Integer getWitnessId() {
		return witnessId;
	}

	public void setWitnessId(Integer witnessId) {
		this.witnessId = witnessId;
	}

	public String getWitnessName() {
		return witnessName;
	}

	public void setWitnessName(String witnessName) {
		this.witnessName = witnessName;
	}

	public List<WitnessResponse> getWitnessDetails() {
		return witnessDetails;
	}

	public void setWitnessDetails(List<WitnessResponse> witnessDetails) {
		this.witnessDetails = witnessDetails;
	}

	public void addItems(com.param.inventory.scrap.dto.scrapRequest.ItemResponse items) {
		if (this.items == null)
			this.items = new ArrayList<com.param.inventory.scrap.dto.scrapRequest.ItemResponse>();

		this.items.add(items);
	}

	public Integer getScrapType() {
		return scrapType;
	}

	public void setScrapType(Integer scrapType) {
		this.scrapType = scrapType;
	}
}
