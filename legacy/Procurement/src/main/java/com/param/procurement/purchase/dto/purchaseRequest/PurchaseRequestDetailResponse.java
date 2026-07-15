package com.param.procurement.purchase.dto.purchaseRequest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseRequestDetailResponse {
    private Long id;
    private Date prDate;
    private String prNo;
    private Integer storeId;
    private String storeName;
    private Integer requestingStoreId;
    private String requestingStoreName;
    private Integer priorityId;
    private String priority;
    private Integer assetTypeId;
    private String assetType;
    private Integer prTypeId;
    private String prType;
    private String remark;
    private String status;
    private String approvalStatus;
    private List<ItemResponse> items;

    public PurchaseRequestDetailResponse() {
        super();
    }

    public PurchaseRequestDetailResponse(Long id, Date prDate, String prNo, Integer storeId, String storeName, Integer priorityId, String priority, Integer assetTypeId,
                                         String assetType, Integer prTypeId, String prType, String remark, String status, String approvalStatus, Integer requestingStoreId, String requestingStoreName ) {
        this.id = id;
        this.prDate = prDate;
        this.prNo = prNo;
        this.storeId = storeId;
        this.storeName = storeName;
        this.priorityId = priorityId;
        this.priority = priority;
        this.assetTypeId = assetTypeId;
        this.assetType = assetType;
        this.prTypeId = prTypeId;
        this.prType = prType;
        this.remark = remark;
        this.status = status;
        this.approvalStatus = approvalStatus;
        this.requestingStoreId = requestingStoreId;
        this.requestingStoreName = requestingStoreName;
    }

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

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemResponse> items) {
        this.items = items;
    }
    
   public void addItems(ItemResponse item) {
        if (this.items == null)
            this.items = new ArrayList<ItemResponse>();

        this.items.add(item);
    }

    @Override
	public String toString() {
		return "PurchaseRequestDetailResponse [id=" + id + ", prDate=" + prDate + ", prNo=" + prNo + ", storeId="
				+ storeId + ", storeName=" + storeName + ", requestingStoreId=" + requestingStoreId
				+ ", requestingStoreName=" + requestingStoreName + ", priorityId=" + priorityId + ", priority="
				+ priority + ", assetTypeId=" + assetTypeId + ", assetType=" + assetType + ", prTypeId=" + prTypeId
				+ ", prType=" + prType + ", remark=" + remark + ", status=" + status + ", approvalStatus="
				+ approvalStatus + ", items=" + items + "]";
	}
}
