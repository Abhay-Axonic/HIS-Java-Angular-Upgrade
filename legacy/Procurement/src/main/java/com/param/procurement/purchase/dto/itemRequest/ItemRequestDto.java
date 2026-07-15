package com.param.procurement.purchase.dto.itemRequest;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class ItemRequestDto extends CookieObjectDto {
	
    private Integer id;
	
	private Integer approvalStatusId;
	
	private Integer assetTypeId;
	
	private String cancelNote;
	
	private Integer cancelReasonId;
	
	private String requestNumber;
	
	private Integer requestTypeId;
	
	private Integer statusId;
	
    private Date fromDate;
	
	private Date toDate;
	
	List<ItemRequestDetailDto> items;
	
	private List<Integer> itemRequestId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public List<ItemRequestDetailDto> getItems() {
		return items;
	}

	public void setItems(List<ItemRequestDetailDto> items) {
		this.items = items;
	}

	public List<Integer> getItemRequestId() {
		return itemRequestId;
	}

	public void setItemRequestId(List<Integer> itemRequestId) {
		this.itemRequestId = itemRequestId;
	}
	
	

}
