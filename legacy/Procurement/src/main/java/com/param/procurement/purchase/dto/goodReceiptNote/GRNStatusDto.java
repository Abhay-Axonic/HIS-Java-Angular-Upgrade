package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class GRNStatusDto extends CookieObjectDto{
	
    private List<Integer> grnIdList;
    
    private Integer grnId;
	
	private Integer statusId;
	
	private Integer approvalStatusId;
	
	private Integer rejectReasonId;

	private String rejectNote;
	
	
	public GRNStatusDto() {
		super();
	}
	
	public GRNStatusDto(List<Integer> grnIdList, Integer grnId, Integer statusId, Integer approvalStatusId,
			Integer rejectReasonId, String rejectNote) {
		super();
		this.grnIdList = grnIdList;
		this.grnId = grnId;
		this.statusId = statusId;
		this.approvalStatusId = approvalStatusId;
		this.rejectReasonId = rejectReasonId;
		this.rejectNote = rejectNote;
	}



	public List<Integer> getGrnIdList() {
		return grnIdList;
	}

	public void setGrnIdList(List<Integer> grnIdList) {
		this.grnIdList = grnIdList;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
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
