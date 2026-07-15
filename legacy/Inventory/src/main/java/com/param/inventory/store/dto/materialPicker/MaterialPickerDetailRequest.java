package com.param.inventory.store.dto.materialPicker;

import java.util.List;

import com.param.global.dto.items.BatchResponse;

public class MaterialPickerDetailRequest {

	protected Integer id;
	
    protected Integer materialPickerDetailId;
    
    protected Integer itemId;
    
	protected Integer statusId;

	protected Integer pickerQuantity;
	
	protected Integer issueQuantity;
	protected Integer storeIndentDetailId;
	protected List<BatchResponse> batches;
	protected Integer ParentId;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMaterialPickerDetailId() {
		return materialPickerDetailId;
	}

	public void setMaterialPickerDetailId(Integer materialPickerDetailId) {
		this.materialPickerDetailId = materialPickerDetailId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getPickerQuantity() {
		return pickerQuantity;
	}

	public void setPickerQuantity(Integer pickerQuantity) {
		this.pickerQuantity = pickerQuantity;
	}

	public List<BatchResponse> getBatches() {
		return batches;
	}

	public void setBatches(List<BatchResponse> batches) {
		this.batches = batches;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Integer getParentId() {
		return ParentId;
	}

	public void setParentId(Integer parentId) {
		ParentId = parentId;
	}

	public Integer getStoreIndentDetailId() {
		return storeIndentDetailId;
	}

	public void setStoreIndentDetailId(Integer storeIndentDetailId) {
		this.storeIndentDetailId = storeIndentDetailId;
	}

}
