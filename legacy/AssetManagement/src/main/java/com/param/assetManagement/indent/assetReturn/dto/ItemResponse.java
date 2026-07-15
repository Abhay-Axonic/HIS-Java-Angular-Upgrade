package com.param.assetManagement.indent.assetReturn.dto;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.AsseccoriesResponse;
import com.param.global.dto.items.ItemsResponse;

public class ItemResponse extends ItemsResponse {

	protected Integer id;

	protected Integer returnReasonId;

	protected Integer returnQuantity;

	protected String returnReason;

	protected String batchNo;

	protected String genericName;

	protected List<AsseccoriesResponse> asseccories;
	protected Integer acceptedQuantity;
	protected Integer rejectQuantity;

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public ItemResponse() {
		super();
	}

	public ItemResponse(Integer itemId, String itemCode, String itemName, Long batchId, Date expiry,
			Long availableQuantity, Integer id, Integer returnReasonId, Integer returnQuantity, String returnReason,
			String batchNo, String genericName, List<AsseccoriesResponse> asseccories) {
		super(itemId, itemCode, itemName, batchId, expiry, availableQuantity);
		this.id = id;
		this.returnReasonId = returnReasonId;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.batchNo = batchNo;
		this.genericName = genericName;
		this.asseccories = asseccories;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReturnReasonId() {
		return returnReasonId;
	}

	public void setReturnReasonId(Integer returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public List<AsseccoriesResponse> getAsseccories() {
		return asseccories;
	}

	public void setAsseccories(List<AsseccoriesResponse> asseccories) {
		this.asseccories = asseccories;
	}

}
