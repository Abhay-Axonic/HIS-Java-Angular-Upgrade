package com.param.procurement.workOrder.dto.workOrderRequest;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class WorkOrderRequest extends CookieObjectDto {

	private Integer id;

	private Date fromDate;

	private Date toDate;

	private Integer contractTypeId;

	private Integer approvalStatusId;

	private Integer approvedById;

	private Date approvedOn;

	private String requestNumber;

	private String cancelNote;

	private Integer cancelReasonId;

	private String remark;

	private Integer statusId;

	private Integer storeId;

	private Boolean sendWorkOrderCreation;

	private Boolean sendQuotationRequest;

	private List<Integer> workOrderIds;

	private List<WorkOrderRequestDetail> items;

	public WorkOrderRequest() {
		super();
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

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
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

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public List<WorkOrderRequestDetail> getItems() {
		return items;
	}

	public void setItems(List<WorkOrderRequestDetail> items) {
		this.items = items;
	}

	public List<Integer> getWorkOrderIds() {
		return workOrderIds;
	}

	public void setWorkOrderIds(List<Integer> workOrderIds) {
		this.workOrderIds = workOrderIds;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Boolean getSendWorkOrderCreation() {
		return sendWorkOrderCreation;
	}

	public void setSendWorkOrderCreation(Boolean sendWorkOrderCreation) {
		this.sendWorkOrderCreation = sendWorkOrderCreation;
	}

	public Boolean getSendQuotationRequest() {
		return sendQuotationRequest;
	}

	public void setSendQuotationRequest(Boolean sendQuotationRequest) {
		this.sendQuotationRequest = sendQuotationRequest;
	}

}
