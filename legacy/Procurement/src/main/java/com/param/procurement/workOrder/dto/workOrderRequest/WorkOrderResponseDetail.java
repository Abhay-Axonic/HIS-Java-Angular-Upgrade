package com.param.procurement.workOrder.dto.workOrderRequest;

import java.util.List;

import com.param.global.dto.items.BatchResponse;

public class WorkOrderResponseDetail extends WorkOrderRequestDetail{
	
	private String currentVendor;
	
	private String itemCode;
	
	private String itemName;
	
	private String vendor;
	
	private String batchNo;
	
	private String status;
	
	protected List<BatchResponse> batchList;

	public WorkOrderResponseDetail() {
		super();
	}
	


	public WorkOrderResponseDetail(Integer id, Integer approvedQuantity, Integer rejectQuantity,
			Integer currentVendorId, Integer itemId, Integer quantity, String remark, String specification,
			Integer statusId, Integer vendorId,String currentVendor, String itemCode, String itemName, String vendor, String status,Long batchId , String batchNo,List<BatchResponse> batchList) {
		super(id, approvedQuantity, rejectQuantity, currentVendorId, itemId, quantity, remark, specification, statusId,
				vendorId,batchId);
		this.currentVendor = currentVendor;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.vendor = vendor;
		this.status = status;
		this.batchNo = batchNo;
		this.batchList = batchList;
	}
	
	public String getCurrentVendor() {
		return currentVendor;
	}

	public void setCurrentVendor(String currentVendor) {
		this.currentVendor = currentVendor;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public List<BatchResponse> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<BatchResponse> batchList) {
		this.batchList = batchList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
