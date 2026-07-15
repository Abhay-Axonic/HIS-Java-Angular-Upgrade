package com.param.procurement.workOrder.dto.workOrderQuotationRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class WorkOrderQrResponse extends WorkOrderQR {

	private String approvalStatus;

	private String approvedBy;

	private Date approvedDate;

	private String contractType;

	private String requestType;

	private String status;

	private String store;

	private String workOrderQRNo;

	private Date qrDate;

	private String addedBy;
	
	private Boolean requestForNegotiation;

	private List<WorkOrderQRDetailResponse> details;

	public WorkOrderQrResponse() {
		super();
	}

	public WorkOrderQrResponse(Integer id, String remark, Boolean sendVendorSelection, String approvalStatus,
			String contractType, String requestType, String status, String store, String workOrderQRNo,
			Date qrDate, String addedBy) {
		super(id, remark, sendVendorSelection);
		this.approvalStatus = approvalStatus;
		this.contractType = contractType;
		this.requestType = requestType;
		this.status = status;
		this.store = store;
		this.workOrderQRNo = workOrderQRNo;
		this.qrDate = qrDate;
		this.addedBy = addedBy;

	}

	public WorkOrderQrResponse(Integer id, Integer approvalStatusId, Integer approvedById, Integer contractTypeId,
			String remark, Integer requestTypeId, Boolean sendVendorSelection, Integer statusId, Integer storeId,
			Integer workOrderId, String approvalStatus, String approvedBy, Date approvedDate, String contractType,
			String requestType, String status, String store, String workOrderQRNo,Date qrDate, String addedBy,Boolean requestForNegotiation) {
		super(id, approvalStatusId, approvedById, contractTypeId, remark, requestTypeId, sendVendorSelection, statusId,
				storeId, workOrderId);
		this.approvalStatus = approvalStatus;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.contractType = contractType;
		this.requestType = requestType;
		this.status = status;
		this.store = store;
		this.workOrderQRNo = workOrderQRNo;
		this.qrDate = qrDate;
		this.addedBy = addedBy;
		this.requestForNegotiation = requestForNegotiation;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getWorkOrderQRNo() {
		return workOrderQRNo;
	}

	public void setWorkOrderQRNo(String workOrderQRNo) {
		this.workOrderQRNo = workOrderQRNo;
	}

	public List<WorkOrderQRDetailResponse> getDetails() {
		return details;
	}

	public void setDetails(List<WorkOrderQRDetailResponse> details) {
		this.details = details;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Boolean getRequestForNegotiation() {
		return requestForNegotiation;
	}

	public void setRequestForNegotiation(Boolean requestForNegotiation) {
		this.requestForNegotiation = requestForNegotiation;
	}

	public void addItems(WorkOrderQRDetailResponse item) {
		if (this.details == null)
			this.details = new ArrayList<WorkOrderQRDetailResponse>();

		this.details.add(item);
	}

}
