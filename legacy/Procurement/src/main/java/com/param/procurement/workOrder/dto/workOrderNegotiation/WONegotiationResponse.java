package com.param.procurement.workOrder.dto.workOrderNegotiation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuotationEntryDetailResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WONegotiationResponse extends WONegotiationRequest{
	
	private String qrNumber;

	private Date qrDate;

	private String vqrNumber;

	private Date vqrDate;
	
	private Integer storeId;

	private String storeName;

	private String vendorName;

	private String contractType;

	private Integer contractTypeId;

	private String qrAgainst;

	private String addedBy;

	private String status;

	private String approvalStatus;
	
	private String refNumber;
	
	private Date refDate;
	
	private Integer vendorId;
	
	private List<WONegotiationDetailResponse> detail;

	private String remark;

	public WONegotiationResponse() {
		super();
	}

	public WONegotiationResponse(Integer id, Integer statusId, String qrNumber, Date qrDate, String vqrNumber, Date vqrDate, String storeName,
			String vendorName, String contractType, Integer contractTypeId, String qrAgainst, String addedBy,
			String status) {
		super(id, statusId);
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.vqrNumber = vqrNumber;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.contractType = contractType;
		this.contractTypeId = contractTypeId;
		this.qrAgainst = qrAgainst;
		this.addedBy = addedBy;
		this.status = status;
		
	}
	
	public WONegotiationResponse(Integer id,Integer statusId,String qrNumber, Date qrDate, String vqrNumber, Date vqrDate, Integer storeId, String storeName,
			String vendorName, String contractType, Integer contractTypeId, String qrAgainst, String addedBy,
			String status,String refNumber,Date refDate,Integer vendorId) {
		super(id,statusId);
		this.qrNumber = qrNumber;
		this.qrDate = qrDate;
		this.vqrNumber = vqrNumber;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.contractType = contractType;
		this.contractTypeId = contractTypeId;
		this.qrAgainst = qrAgainst;
		this.addedBy = addedBy;
		this.status = status;
		this.refNumber = refNumber;
		this.refDate = refDate;
		this.vendorId=vendorId;
	}



	public WONegotiationResponse(Integer id, Integer statusId,String remark, String qrNumber, Date qrDate, String vqrNumber, Date vqrDate, String storeName,
			String vendorName, String contractType, Integer contractTypeId, String qrAgainst, String addedBy,
			String status) {
		this( id,  statusId,  qrNumber,qrDate,  vqrNumber,  vqrDate,  storeName,
			 vendorName,  contractType,  contractTypeId,  qrAgainst, addedBy,
			 status);
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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

	public List<WONegotiationDetailResponse> getDetail() {
		return detail;
	}

	public void setDetail(List<WONegotiationDetailResponse> detail) {
		this.detail = detail;
	}

	
	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public void addItems(WONegotiationDetailResponse item) {
		if (this.detail == null)
			this.detail = new ArrayList<WONegotiationDetailResponse>();
		
		this.detail.add(item);
	}
	
	
}
