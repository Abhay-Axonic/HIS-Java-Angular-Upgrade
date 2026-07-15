package com.param.global.procedure.dto;

import com.param.global.billing.dto.CommonBillingItemsDto;

public class OTCompletionItemRequest extends CommonBillingItemsDto {


	protected Integer usedQuantity;
	protected double mrp;
	protected Integer issueId;
	// Field Extra Added for OT Completion
	protected Integer remainingQty;
	private Integer issueDetailsBatchMapperId;
	
	public Integer getUsedQuantity() {
		return usedQuantity;
	}
	public void setUsedQuantity(Integer usedQuantity) {
		this.usedQuantity = usedQuantity;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	public Integer getIssueId() {
		return issueId;
	}
	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	public Integer getRemainingQty() {
		return remainingQty;
	}
	public void setRemainingQty(Integer remainingQty) {
		this.remainingQty = remainingQty;
	}
	public Integer getIssueDetailsBatchMapperId() {
		return issueDetailsBatchMapperId;
	}
	public void setIssueDetailsBatchMapperId(Integer issueDetailsBatchMapperId) {
		this.issueDetailsBatchMapperId = issueDetailsBatchMapperId;
	}
	
	
	
	

}
