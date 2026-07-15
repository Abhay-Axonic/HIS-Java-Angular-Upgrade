package com.param.procurement.workOrder.dto.workOrderQuotationVendor;

import java.util.List;

public class WorkOrderQVDetailResponse {

	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer quantity;

	private Integer currentVendorId;

	private String specification;

	private String expectedInclusion;
	
/*	private String expectedExclusion;
	
	private String paymentTerm;


	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getExpectedExclusion() {
		return expectedExclusion;
	}

	public void setExpectedExclusion(String expectedExclusion) {
		this.expectedExclusion = expectedExclusion;
	}*/

	private String remark;
	
	private String batchNo;

	private List<WorkOrderQRDto> requestDetails;

	private String currentVendorName;

	
	public WorkOrderQVDetailResponse() {
		super();
	}
	
	public WorkOrderQVDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			Integer currentVendorId, String specification, String expectedInclusion, String remark,String batchNo,List<WorkOrderQRDto> requestDetails) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.currentVendorId = currentVendorId;
		this.specification = specification;
		this.expectedInclusion = expectedInclusion;
		this.remark = remark;
		this.batchNo = batchNo;
		this.requestDetails = requestDetails;
	
	}
	

	public WorkOrderQVDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			Integer currentVendorId,String currentVendorName, String specification, String expectedInclusion, String remark,String batchNo,List<WorkOrderQRDto> requestDetails) {
		this( id,itemId,  itemCode,  itemName,  quantity,
			 currentVendorId, specification,  expectedInclusion,  remark, batchNo, requestDetails);
		this.currentVendorName = currentVendorName;
		}

public String getCurrentVendorName() {
		return currentVendorName;
	}

	public void setCurrentVendorName(String currentVendorName) {
		this.currentVendorName = currentVendorName;
	}

public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getExpectedInclusion() {
		return expectedInclusion;
	}

	public void setExpectedInclusion(String expectedInclusion) {
		this.expectedInclusion = expectedInclusion;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<WorkOrderQRDto> getRequestDetails() {
		return requestDetails;
	}

	public void setRequestDetails(List<WorkOrderQRDto> requestDetails) {
		this.requestDetails = requestDetails;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
}
