package com.param.global.pharmacy.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.billing.dto.PharmacyBillCommonDto;

@JsonInclude(Include.NON_NULL)
public class PendingIssuesSave extends PharmacyBillCommonDto {

	private static final long serialVersionUID = 1L;

	private Integer statusId;

	private Integer orderId;

	private Integer id;

	private Integer createdBy;

	private List<ItemRequest> items;

	private boolean isFromNarcotics;
	
	private boolean isFromPendingIssue;
    private boolean atCopToSave;
    private boolean isConsolidatedFlag;
	
	public boolean isFromPendingIssue() {
		return isFromPendingIssue;
	}

	public void setFromPendingIssue(boolean isFromPendingIssue) {
		this.isFromPendingIssue = isFromPendingIssue;
	}

	public boolean isFromNarcotics() {
		return isFromNarcotics;
	}

	public void setFromNarcotics(boolean isFromNarcotics) {
		this.isFromNarcotics = isFromNarcotics;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public boolean getAtCopToSave() {
		return atCopToSave;
	}

	public void setAtCopToSave(boolean atCopToSave) {
		this.atCopToSave = atCopToSave;
	}

	public boolean getIsConsolidatedFlag() {
		return isConsolidatedFlag;
	}

	public void setIsConsolidatedFlag(boolean isConsolidatedFlag) {
		this.isConsolidatedFlag = isConsolidatedFlag;
	}


}