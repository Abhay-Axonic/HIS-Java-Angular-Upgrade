package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

public class GRNStagedList {
	
	private Integer id;
	
	private Integer stagedReceivedQuantity;
	
	private Date stagedDcDate;
	
	private Boolean isEditable;
	
	private Integer approvedQuantity;
	
	private Integer grnDetailId;

	private Integer grnId;

	private Integer poDetailId;

	public Integer getStagedReceivedQuantity() {
		return stagedReceivedQuantity;
	}

	public void setStagedReceivedQuantity(Integer stagedReceivedQuantity) {
		this.stagedReceivedQuantity = stagedReceivedQuantity;
	}

	public Date getStagedDcDate() {
		return stagedDcDate;
	}

	public void setStagedDcDate(Date stagedDcDate) {
		this.stagedDcDate = stagedDcDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}
}
