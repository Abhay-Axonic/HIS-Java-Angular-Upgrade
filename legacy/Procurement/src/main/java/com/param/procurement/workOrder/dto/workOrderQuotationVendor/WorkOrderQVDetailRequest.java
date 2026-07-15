package com.param.procurement.workOrder.dto.workOrderQuotationVendor;

public class WorkOrderQVDetailRequest {

	private Integer id;

	private Integer workOrderQRDetailId;

	private String remark;
	 private Integer vendorId;
	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	private Integer statusId;

	public WorkOrderQVDetailRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWorkOrderQRDetailId() {
		return workOrderQRDetailId;
	}

	public void setWorkOrderQRDetailId(Integer workOrderQRDetailId) {
		this.workOrderQRDetailId = workOrderQRDetailId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

}
