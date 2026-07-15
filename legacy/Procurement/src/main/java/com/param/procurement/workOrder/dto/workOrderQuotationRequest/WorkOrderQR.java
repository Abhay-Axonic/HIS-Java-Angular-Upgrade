package com.param.procurement.workOrder.dto.workOrderQuotationRequest;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class WorkOrderQR extends CookieObjectDto {
	
	private Integer id;
	
	private Date fromDate;

	private Date toDate;
	
	private Integer approvalStatusId;

	private Integer approvedById;

	private String cancelNote;

	private Integer cancelReasonId;

	private Integer contractTypeId;

	private String remark;
	
	private Integer requestTypeId;

	private Boolean sendVendorSelection;
	
	private Integer statusId;
	
	private Integer storeId;
	
	private Integer workOrderId;
	
	private List<Integer> woIds;
	
	private List<Integer> qrIds;
	
	private List<WorkOrderQRDetail> items;
	
	private String workOrderQRNo;
	
	private Integer vendorId;

	public WorkOrderQR() {
		super();
	}
	
	public WorkOrderQR(Integer id, String remark, Boolean sendVendorSelection) {
		super();
		this.id = id;
		this.remark = remark;
		this.sendVendorSelection = sendVendorSelection;
	}
	
	public WorkOrderQR(Integer id, Integer approvalStatusId, Integer approvedById,
			Integer contractTypeId, String remark, Integer requestTypeId,
			Boolean sendVendorSelection, Integer statusId, Integer storeId, Integer workOrderId) {
		super();
		this.id = id;
		this.approvalStatusId = approvalStatusId;
		this.approvedById = approvedById;
		this.contractTypeId = contractTypeId;
		this.remark = remark;
		this.requestTypeId = requestTypeId;
		this.sendVendorSelection = sendVendorSelection;
		this.statusId = statusId;
		this.storeId = storeId;
		this.workOrderId = workOrderId;
	}
	
	
	
	public WorkOrderQR(Integer id, Integer approvalStatusId, Integer approvedById, String cancelNote,
			Integer cancelReasonId, Integer contractTypeId, String remark, Integer requestTypeId,
			Boolean sendVendorSelection, Integer statusId, Integer storeId, Integer workOrderId,
			List<WorkOrderQRDetail> items) {
		super();
		this.id = id;
		this.approvalStatusId = approvalStatusId;
		this.approvedById = approvedById;
		this.cancelNote = cancelNote;
		this.cancelReasonId = cancelReasonId;
		this.contractTypeId = contractTypeId;
		this.remark = remark;
		this.requestTypeId = requestTypeId;
		this.sendVendorSelection = sendVendorSelection;
		this.statusId = statusId;
		this.storeId = storeId;
		this.workOrderId = workOrderId;
		this.items = items;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getApprovedById() {
		return approvedById;
	}

	public void setApprovedById(Integer approvedById) {
		this.approvedById = approvedById;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Boolean getSendVendorSelection() {
		return sendVendorSelection;
	}

	public void setSendVendorSelection(Boolean sendVendorSelection) {
		this.sendVendorSelection = sendVendorSelection;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public List<WorkOrderQRDetail> getItems() {
		return items;
	}

	public void setItems(List<WorkOrderQRDetail> items) {
		this.items = items;
	}

	public Integer getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Integer workOrderId) {
		this.workOrderId = workOrderId;
	}

	public List<Integer> getQrIds() {
		return qrIds;
	}

	public void setQrIds(List<Integer> qrIds) {
		this.qrIds = qrIds;
	}

	public List<Integer> getWoIds() {
		return woIds;
	}

	public void setWoIds(List<Integer> woIds) {
		this.woIds = woIds;
	}

	public String getWorkOrderQRNo() {
		return workOrderQRNo;
	}

	public void setWorkOrderQRNo(String workOrderQRNo) {
		this.workOrderQRNo = workOrderQRNo;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	
}
