package com.param.inventory.store.dto.returnableGatepass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.Vendor;
import com.param.inventory.store.dto.materialInward.MaterialItemResponse;

@JsonInclude(Include.NON_NULL)
public class ReturnableGatepassResponse {
	protected Integer id;

	protected String gatepassNo;

	protected Date gatepassDate;

	protected Integer assetTypeId;

	protected String assetType;

	protected Integer againstId;

	protected String against;

	protected Integer storeId;

	protected String store;

	//protected Integer requestingStoreId;

	protected String requestingStore;

	protected Integer returnableGatePassDetailId;

	protected Integer grnDetailId;

/*	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}*/

	public String getRequestingStore() {
		return requestingStore;
	}

	public void setRequestingStore(String requestingStore) {
		this.requestingStore = requestingStore;
	}

	protected Integer vendorId;

	protected String vendor;

	protected String referenceNo;

	protected String requestedBy;

	protected String requestedType;

	protected Integer statusId;

	protected String status;

	protected Integer approvalStatusId;

	protected Integer requestTypeId;

	protected String approvalStatus;

	protected Double totalAmount;

	protected Double totalTaxAmount;

	protected Double netAmount;

	protected String remark;

	protected String userName;

	protected List<ItemResponse> items;

	public ReturnableGatepassResponse() {
		super();
	}

	public ReturnableGatepassResponse(Integer id, String gatepassNo, Date addedDate, Integer storeId, String storeName,
			Integer venderid, String vendername, Integer assetTypeId, String assetTypeName, Integer reTypeId,
			String reTypeName, String referenceNo, Integer approvalStattusId, String apprStatus, Double totalAmt,
			Double totalTaxAmount, Double netAmount, Integer statusId, String status, String remark, String userName) {

		this.id = id;
		this.gatepassNo = gatepassNo;
		this.gatepassDate = addedDate;
		this.storeId = storeId;
		this.store = storeName;
		this.vendorId = venderid;
		this.vendor = vendername;
		this.assetTypeId = assetTypeId;
		this.assetType = assetTypeName;
		this.requestTypeId = reTypeId;
		this.referenceNo = referenceNo;
		this.requestedType = reTypeName;
		this.approvalStatusId = approvalStattusId;
		this.approvalStatus = apprStatus;
		this.totalAmount = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		this.netAmount = netAmount;
		this.statusId = statusId;
		this.status = status;
		this.remark = remark;
		this.userName = userName;
		//this.requestingStoreId = requestingStoreId;
		//this.requestingStore = requestingStore;

	}

	public String getRequestedType() {
		return requestedType;
	}

	public void setRequestedType(String requestedType) {
		this.requestedType = requestedType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGatepassNo() {
		return gatepassNo;
	}

	public void setGatepassNo(String gatepassNo) {
		this.gatepassNo = gatepassNo;
	}

	public Date getGatepassDate() {
		return gatepassDate;
	}

	public void setGatepassDate(Date gatepassDate) {
		this.gatepassDate = gatepassDate;
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

	public Integer getAgainstId() {
		return againstId;
	}

	public void setAgainstId(Integer againstId) {
		this.againstId = againstId;
	}

	public String getAgainst() {
		return against;
	}

	public void setAgainst(String against) {
		this.against = against;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ItemResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemResponse> items) {
		this.items = items;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public void addItems(ItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ItemResponse>();

		this.items.add(item);
	}

	public Integer getReturnableGatePassDetailId() {
		return returnableGatePassDetailId;
	}

	public void setReturnableGatePassDetailId(Integer returnableGatePassDetailId) {
		this.returnableGatePassDetailId = returnableGatePassDetailId;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

}