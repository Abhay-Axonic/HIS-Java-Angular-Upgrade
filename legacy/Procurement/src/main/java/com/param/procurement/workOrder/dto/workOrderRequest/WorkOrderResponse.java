package com.param.procurement.workOrder.dto.workOrderRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDetailsResponse;

public class WorkOrderResponse {
	
	private Integer id;
	
	private String requestNumber;
	
	private Date requestDate;
	
	private String remark;
	
	private String addedBy;
	
	private Date addedOn;
	
	private Integer storeId;
	
	private String store;
	
	private String contractType;
	
	private Integer statusId;
	
	private String status;
	
    private Integer approvalStatusId;
	
	private String approvalStatus;
	
	private Boolean isQuotation;
	
	private Boolean isCreation;
	
	private List<WorkOrderResponseDetail> items;

	public WorkOrderResponse() {
		super();
	}

	public WorkOrderResponse(Integer id, String requestNumber, Date requestDate, String remark,
			String addedBy, Date addedOn, String store, String contractType, String status, String approvalStatus) {
		super();
		this.id = id;
		this.requestNumber = requestNumber;
		this.requestDate = requestDate;
		this.remark = remark;
		this.addedBy = addedBy;
		this.addedOn = addedOn;
		this.store = store;
		this.contractType = contractType;
		this.status = status;
		this.approvalStatus = approvalStatus;
	}
	
	public WorkOrderResponse(Integer id, String requestNumber, Date requestDate, String remark, String addedBy,
			Date addedOn, Integer storeId, String store, String contractType, Integer statusId, String status,
			Integer approvalStatusId, String approvalStatus) {
		super();
		this.id = id;
		this.requestNumber = requestNumber;
		this.requestDate = requestDate;
		this.remark = remark;
		this.addedBy = addedBy;
		this.addedOn = addedOn;
		this.storeId = storeId;
		this.store = store;
		this.contractType = contractType;
		this.statusId = statusId;
		this.status = status;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<WorkOrderResponseDetail> getItems() {
		return items;
	}

	public void setItems(List<WorkOrderResponseDetail> items) {
		this.items = items;
	}
	
	
	public Boolean getIsQuotation() {
		return isQuotation;
	}

	public void setIsQuotation(Boolean isQuotation) {
		this.isQuotation = isQuotation;
	}

	public Boolean getIsCreation() {
		return isCreation;
	}

	public void setIsCreation(Boolean isCreation) {
		this.isCreation = isCreation;
	}

	public void addItems(WorkOrderResponseDetail item) {
		if (this.items == null)
			this.items = new ArrayList<WorkOrderResponseDetail>();
		
		this.items.add(item);
	}
}
