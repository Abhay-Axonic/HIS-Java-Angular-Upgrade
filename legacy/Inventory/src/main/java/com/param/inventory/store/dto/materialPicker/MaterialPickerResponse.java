package com.param.inventory.store.dto.materialPicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.global.PickerResponse;

@JsonInclude(Include.NON_NULL)
public class MaterialPickerResponse {

	protected Integer id;

	protected Integer pickerId;

	protected Date indentDate;

	protected String indentNo;
	
	protected Date issueDate;
	
	protected String issueNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected Integer priorityId;

	protected String priority;

	protected String indentType;

	protected Boolean isConsignment;

	protected String remark;

	protected String status;
	
	protected String approvalStatus;

	protected List<PickerResponse> pickers;
	
	protected List<PickerResponse> carriers;

	protected List<MaterialPickerDetailResponse> items;
	
	protected String pickerName;

	// protected List<ItemResponse> items;

	public MaterialPickerResponse() {
		super();
	}

	public MaterialPickerResponse(Integer id, Date indentDate, String indentNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String priority, String indentType, Boolean isConsignment, String remark,
			String status) {
		super();
		this.id = id;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.status = status;
	}

	public MaterialPickerResponse(Integer id, Integer pickerId,Date indentDate, String indentNo, Integer fromStoreId, String fromStore,
								  Integer toStoreId, String toStore, String priority, String indentType, Boolean isConsignment, String remark,
								  String status) {
		super();
		this.id = id;
		this.pickerId=pickerId;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.fromStoreId=fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId=toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.status = status;
	}
	
	public MaterialPickerResponse(Integer id, String indentNo, Date issueDate, String issueNo, Integer fromStoreId,
			String fromStore, Integer toStoreId, String toStore, String priority, String indentType,
			Boolean isConsignment, String remark, String status) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.issueDate = issueDate;
		this.issueNo = issueNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.status = status;
	}

	public MaterialPickerResponse(Integer id, Date indentDate, String indentNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String priority, String indentType, Boolean isConsignment,
			List<PickerResponse> pickers) {
		super();
		this.id = id;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
		this.pickers = pickers;
	}
	
	public MaterialPickerResponse(Integer id, Date indentDate, String indentNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String priority, String indentType, Boolean isConsignment) {
		super();
		this.id = id;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.priority = priority;
		this.indentType = indentType;
		this.isConsignment = isConsignment;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public String getFromStore() {
		return fromStore;
	}

	public void setFromStore(String fromStore) {
		this.fromStore = fromStore;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void addItems(MaterialPickerDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<MaterialPickerDetailResponse>();

		this.items.add(item);
	}

	public List<PickerResponse> getPickers() {
		return pickers;
	}

	public void setPickers(List<PickerResponse> pickers) {
		this.pickers = pickers;
	}

	public List<MaterialPickerDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<MaterialPickerDetailResponse> items) {
		this.items = items;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public List<PickerResponse> getCarriers() {
		return carriers;
	}

	public void setCarriers(List<PickerResponse> list) {
		this.carriers = list;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getPickerName() {
		return pickerName;
	}

	public void setPickerName(String pickerName) {
		this.pickerName = pickerName;
	}

	public Integer getPickerId() {
		return pickerId;
	}

	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
	}
}