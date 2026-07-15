package com.param.procurement.quotation.dto.quotationRequest;

import java.util.Date;
import java.util.List;

import com.param.entity.model.master.User;
import com.param.service.global.model.CookieObjectDto;

public class QuotationRequestDto extends CookieObjectDto {
	
	private Integer id;
	
	private Integer statusId;
	
	private Date fromDate;
	
	private Date toDate;

	private Integer storeId;

	private Integer approvalStatusId;
	
	private User approvedBy;

	private Date approvedDate;

	private Integer assetTypeId;

	private String qrNo;

	private String remark;

	private Integer requestTypeId;

	private Boolean sendTechnicalApproval;

	private Boolean sendVendorSelection;
	
	private String cancelNote;
	
	private Integer cancelReasonId;
	
	private List<Integer> qrId ;
	
	List<QuotationRequestDetailDto> items;

	public QuotationRequestDto() {
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

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getQrNo() {
		return qrNo;
	}

	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
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

	public Boolean getSendTechnicalApproval() {
		return sendTechnicalApproval;
	}

	public void setSendTechnicalApproval(Boolean sendTechnicalApproval) {
		this.sendTechnicalApproval = sendTechnicalApproval;
	}

	public Boolean getSendVendorSelection() {
		return sendVendorSelection;
	}

	public void setSendVendorSelection(Boolean sendVendorSelection) {
		this.sendVendorSelection = sendVendorSelection;
	}

	public List<QuotationRequestDetailDto> getItems() {
		return items;
	}

	public void setItems(List<QuotationRequestDetailDto> items) {
		this.items = items;
	}

	public List<Integer> getQrId() {
		return qrId;
	}

	public void setQrId(List<Integer> qrId) {
		this.qrId = qrId;
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
}
