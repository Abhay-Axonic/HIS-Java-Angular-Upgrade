package com.param.assetManagement.nonReturnableGatepass.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NonReturnableGatepssResponse {

	protected Integer id;

	protected Integer vendorAuctionMapperId;

	protected Date nonReturnableGatepassDate;

	protected String nonReturnableGatepassNo;

	protected String store;

	protected Integer storeId;

	protected String vendor;

	protected Integer vendorId;

	protected String addedBy;

	protected String status;

	protected Integer statusId;

	protected String approvalStatus;

	protected Integer approvalStatusId;

	protected String requestNo;

	protected Date requestDate;

	protected String vendorName;

	protected String againstRequisition;

	protected Integer approveById;

	protected String approveBy;

	protected Integer auctionRequisitionId;

	protected Integer requestTypeId;

	protected List<NonReturnableGatepssResponseItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getNonReturnableGatepassDate() {
		return nonReturnableGatepassDate;
	}

	public void setNonReturnableGatepassDate(Date nonReturnableGatepassDate) {
		this.nonReturnableGatepassDate = nonReturnableGatepassDate;
	}

	public String getNonReturnableGatepassNo() {
		return nonReturnableGatepassNo;
	}

	public void setNonReturnableGatepassNo(String nonReturnableGatepassNo) {
		this.nonReturnableGatepassNo = nonReturnableGatepassNo;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public List<NonReturnableGatepssResponseItem> getItems() {
		return items;
	}

	public void setItems(List<NonReturnableGatepssResponseItem> items) {
		this.items = items;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getAgainstRequisition() {
		return againstRequisition;
	}

	public void setAgainstRequisition(String againstRequisition) {
		this.againstRequisition = againstRequisition;
	}

	public Integer getApproveById() {
		return approveById;
	}

	public void setApproveById(Integer approveById) {
		this.approveById = approveById;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public Integer getVendorAuctionMapperId() {
		return vendorAuctionMapperId;
	}

	public void setVendorAuctionMapperId(Integer vendorAuctionMapperId) {
		this.vendorAuctionMapperId = vendorAuctionMapperId;
	}

	public Integer getAuctionRequisitionId() {
		return auctionRequisitionId;
	}

	public void setAuctionRequisitionId(Integer auctionRequisitionId) {
		this.auctionRequisitionId = auctionRequisitionId;
	}

	public NonReturnableGatepssResponse() {
		super();
	}

	public NonReturnableGatepssResponse(Integer id, Date nonReturnableGatepassDate, String nonReturnableGatepassNo,
			String store, Integer storeId, String vendor, Integer vendorId, String addedBy, String status,
			Integer statusId, Integer requestTypeId, String approvalStatus, Integer approvalStatusId) {
		super();
		this.id = id;
		this.nonReturnableGatepassDate = nonReturnableGatepassDate;
		this.nonReturnableGatepassNo = nonReturnableGatepassNo;
		this.store = store;
		this.storeId = storeId;
		this.vendor = vendor;
		this.vendorId = vendorId;
		this.addedBy = addedBy;
		this.status = status;
		this.statusId = statusId;
		this.requestTypeId = requestTypeId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
	}

	public NonReturnableGatepssResponse(Integer id, String requestNo, Date requestDate, String againstRequisition,
			Integer approveById, String approveBy) {
		super();
		this.id = id;
		this.requestNo = requestNo;
		this.requestDate = requestDate;
		this.againstRequisition = againstRequisition;
		this.approveById = approveById;
		this.approveBy = approveBy;
	}

	public NonReturnableGatepssResponse(Integer id, Integer vendorId, String vendor, String requestNo, Date requestDate,
			String againstRequisition, Integer approveById, String approveBy, Integer auctionRequisitionId) {
		super();
		this.id = id;
		this.vendorId = vendorId;
		this.vendor = vendor;
		this.requestNo = requestNo;
		this.requestDate = requestDate;
		this.againstRequisition = againstRequisition;
		this.approveById = approveById;
		this.approveBy = approveBy;
		this.auctionRequisitionId = auctionRequisitionId;
	}

	public NonReturnableGatepssResponse(Integer vendorId, String vendor) {
		super();
		this.vendorId = vendorId;
		this.vendor = vendor;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

}