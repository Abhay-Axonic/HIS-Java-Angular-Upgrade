package com.param.procurement.workOrder.dto.workOrderQuotationVendor;

import java.util.Date;
import java.util.List;

import com.param.procurement.quotation.dto.enqSendToVendor.MultipleVendors;
import com.param.service.global.model.CookieObjectDto;

public class WorkOrderQVRequest extends CookieObjectDto {

	private Integer id;

	private Date fromDate;

	private Date toDate;

	public List<MultipleVendors> getVenders() {
		return venders;
	}

	public void setVenders(List<MultipleVendors> venders) {
		this.venders = venders;
	}

	private Integer storeId;

	private Integer vendorId;
	private List<MultipleVendors> venders;
	private String vendorName;

	private String qrNumber;

	private String vqrNumber;

	private Integer statusId;

	private Integer approvalStatusId;

	private Date dueDate;

	private Integer woQuotationRequestId;

	private String remark;

	private List<WorkOrderQVDetailRequest> items;
	
	private List<Integer> qvIds;
	
	private List<Integer> qvDetailIds;
	
	private Integer cancelReasonId;
	
	private String cancelNote;
	
	private Integer contractTypeId;

	public WorkOrderQVRequest() {
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public String getVqrNumber() {
		return vqrNumber;
	}

	public void setVqrNumber(String vqrNumber) {
		this.vqrNumber = vqrNumber;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getWoQuotationRequestId() {
		return woQuotationRequestId;
	}

	public void setWoQuotationRequestId(Integer woQuotationRequestId) {
		this.woQuotationRequestId = woQuotationRequestId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<WorkOrderQVDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<WorkOrderQVDetailRequest> items) {
		this.items = items;
	}

	public List<Integer> getQvIds() {
		return qvIds;
	}

	public void setQvIds(List<Integer> qvIds) {
		this.qvIds = qvIds;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<Integer> getQvDetailIds() {
		return qvDetailIds;
	}

	public void setQvDetailIds(List<Integer> qvDetailIds) {
		this.qvDetailIds = qvDetailIds;
	}

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}
	
	
	
}
