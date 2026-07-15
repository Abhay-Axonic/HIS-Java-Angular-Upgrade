package com.param.procurement.workOrder.dto.workOrderQuotationEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQRDetailResponse;

public class WoQuotationEntryResponse extends WoQuoatationEntryRequest {

	private String qrNo;

	private Date qrDate;

	private String vqrNo;

	private Date vqrDate;

	private String storeName;

	private String vendorName;

	private String contractType;

	private Integer contractTypeId;

	private String qrAgainst;

	private String addedBy;

	private String status;
	
	private String remark;

	private Integer id;
	
	private Integer storeId;
	
	private Integer vendorId;
	
	private Integer approvalStatusId;
	
	private Date referenceDate;
	
	private String referenceNo;
	
	private Integer statusId;

	private Integer vendorQuotationId;
	
	private Integer qrAgainstId;

	private Date fromDate;
		
	private Date toDate;

	private String approvalStatus;

	private List<WoQuotationEntryDetailResponse> detail;

	public WoQuotationEntryResponse() {
		super();
	}

	public WoQuotationEntryResponse(Integer id, Integer storeId, Integer vendorId, Integer approvalStatusId,
			Date referenceDate, String referenceNo, String remark, Integer statusId, Integer vendorQuotationId,
			Integer qrAgainstId, String qrNo, Date qrDate, String vqrNo, Date vqrDate, String storeName,
			String vendorName, String contractType, Integer contractTypeId, String qrAgainst, String addedBy,
			String status, String approvalStatus,Date fromDate,Date toDate) {
	
		this.id=id;
		this.storeId = storeId;
		this.vendorId = vendorId;
		this.approvalStatusId = approvalStatusId;
		this.referenceDate = referenceDate;
		this.referenceNo = referenceNo;
		this.statusId = statusId;
		this.vendorQuotationId = vendorQuotationId;
		this.qrAgainstId = qrAgainstId;
		this.qrNo = qrNo;
		this.qrDate = qrDate;
		this.vqrNo = vqrNo;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.contractType = contractType;
		this.qrAgainst = qrAgainst;
		this.addedBy = addedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.remark=remark;
		this.fromDate=fromDate;
		this.toDate=toDate;
	}

	public WoQuotationEntryResponse(Integer id, Integer storeId, Integer vendorId, Integer approvalStatusId,
			Date referenceDate, String referenceNo, String remark, Integer statusId, Integer vendorQuotationId,
			Integer qrAgainstId, String qrNo, Date qrDate, String vqrNo, Date vqrDate, String storeName,
			String vendorName, String contractType, Integer contractTypeId, String qrAgainst, String addedBy,
			String status, String approvalStatus) {
		
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
		this.qrNo = qrNo;
		this.qrDate = qrDate;
		this.vqrNo = vqrNo;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.contractType = contractType;
		this.contractTypeId = contractTypeId;
		this.qrAgainst = qrAgainst;
		this.addedBy = addedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
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
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getQrNo() {
		return qrNo;
	}

	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getVqrNo() {
		return vqrNo;
	}

	public void setVqrNo(String vqrNo) {
		this.vqrNo = vqrNo;
	}

	public Date getVqrDate() {
		return vqrDate;
	}

	public void setVqrDate(Date vqrDate) {
		this.vqrDate = vqrDate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public String getQrAgainst() {
		return qrAgainst;
	}

	public void setQrAgainst(String qrAgainst) {
		this.qrAgainst = qrAgainst;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<WoQuotationEntryDetailResponse> getDetail() {
		return detail;
	}

	public void setDetail(List<WoQuotationEntryDetailResponse> detail) {
		this.detail = detail;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	public void addItems(WoQuotationEntryDetailResponse item) {
		if (this.detail == null)
			this.detail = new ArrayList<WoQuotationEntryDetailResponse>();
		
		this.detail.add(item);
	}

}
