package com.param.inventory.store.dto.materialPicker;

import java.util.List;

import com.param.global.dto.items.BatchResponse;

public class SubstituteMaterialIssueDetailDto {
	
	private Integer id;
	
	private Integer materialIssueId;
	
	private Integer materialPickerDetailId;
	
	private Integer storeIndentDetailId;
	
	private Integer issueQuantity;
	
	private Integer itemId;
	
	private Integer parentMaterialIssueId;
	
	private String itemName;
	
	private String itemCode;
	
	private String genericName;
	private Integer availableStockQuantity;
	private String uomUnit;
	private Integer uomUnitId;
	private String uomType;
	private Integer uomTypeId;

	private Integer indentQuantity;
	private List<BatchResponse> batch;
	private List<BatchResponse> newBatch;
	
	private Integer fromStoreId;
	private Integer toStoreId;
	private Integer materialPickerId;
	private Integer storeIndentId;
	private Integer carrierId;
	private Integer statusId;
	private String remark;
	private String issueNo;
	private boolean isConsignment;
	private Integer pickerId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMaterialIssueId() {
		return materialIssueId;
	}

	public void setMaterialIssueId(Integer materialIssueId) {
		this.materialIssueId = materialIssueId;
	}

	public Integer getMaterialPickerDetailId() {
		return materialPickerDetailId;
	}

	public void setMaterialPickerDetailId(Integer materialPickerDetailId) {
		this.materialPickerDetailId = materialPickerDetailId;
	}

	public Integer getStoreIndentDetailId() {
		return storeIndentDetailId;
	}

	public void setStoreIndentDetailId(Integer storeIndentDetailId) {
		this.storeIndentDetailId = storeIndentDetailId;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getParentMaterialIssueId() {
		return parentMaterialIssueId;
	}

	public void setParentMaterialIssueId(Integer parentMaterialIssueId) {
		this.parentMaterialIssueId = parentMaterialIssueId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public List<BatchResponse> getNewBatch() {
		return newBatch;
	}

	public void setNewBatch(List<BatchResponse> newBatch) {
		this.newBatch = newBatch;
	}

	public Integer getAvailableStockQuantity() {
		return availableStockQuantity;
	}

	public void setAvailableStockQuantity(Integer availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getMaterialPickerId() {
		return materialPickerId;
	}

	public void setMaterialPickerId(Integer materialPickerId) {
		this.materialPickerId = materialPickerId;
	}

	public Integer getStoreIndentId() {
		return storeIndentId;
	}

	public void setStoreIndentId(Integer storeIndentId) {
		this.storeIndentId = storeIndentId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public boolean isConsignment() {
		return isConsignment;
	}

	public void setConsignment(boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Integer getPickerId() {
		return pickerId;
	}

	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
	}
	
	
}
