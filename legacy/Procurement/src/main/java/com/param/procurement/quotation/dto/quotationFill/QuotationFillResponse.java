package com.param.procurement.quotation.dto.quotationFill;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorDetailRespDto;

public class QuotationFillResponse {

	private Integer id;

	private Integer statusId;

	private String status;

	private Integer assetTypeId;

	private String assetType;

	private Integer storeId;

	private String storeName;

	private Integer approvalStatusId;

	private String approvalStatus;

	private String createdBy;

	private Integer qrId;

	private String qrNumber;

	private Date qrDate;

	private Integer vqrId;

	private String vqrNumber;

	private Date vqrDate;

	private String refNumber;

	private Date refDate;

	private String remark;

	private Integer vendorId;

	private String vendorName;

	private List<QuotationFillDetailResponse> items;
	
	
	public QuotationFillResponse(Integer id, Integer statusId, String status, Integer assetTypeId, String assetType,
			Integer storeId, String storeName, Integer approvalStatusId, String approvalStatus, String createdBy,
			Integer qrId, String qrNumber, Date qrDate, Integer vqrId, String vqrNumber, Date vqrDate, String refNumber,
			Date refDate, String remark, Integer vendorId, String vendorName) {
		super();
		this.id = id;
		this.statusId = statusId;
		this.status = status;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.storeId = storeId;
		this.storeName = storeName;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
		this.createdBy = createdBy;
		this.qrId = qrId;
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.vqrId = vqrId;
		this.vqrNumber = vqrNumber;
		this.vqrDate = vqrDate;
		this.refNumber = refNumber;
		this.refDate = refDate;
		this.remark = remark;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public Integer getVqrId() {
		return vqrId;
	}

	public void setVqrId(Integer vqrId) {
		this.vqrId = vqrId;
	}

	public String getVqrNumber() {
		return vqrNumber;
	}

	public void setVqrNumber(String vqrNumber) {
		this.vqrNumber = vqrNumber;
	}

	public Date getVqrDate() {
		return vqrDate;
	}

	public void setVqrDate(Date vqrDate) {
		this.vqrDate = vqrDate;
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

	public List<QuotationFillDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<QuotationFillDetailResponse> items) {
		this.items = items;
	}
	
	public void addItems(QuotationFillDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<QuotationFillDetailResponse>();
		
		this.items.add(item);
	}
}
