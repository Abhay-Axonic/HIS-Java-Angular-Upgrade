package com.param.inventory.store.dto.materialPicker;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class MaterialPickerRequest extends CookieObjectDto {

	protected Integer id;

	protected Integer pickerDetailsId;

	protected Date fromDate;

	protected Date toDate;

	protected Integer fromStoreId;

	protected Integer toStoreId;

	protected Integer indentId;

	protected String indentNo;

	protected Integer indentTypeId;

	protected Integer statusId;

	protected Integer priorityId;

	protected String priority;

	protected Integer approvalStatusId;

	protected Integer closeReasonId;

	protected String closeNote;

	protected Integer pickerId;

	protected Integer carrierId;

	protected Integer pickerid;

	protected String remark;

	protected Boolean isConsignment;

	protected List<MaterialPickerDetailRequest> items;

	protected String issueNo;

	protected String returnNo;
	
	protected List<Integer> fromStoreIds;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPickerDetailsId() {
		return pickerDetailsId;
	}

	public void setPickerDetailsId(Integer pickerDetailsId) {
		this.pickerDetailsId = pickerDetailsId;
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

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getIndentId() {
		return indentId;
	}

	public void setIndentId(Integer indentId) {
		this.indentId = indentId;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getIndentTypeId() {
		return indentTypeId;
	}

	public void setIndentTypeId(Integer indentTypeId) {
		this.indentTypeId = indentTypeId;
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

	public List<MaterialPickerDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<MaterialPickerDetailRequest> items) {
		this.items = items;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getCloseReasonId() {
		return closeReasonId;
	}

	public void setCloseReasonId(Integer closeReasonId) {
		this.closeReasonId = closeReasonId;
	}

	public String getCloseNote() {
		return closeNote;
	}

	public void setCloseNote(String closeNote) {
		this.closeNote = closeNote;
	}

	public Integer getPickerid() {
		return pickerid;
	}

	public void setPickerid(Integer pickerid) {
		this.pickerid = pickerid;
	}

	public Integer getPickerId() {
		return pickerId;
	}

	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public List<Integer> getFromStoreIds() {
		return fromStoreIds;
	}

	public void setFromStoreIds(List<Integer> fromStoreIds) {
		this.fromStoreIds = fromStoreIds;
	}

}
