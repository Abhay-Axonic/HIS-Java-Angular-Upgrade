package com.param.procurement.workOrder.dto.workOrderQuotationEntry;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class WoQuoatationEntryRequest extends CookieObjectDto {

	private Integer id;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Integer storeId;
	
	private Integer vendorId;
	
	private Integer approvalStatusId;

	private String cancelNote;

	private Integer cancelReasonId;

	private Date referenceDate;

	private String referenceNo;

	private String remark;

	private Integer statusId;

	private Integer vendorQuotationId;
	
	private Integer qrAgainstId;
	
	private List<WoQuotationEntryDetailRequest> items;
	
	private List<Integer> qEntryIds;
	
	private String workOrderQRNo;
	
	private String workOrderVQRNo;
	
	


	public String getWorkOrderQRNo() {
		return workOrderQRNo;
	}

	public void setWorkOrderQRNo(String workOrderQRNo) {
		this.workOrderQRNo = workOrderQRNo;
	}

	public String getWorkOrderVQRNo() {
		return workOrderVQRNo;
	}

	public void setWorkOrderVQRNo(String workOrderVQRNo) {
		this.workOrderVQRNo = workOrderVQRNo;
	}

	public WoQuoatationEntryRequest() {
		super();
	}
	
	public WoQuoatationEntryRequest(Integer id) {
		this.id = id;
	}
	
	public WoQuoatationEntryRequest(Integer id, Integer storeId, Integer vendorId, Integer approvalStatusId,
			Date referenceDate, String referenceNo, String remark, Integer statusId, Integer vendorQuotationId,
			Integer qrAgainstId) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.vendorId = vendorId;
		this.approvalStatusId = approvalStatusId;
		this.referenceDate = referenceDate;
		this.referenceNo = referenceNo;
		this.remark = remark;
		this.statusId = statusId;
		this.vendorQuotationId = vendorQuotationId;
		this.qrAgainstId = qrAgainstId;
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

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
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

	public Integer getVendorQuotationId() {
		return vendorQuotationId;
	}

	public void setVendorQuotationId(Integer vendorQuotationId) {
		this.vendorQuotationId = vendorQuotationId;
	}
	
	public Integer getQrAgainstId() {
		return qrAgainstId;
	}

	public void setQrAgainstId(Integer qrAgainstId) {
		this.qrAgainstId = qrAgainstId;
	}

	public List<WoQuotationEntryDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<WoQuotationEntryDetailRequest> items) {
		this.items = items;
	}

	public List<Integer> getqEntryIds() {
		return qEntryIds;
	}

	public void setqEntryIds(List<Integer> qEntryIds) {
		this.qEntryIds = qEntryIds;
	}
	
}
