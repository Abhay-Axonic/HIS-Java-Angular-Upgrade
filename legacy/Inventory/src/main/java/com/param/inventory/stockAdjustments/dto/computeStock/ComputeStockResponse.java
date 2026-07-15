package com.param.inventory.stockAdjustments.dto.computeStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;
@JsonInclude(Include.NON_NULL)
public class ComputeStockResponse {
	
	protected Integer id;

	protected String requestNo;

	protected Date requestDate;
	
	protected String uploadNo;

	protected Date requestStartDate;

	protected Date requestStopDate;

	protected Date startDateTime;

	protected Date stopDateTime;

	protected Integer requestedById;

	protected String requestedBy;

	protected Integer storeId;

	protected String store;

	protected Integer statusId;

	protected String status;

	protected Integer uploadedById;

	protected String uploadedBy;

	protected List<ComputeStockItemResponse> items;	
		
	public ComputeStockResponse() {
		super();
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getAgainstRequestNo() {
		return uploadNo;
	}

	public void setAgainstRequestNo(String againstRequestNo) {
		this.uploadNo = againstRequestNo;
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

	public Integer getRequestedById() {
		return requestedById;
	}

	public void setRequestedById(Integer requestedById) {
		this.requestedById = requestedById;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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

	public String getUploadNo() {
		return uploadNo;
	}
	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
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
	
	public Integer getUploadedById() {
		return uploadedById;
	}
	public void setUploadedById(Integer uploadedById) {
		this.uploadedById = uploadedById;
	}
	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	
	public List<ComputeStockItemResponse> getItems() {
		return items;
	}
	public void setItems(List<ComputeStockItemResponse> items) {
		this.items = items;
	}

	public void addItems(ComputeStockItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ComputeStockItemResponse>();

		this.items.add(item);
	}
	
	public ComputeStockResponse(Integer id, Integer storeId, String storeName, Date startDateTime, Date stopDateTime,
			Integer userId, String userName, String status, Integer statusId) {
		
		this.id = id;
		this.storeId = storeId;
		this.store = storeName;
		this.startDateTime = startDateTime;
		this.stopDateTime = stopDateTime;
		this.uploadedById = userId;
		this.uploadedBy = userName;
		this.status = status;
		this.statusId = statusId;	
	}	
		
}
