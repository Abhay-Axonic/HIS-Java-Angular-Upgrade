package com.param.procurement.quotation.dto.enqSendToVendor;

import java.util.Date;
import java.util.List;

import com.param.entity.model.master.User;
import com.param.service.global.model.CookieObjectDto;

public class EnqSendToVendorReqDto extends CookieObjectDto{
	
    private Integer id;
	
	private Integer statusId;
	
	private Integer assetTypeId;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Date dueDate;

	private Integer storeId;

	private Integer approvalStatusId;
	
	private User approvedBy;

	private Date approvedDate;

	private String qrNumber;
	
	private String vqrNumber;
	
	private Integer vendorId;
	
	private String vendorName;

	private String remark;

	private Integer requestTypeId;

	private String cancelNote;
	
	private Integer cancelReasonId;
	
	private Integer qrId;
	
	private Boolean getQr;
	
	private List<Integer> vqrId ;
	
	private List<Integer> itemIds ;
	
	List<EnqSendToVentorReqDetailDto> items;

	public EnqSendToVendorReqDto() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public List<EnqSendToVentorReqDetailDto> getItems() {
		return items;
	}

	public void setItems(List<EnqSendToVentorReqDetailDto> items) {
		this.items = items;
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

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}

	public List<Integer> getVqrId() {
		return vqrId;
	}

	public void setVqrId(List<Integer> vqrId) {
		this.vqrId = vqrId;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public Boolean getGetQr() {
		return getQr;
	}

	public void setGetQr(Boolean getQr) {
		this.getQr = getQr;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	
	
	
		
}
