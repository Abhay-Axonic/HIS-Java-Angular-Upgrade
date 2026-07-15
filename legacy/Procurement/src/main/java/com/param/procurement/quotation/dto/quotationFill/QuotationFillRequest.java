package com.param.procurement.quotation.dto.quotationFill;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class QuotationFillRequest  extends CookieObjectDto{
	
	private Integer id;
	
	private Date fromDate;
	
	private Date toDate;

	private Integer assetTypeId;

	private Integer statusId;

	private Integer storeId;

	private Integer approvalStatusId;

	private Integer approvedBy;

	private Date approvedDate;

	private Integer qrId;

	private Integer vqrId;
	
	private String vqrNumber;

	private String cancelNote;

	private String refNumber;

	private Date refDate;

	private String remark;
	
	private Integer cancelReasonId;

	private Integer vendorId;
	
	private String vendorName;
	
	private Boolean getVqr;
	
	private List<Integer> quotationFillId;

	private List<QuotationFillDetailRequestDto> items;
	
	private List<Integer> itemIds;
	

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

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
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

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}

	public Integer getVqrId() {
		return vqrId;
	}

	public void setVqrId(Integer vqrId) {
		this.vqrId = vqrId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public Date getRefDate() {
		return refDate;
	}

	public void setRefDate(Date refDate) {
		this.refDate = refDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public List<QuotationFillDetailRequestDto> getItems() {
		return items;
	}

	public void setItems(List<QuotationFillDetailRequestDto> items) {
		this.items = items;
	}

	public List<Integer> getQuotationFillId() {
		return quotationFillId;
	}

	public void setQuotationFillId(List<Integer> quotationFillId) {
		this.quotationFillId = quotationFillId;
	}

	public String getVqrNumber() {
		return vqrNumber;
	}

	public void setVqrNumber(String vqrNumber) {
		this.vqrNumber = vqrNumber;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Boolean getGetVqr() {
		return getVqr;
	}

	public void setGetVqr(Boolean getVqr) {
		this.getVqr = getVqr;
	}
	
	
}
