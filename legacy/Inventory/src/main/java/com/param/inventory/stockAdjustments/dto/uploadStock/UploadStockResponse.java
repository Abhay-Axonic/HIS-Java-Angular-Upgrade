package com.param.inventory.stockAdjustments.dto.uploadStock;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UploadStockResponse {

	private Integer id;

	private String uploadNo;

	private Date uploadDate;

	private String requestNo;

	// private String againstRequestNo;

	private Date requestDate;

	private Date startDateTime;

	private Date stopDateTime;

	private Date requestStartDate;

	private Date requestStopDate;

	private String requestedBy;

	private Integer uploadedById;

	private String uploadedBy;

	private String store;

	private Integer storeId;

	private String status;

	private Integer statusId;

	private String approvalStatus;

	private Integer approvalStatusId;
	
	private String remark;
	
	private Integer AsseteTypeId;

	private List<UploadStockResponseItem> items;

	public UploadStockResponse() {
		super();
	}

	public UploadStockResponse(Integer id, String requestNo, Date requestDate, Date startDateTime, Date stopDateTime,
			String requestedBy, String store, Integer storeId, String status, Integer statusId) {
		super();
		this.id = id;
		this.requestNo = requestNo;
		this.requestDate = requestDate;
		this.startDateTime = startDateTime;
		this.stopDateTime = stopDateTime;
		this.requestedBy = requestedBy;
		this.store = store;
		this.storeId = storeId;
		this.status = status;
		this.statusId = statusId;
	}

	public UploadStockResponse(Integer id, String requestNo, Date requestDate, Date startDateTime, Date stopDateTime,
			String store, Integer storeId, String status, Integer statusId, String uploadedBy) {
		super();
		this.id = id;
		this.requestNo = requestNo;
		this.requestDate = requestDate;
		this.startDateTime = startDateTime;
		this.stopDateTime = stopDateTime;
		this.store = store;
		this.storeId = storeId;
		this.status = status;
		this.statusId = statusId;
		this.uploadedBy = uploadedBy;
	}

	public UploadStockResponse(Integer id, String uploadNo, Date uploadDate, String requestNo, Date requestStartDate,
			Date requestStopDate, String uploadedBy, String store, Integer storeId, String status, Integer statusId,
			String approvalStatus, Integer approvalStatusId) {
		super();
		this.id = id;
		this.uploadNo = uploadNo;
		this.uploadDate = uploadDate;
		this.requestNo = requestNo;
		this.requestStartDate = requestStartDate;
		this.requestStopDate = requestStopDate;
		this.uploadedBy = uploadedBy;
		this.store = store;
		this.storeId = storeId;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*
	 * public String getAgainstRequestNo() { return againstRequestNo; }
	 * 
	 * public void setAgainstRequestNo(String againstRequestNo) {
	 * this.againstRequestNo = againstRequestNo; }
	 */

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

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getStopDateTime() {
		return stopDateTime;
	}

	public void setStopDateTime(Date stopDateTime) {
		this.stopDateTime = stopDateTime;
	}

	public Date getRequestStartDate() {
		return requestStartDate;
	}

	public void setRequestStartDate(Date requestStartDate) {
		this.requestStartDate = requestStartDate;
	}

	public Date getRequestStopDate() {
		return requestStopDate;
	}

	public void setRequestStopDate(Date requestStopDate) {
		this.requestStopDate = requestStopDate;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
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

	public List<UploadStockResponseItem> getItems() {
		return items;
	}

	public void setItems(List<UploadStockResponseItem> items) {
		this.items = items;
	}

	public String getUploadNo() {
		return uploadNo;
	}

	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}

	public Integer getUploadedById() {
		return uploadedById;
	}

	public void setUploadedById(Integer uploadedById) {
		this.uploadedById = uploadedById;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getAsseteTypeId() {
		return AsseteTypeId;
	}

	public void setAsseteTypeId(Integer asseteTypeId) {
		AsseteTypeId = asseteTypeId;
	}

}
