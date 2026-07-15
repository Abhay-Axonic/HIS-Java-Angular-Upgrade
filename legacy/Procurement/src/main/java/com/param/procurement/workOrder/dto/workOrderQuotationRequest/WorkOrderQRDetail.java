package com.param.procurement.workOrder.dto.workOrderQuotationRequest;

import java.util.List;

import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQRDto;

public class WorkOrderQRDetail {
	
	private Integer id;

	private Integer currentVendorId;

	private String specification;

	private Integer statusId;

	private Integer workOrderRequestDetailId;
	
	private String remark;
	
	private Integer quantity;
	
	private String expectedInclusion;
	
	private Integer itemId;
	
    private Long batchId;
    
    List<WorkOrderQRDto> requestDetails;
	
	
	public WorkOrderQRDetail() {
		super();
	}

	public WorkOrderQRDetail(Integer id, Integer currentVendorId, String specification, Integer statusId,
			Integer workOrderRequestDetailId, String remark, Integer quantity, String expectedInclusion,
			Integer itemId) {
		super();
		this.id = id;
		this.currentVendorId = currentVendorId;
		this.specification = specification;
		this.statusId = statusId;
		this.workOrderRequestDetailId = workOrderRequestDetailId;
		this.remark = remark;
		this.quantity = quantity;
		this.expectedInclusion = expectedInclusion;
		this.itemId = itemId;
	}
	
	public WorkOrderQRDetail(Integer id, Integer currentVendorId, String specification, Integer statusId,
			Integer workOrderRequestDetailId, String remark, Integer quantity, String expectedInclusion,
			Integer itemId,List<WorkOrderQRDto> requestDetails) {
		super();
		this.id = id;
		this.currentVendorId = currentVendorId;
		this.specification = specification;
		this.statusId = statusId;
		this.workOrderRequestDetailId = workOrderRequestDetailId;
		this.remark = remark;
		this.quantity = quantity;
		this.expectedInclusion = expectedInclusion;
		this.itemId = itemId;
		this.requestDetails=requestDetails;
	}

	public WorkOrderQRDetail(Integer currentVendorId2, String specification2, Integer statusId2,
			Integer workOrderRequestDetailId2, String remark2, Integer quantity2, String expectedInclusion2,
			Integer itemId2) {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getWorkOrderRequestDetailId() {
		return workOrderRequestDetailId;
	}

	public void setWorkOrderRequestDetailId(Integer workOrderRequestDetailId) {
		this.workOrderRequestDetailId = workOrderRequestDetailId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getExpectedInclusion() {
		return expectedInclusion;
	}

	public void setExpectedInclusion(String expectedInclusion) {
		this.expectedInclusion = expectedInclusion;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public List<WorkOrderQRDto> getRequestDetails() {
		return requestDetails;
	}

	public void setRequestDetails(List<WorkOrderQRDto> requestDetails) {
		this.requestDetails = requestDetails;
	}
	
	
	
}
