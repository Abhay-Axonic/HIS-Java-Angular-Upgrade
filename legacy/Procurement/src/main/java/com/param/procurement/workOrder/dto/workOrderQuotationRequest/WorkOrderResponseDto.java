package com.param.procurement.workOrder.dto.workOrderQuotationRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkOrderResponseDto {

	private Integer id;

	private String requestNumber;

	private Date requestDate;

	private String remark;

	private String addedBy;

	private Date addedOn;

	private Integer storeId;

	private String store;

	private String contractType;

	private List<WorkOrderDetailResponseDto> items;

	public WorkOrderResponseDto() {
		super();
	}
	
	public WorkOrderResponseDto(Integer id, String requestNumber, Date requestDate, String remark, String addedBy,
			Date addedOn, Integer storeId, String store, String contractType) {
		super();
		this.id = id;
		this.requestNumber = requestNumber;
		this.requestDate = requestDate;
		this.remark = remark;
		this.addedBy = addedBy;
		this.addedOn = addedOn;
		this.storeId = storeId;
		this.store = store;
		this.contractType = contractType;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
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

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
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

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public List<WorkOrderDetailResponseDto> getItems() {
		return items;
	}

	public void setItems(List<WorkOrderDetailResponseDto> items) {
		this.items = items;
	}
	
	public void addItems(WorkOrderDetailResponseDto item) {
		if (this.items == null)
			this.items = new ArrayList<WorkOrderDetailResponseDto>();

		this.items.add(item);
	}

}
