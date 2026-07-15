package com.param.inventory.stockAdjustments.dto.confirmStock;

public class ConfirmStockDetailRequest {
	
	private Integer id;
	
	private Integer statusId;
	
	private Integer rejectQuantity;
	
	private Integer approvalQunatity;
	
	private Integer rejectReasonId;
	
	private String rejectNote;
	
	
	public ConfirmStockDetailRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getApprovalQunatity() {
		return approvalQunatity;
	}

	public void setApprovalQunatity(Integer approvalQunatity) {
		this.approvalQunatity = approvalQunatity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}
	
	

}
