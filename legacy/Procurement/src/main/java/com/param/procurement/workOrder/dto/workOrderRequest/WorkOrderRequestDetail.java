package com.param.procurement.workOrder.dto.workOrderRequest;

public class WorkOrderRequestDetail {

	private Integer id;

	private Integer approvedQuantity;

	private Integer rejectQuantity;
	
	private Integer rejectReasonId;
	
	private String rejectReason;

	private Integer currentVendorId;

	private Integer itemId;

	private Integer quantity;

	private String remark;

	private String specification;

	private Integer statusId;

	private Integer vendorId;
	
	private Long batchId;
	
	public WorkOrderRequestDetail() {
		super();
	}
	
	public WorkOrderRequestDetail(Integer id, Integer approvedQuantity, Integer rejectQuantity, Integer currentVendorId,
			Integer itemId, Integer quantity, String remark, String specification, Integer statusId, Integer vendorId,Long batchId) {
		super();
		this.id = id;
		this.approvedQuantity = approvedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.currentVendorId = currentVendorId;
		this.itemId = itemId;
		this.quantity = quantity;
		this.remark = remark;
		this.specification = specification;
		this.statusId = statusId;
		this.vendorId = vendorId;
		this.batchId = batchId;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
}
