package com.param.procurement.quotation.dto.physicalInspection;

import java.util.List;

public class ConsignmentStockConversionSaveItem
{
	private Integer id;
	
    private Integer approvedQuantity;
	  
    private Integer rejectQuantity;
    	
    private Integer rejectReasonId;
    
    private String rejectNote;
    
    private Integer statusId;
    
    private List<ConsignmentStockConversationBatch> batch;

	public List<ConsignmentStockConversationBatch> getBatch() {
		return batch;
	}

	public void setBatch(List<ConsignmentStockConversationBatch> batch) {
		this.batch = batch;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

		
}