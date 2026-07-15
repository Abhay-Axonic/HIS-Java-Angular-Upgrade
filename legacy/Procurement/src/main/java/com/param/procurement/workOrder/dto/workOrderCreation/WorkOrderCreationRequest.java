package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class WorkOrderCreationRequest extends CookieObjectDto{
	
	
	private Integer id;
	
	private Date fromDate;
	
	private Date toDate;

	private Integer approvalStatusId;

	private String cancelNote;

	private Integer cancelReasonId;

	private Double netAmount;

	private String remark;
	
	private Integer contractTypeId;

	private Integer requestTypeId;

	private Integer statusId;

	private Integer storeId;

	private Double totalAmount;

	private Double totalDiscount;

	private Double totalTax;

	private Integer vendorId;

	private Integer workOrderRequestId;

	private String workOrderNo;

	private Integer workOrderQuotationId;
	
	private List<Integer> woCreationIds;
	
	private List<WorkOrderCreationDetailRequest> items;

	public WorkOrderCreationRequest() {
		super();
	}
	
	
	public WorkOrderCreationRequest(Integer id, Integer approvalStatusId, Double netAmount, String remark,
			Integer contractTypeId, Integer requestTypeId, Integer statusId, Integer storeId, Double totalAmount,
			Double totalDiscount, Double totalTax, Integer vendorId, Integer workOrderRequestId, String workOrderNo,
			Integer workOrderQuotationId) {
		super();
		this.id = id;
		this.approvalStatusId = approvalStatusId;
		this.netAmount = netAmount;
		this.remark = remark;
		this.contractTypeId = contractTypeId;
		this.requestTypeId = requestTypeId;
		this.statusId = statusId;
		this.storeId = storeId;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalTax = totalTax;
		this.vendorId = vendorId;
		this.workOrderRequestId = workOrderRequestId;
		this.workOrderNo = workOrderNo;
		this.workOrderQuotationId = workOrderQuotationId;
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

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getWorkOrderRequestId() {
		return workOrderRequestId;
	}

	public void setWorkOrderRequestId(Integer workOrderRequestId) {
		this.workOrderRequestId = workOrderRequestId;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public Integer getWorkOrderQuotationId() {
		return workOrderQuotationId;
	}

	public void setWorkOrderQuotationId(Integer workOrderQuotationId) {
		this.workOrderQuotationId = workOrderQuotationId;
	}

	public List<WorkOrderCreationDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<WorkOrderCreationDetailRequest> items) {
		this.items = items;
	}


	public List<Integer> getWoCreationIds() {
		return woCreationIds;
	}


	public void setWoCreationIds(List<Integer> woCreationIds) {
		this.woCreationIds = woCreationIds;
	}
	
	

}
