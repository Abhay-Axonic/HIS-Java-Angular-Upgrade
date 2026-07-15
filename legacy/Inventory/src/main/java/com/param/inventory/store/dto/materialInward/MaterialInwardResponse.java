package com.param.inventory.store.dto.materialInward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MaterialInwardResponse {

	protected Integer id;

	protected Date inwardDate;

	protected String inwardNo;

	protected Integer storeId;

	protected String store;

	protected String remarks;

	protected Double returnAmount;

	protected Double vendorPayable;

	protected Double creditNote;

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Double getVendorPayable() {
		return vendorPayable;
	}

	public void setVendorPayable(Double vendorPayable) {
		this.vendorPayable = vendorPayable;
	}

	public Double getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(Double creditNote) {
		this.creditNote = creditNote;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
//protected Integer requestingStoreId;

	//protected String requestingStore;

	/*public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public String getRequestingStore() {
		return requestingStore;
	}

	public void setRequestingStore(String requestingStore) {
		this.requestingStore = requestingStore;
	}*/

	protected Integer vendorId;

	protected String vendor;

	protected Integer assetTypeId;

	protected String assetType;

	protected String replacementType;

	protected Integer replacementTypeId;

	protected Integer returnableGatePassId;

	protected Date returnableGatePassDate;

	protected String returnableGatePassNo;

	protected String returnGPNo;

	protected String status;

	protected Integer statusId;

	protected String addedBY;

	protected Date documentDate;

	protected String documentNo;

	protected Double totalAmount;

	protected Double totalTaxAmount;

	protected Double totalNetAmount;

	protected List<MaterialItemResponse> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInwardDate() {
		return inwardDate;
	}

	public void setInwardDate(Date inwardDate) {
		this.inwardDate = inwardDate;
	}

	public String getInwardNo() {
		return inwardNo;
	}

	public void setInwardNo(String inwardNo) {
		this.inwardNo = inwardNo;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Date getReturnableGatePassDate() {
		return returnableGatePassDate;
	}

	public void setReturnableGatePassDate(Date returnableGatePassDate) {
		this.returnableGatePassDate = returnableGatePassDate;
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

	public String getReplacementType() {
		return replacementType;
	}

	public void setReplacementType(String replacementType) {
		this.replacementType = replacementType;
	}

	public Integer getReplacementTypeId() {
		return replacementTypeId;
	}

	public void setReplacementTypeId(Integer replacementTypeId) {
		this.replacementTypeId = replacementTypeId;
	}

	public String getReturnGPNo() {
		return returnGPNo;
	}

	public void setReturnGPNo(String returnGPNo) {
		this.returnGPNo = returnGPNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddedBY() {
		return addedBY;
	}

	public void setAddedBY(String addedBY) {
		this.addedBY = addedBY;
	}

	public Integer getReturnableGatePassId() {
		return returnableGatePassId;
	}

	public void setReturnableGatePassId(Integer returnableGatePassId) {
		this.returnableGatePassId = returnableGatePassId;
	}

	public String getReturnableGatePassNo() {
		return returnableGatePassNo;
	}

	public void setReturnableGatePassNo(String returnableGatePassNo) {
		this.returnableGatePassNo = returnableGatePassNo;
	}

	public List<MaterialItemResponse> getItems() {
		return items;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public void setItems(List<MaterialItemResponse> items) {
		this.items = items;
	}

	public void addItems(MaterialItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<MaterialItemResponse>();

		this.items.add(item);
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

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public MaterialInwardResponse(Integer id, Date inwardDate, String inwardNo, Integer storeId, String store,
			Integer vendorId, String vendor, Integer assetTypeId, String assetType, String replacementType,
			Integer replacementTypeId, Integer returnableGatePassId, String returnableGatePassNo, String returnGPNo,
			String status, String addedBY, List<MaterialItemResponse> items) {
		super();
		this.id = id;
		this.inwardDate = inwardDate;
		this.inwardNo = inwardNo;
		this.storeId = storeId;
		this.store = store;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.replacementType = replacementType;
		this.replacementTypeId = replacementTypeId;
		this.returnableGatePassId = returnableGatePassId;
		this.returnableGatePassNo = returnableGatePassNo;
		this.returnGPNo = returnGPNo;
		this.status = status;
		this.addedBY = addedBY;
		this.items = items;
	}

	public MaterialInwardResponse() {
		super();
	}

	public MaterialInwardResponse(Integer id, String inwordNo, Date addedDate, Integer storeId, String store,
			Integer vendorId, String vendor, Integer assetTypeId, String assetType, Integer requestTypeId,
			String requestType, Integer returnableGatePassId, String retGpNo, String documentNo, Date documentDate,
			Double totalAmount, Double totalTaxAmount, Double totalNetAmount, Integer statusId, String statusName ) {
		this.id = id;
		this.inwardDate = addedDate;
		this.inwardNo = inwordNo;
		this.storeId = storeId;
		this.store = store;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.replacementType = requestType;
		this.replacementTypeId = requestTypeId;
		this.returnableGatePassId = returnableGatePassId;
		this.returnableGatePassNo = retGpNo;
		this.documentNo = documentNo;
		this.documentDate = documentDate;
		this.totalAmount = totalAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalNetAmount = totalNetAmount;
		this.statusId = statusId;
		this.status = statusName;
	//	this.requestingStoreId = requestingStoreId;
	//	this.requestingStore = requestingStore;
	}

	public MaterialInwardResponse(Integer id, String retGpNo, Date addedDate, Integer assetTypeId, String assetType,
			Integer requestTypeId, String requestType, Integer storeId, String storeName, Integer vendorId,
			String vendor) {

		this.id = id;
		this.returnableGatePassNo = retGpNo;
		this.returnableGatePassDate = addedDate;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.replacementTypeId = requestTypeId;
		this.replacementType = requestType;
		this.storeId = storeId;
		this.store = storeName;
		this.vendorId = vendorId;
		this.vendor = vendor;
	}

	public MaterialInwardResponse(Integer id, String retGpNo, Date addedDate, Integer assetTypeId, String assetType,
			Integer requestTypeId, String requestType, Integer storeId, String storeName, Integer vendorId,
			String vendor, Double totalAmount, Double totalTaxAmount, Double totalNetAmount) {

		this.id = id;
		this.returnableGatePassNo = retGpNo;
		this.returnableGatePassDate = addedDate;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.replacementTypeId = requestTypeId;
		this.replacementType = requestType;
		this.storeId = storeId;
		this.store = storeName;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.totalAmount = totalAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalNetAmount = totalNetAmount;
	}

}
