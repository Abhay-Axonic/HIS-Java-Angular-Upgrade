package com.param.procurement.purchase.purchaseOrder.dto;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class PurchaseOrderStatusDto extends CookieObjectDto{
	
    private List<Integer> poIdList;
    
    private Integer poId;
	
	private Integer statusId;
	
	private Integer approvalStatusId;
	
	private Integer rejectReasonId;

	private String rejectNote;
	
	private String purchaseType;
	
		
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

	public List<Integer> getPoIdList() {
		return poIdList;
	}

	public void setPoIdList(List<Integer> poIdList) {
		this.poIdList = poIdList;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}
	
	
	
	
	

}
