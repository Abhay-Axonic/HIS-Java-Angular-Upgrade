package com.param.procurement.workOrder.dto.workOrderQuotationRequest;

import java.util.List;

import com.param.global.dto.items.BatchResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQRDto;

public class WorkOrderQRDetailResponse extends WorkOrderQRDetail{
	
	private String currentVendor;


	private String status;

	private String itemName;
	
	private String itemCode;
	
     private Long batchId;
	
	private String batchNo;
	
	protected List<BatchResponse> batchList;
	
	public WorkOrderQRDetailResponse(Integer id, Integer currentVendorId, String specification, Integer statusId,
			Integer workOrderRequestDetailId, String remark, Integer quantity, String expectedInclusion,
			Integer itemId,
			String currentVendor, String status, String itemName,
			String itemCode,Long batchId,String batchNo,List<BatchResponse> batchList) {
		super(id,currentVendorId,specification,statusId,workOrderRequestDetailId,remark,quantity,expectedInclusion,itemId);
		this.currentVendor = currentVendor;
		this.status = status;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.batchList = batchList;
	}
	

	public WorkOrderQRDetailResponse(Integer id, Integer currentVendorId, String specification, Integer statusId,
			Integer workOrderRequestDetailId, String remark, Integer quantity, String expectedInclusion,
			Integer itemId,
			String currentVendor, String status, String itemName,
			String itemCode,Long batchId,String batchNo) {
		super(id,currentVendorId,specification,statusId,workOrderRequestDetailId,remark,quantity,expectedInclusion,itemId);
		this.currentVendor = currentVendor;
		this.status = status;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.batchId = batchId;
		this.batchNo = batchNo;
	}
	
	public WorkOrderQRDetailResponse(Integer id, Integer currentVendorId, String specification, Integer statusId,
			Integer workOrderRequestDetailId, String remark, Integer quantity, String expectedInclusion,
			Integer itemId,
			String currentVendor, String status, String itemName,
			String itemCode,Long batchId,List<WorkOrderQRDto> requestDetails,String batchNo) {
		super(id,currentVendorId,specification,statusId,workOrderRequestDetailId,remark,quantity,expectedInclusion,itemId,requestDetails);
		this.currentVendor = currentVendor;
		this.status = status;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.batchId = batchId;
		this.batchNo = batchNo;
	}


	public List<BatchResponse> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<BatchResponse> batchList) {
		this.batchList = batchList;
	}

	public String getCurrentVendor() {
		return currentVendor;
	}

	public void setCurrentVendor(String currentVendor) {
		this.currentVendor = currentVendor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
}
