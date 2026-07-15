package com.param.procurement.workOrder.dto.workOrderQuotationVendor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkOrderQVResponse {

	private Integer id;

	private String qrNo;

	private Date qrDate;

	private String vqrNo;

	private Date vqrDate;

	private String storeName;
	
	private Integer storeId;

	private String vendorName;
	
	private Integer vendorId;

	private String contractType;
	
	private Integer contractTypeId;

	private String qrAgainst;
	
	private Integer qrAgainstId;

	private Date dueDate;

	private String remark;

	private String addedBy;
	
	private String approvedBy;

	private String status;
	
	private Integer statusId; 

	private String approvalStatus;
	
	private Integer approvalStatusId;
	
	private List<WorkOrderQVDetailResponse> items;

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public WorkOrderQVResponse() {
		super();
	}

	public WorkOrderQVResponse(Integer id, String qrNo, Date qrDate, String vqrNo, Date vqrDate, String storeName,
			String vendorName, String contractType, String qrAgainst, Date dueDate, String remark, String addedBy,
			String status, String approvalStatus) {
		super();
		this.id = id;
		this.qrNo = qrNo;
		this.qrDate = qrDate;
		this.vqrNo = vqrNo;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.vendorName = vendorName;
		this.contractType = contractType;
		this.qrAgainst = qrAgainst;
		this.dueDate = dueDate;
		this.remark = remark;
		this.addedBy = addedBy;
		this.status = status;
		this.approvalStatus = approvalStatus;
	}
	/*.......*/
	public WorkOrderQVResponse(Integer id, String qrNo, Date qrDate, String vqrNo, Date vqrDate, String storeName,
			String vendorName,String contractType,	String qrAgainst, Date dueDate, String remark, String addedBy,String status, String approvalStatus, String approvedBy ) {
		this(id, qrNo, qrDate, vqrNo,  vqrDate,  storeName, vendorName, contractType, qrAgainst, dueDate,  remark, addedBy , status, approvalStatus);
			this.approvedBy = approvedBy;
	}
	
	public WorkOrderQVResponse(Integer id, String qrNo, Date qrDate, String vqrNo, Date vqrDate, String storeName,
			Integer storeId, String vendorName, Integer vendorId, String contractType, Integer contractTypeId,
			String qrAgainst, Integer qrAgainstId, Date dueDate, String remark, String status, Integer statusId, String approvalStatus,
			Integer approvalStatusId,String addedBy) {
		super();
		this.id = id;
		this.qrNo = qrNo;
		this.qrDate = qrDate;
		this.vqrNo = vqrNo;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.storeId = storeId;
		this.vendorName = vendorName;
		this.vendorId = vendorId;
		this.contractType = contractType;
		this.contractTypeId = contractTypeId;
		this.qrAgainst = qrAgainst;
		this.qrAgainstId = qrAgainstId;
		this.dueDate = dueDate;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
		this.addedBy=addedBy;
		
		
	}
	public WorkOrderQVResponse(Integer id, String qrNo, Date qrDate, String vqrNo, Date vqrDate, String storeName,
			Integer storeId, String vendorName, Integer vendorId, String contractType, Integer contractTypeId,
			String qrAgainst, Integer qrAgainstId, Date dueDate, String remark, String status, Integer statusId, String approvalStatus,
			Integer approvalStatusId) {
		super();
		this.id = id;
		this.qrNo = qrNo;
		this.qrDate = qrDate;
		this.vqrNo = vqrNo;
		this.vqrDate = vqrDate;
		this.storeName = storeName;
		this.storeId = storeId;
		this.vendorName = vendorName;
		this.vendorId = vendorId;
		this.contractType = contractType;
		this.contractTypeId = contractTypeId;
		this.qrAgainst = qrAgainst;
		this.qrAgainstId = qrAgainstId;
		this.dueDate = dueDate;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
		
		
		
	}
	/*..........*/
	 	
	public WorkOrderQVResponse(Integer id, String qrNo, Date qrDate, String vqrNo, Date vqrDate, String storeName,
			Integer storeId, String vendorName, Integer vendorId, String contractType, Integer contractTypeId,
			String qrAgainst, Integer qrAgainstId, Date dueDate, String remark, String status, Integer statusId, String approvalStatus,
			Integer approvalStatusId,String addedBy,String approvedBy) {
		this(id, qrNo, qrDate, vqrNo,  vqrDate,  storeName, storeId,  vendorName,  vendorId,  contractType,  contractTypeId,
				 qrAgainst, qrAgainstId, dueDate,  remark,  status,  statusId,  approvalStatus, approvalStatusId);
			this.addedBy = addedBy;
			this.approvedBy = approvedBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getQrAgainst() {
		return qrAgainst;
	}

	public void setQrAgainst(String qrAgainst) {
		this.qrAgainst = qrAgainst;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<WorkOrderQVDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<WorkOrderQVDetailResponse> items) {
		this.items = items;
	}

	public Integer getQrAgainstId() {
		return qrAgainstId;
	}

	public void setQrAgainstId(Integer qrAgainstId) {
		this.qrAgainstId = qrAgainstId;
	}
	
	public void addItems(WorkOrderQVDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<WorkOrderQVDetailResponse>();
		
		this.items.add(item);
	}
	
}
