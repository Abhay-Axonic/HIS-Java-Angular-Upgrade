package com.param.procurement.purchase.dto.goodReceiptNote;

import java.io.Serializable;
import java.util.Date;

public class GRNStagedDto implements Serializable {
	
    private Integer id;
	
    private Integer receivedQuantity;
    
    private Integer stagedQuantity;
    
    private Integer pendingQuantity;
	
	private Date poStagedDate;
	
	private Integer poStagedId;
	
	private Integer approvedQuantity;
	
	private Integer poApprovedQuantity;

	private Boolean isEditable;
	
	private Integer grnDetailId;

	public GRNStagedDto() {
		super();
	}
	
	
	public GRNStagedDto(Integer id, Integer receivedQuantity, Integer stagedQuantity, Integer pendingQuantity, Date poStagedDate) {
		super();
		this.id = id;
		this.receivedQuantity = receivedQuantity;
		this.stagedQuantity = stagedQuantity;
		this.pendingQuantity = pendingQuantity;
		this.poStagedDate = poStagedDate;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}


	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}


	public Integer getPendingQuantity() {
		return pendingQuantity;
	}


	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}


	public Date getPoStagedDate() {
		return poStagedDate;
	}


	public void setPoStagedDate(Date poStagedDate) {
		this.poStagedDate = poStagedDate;
	}


	public Integer getPoStagedId() {
		return poStagedId;
	}


	public void setPoStagedId(Integer poStagedId) {
		this.poStagedId = poStagedId;
	}


	public Integer getStagedQuantity() {
		return stagedQuantity;
	}


	public void setStagedQuantity(Integer stagedQuantity) {
		this.stagedQuantity = stagedQuantity;
	}


	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}


	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}


	public Integer getPoApprovedQuantity() {
		return poApprovedQuantity;
	}


	public void setPoApprovedQuantity(Integer poApprovedQuantity) {
		this.poApprovedQuantity = poApprovedQuantity;
	}


	public Boolean getIsEditable() {
		return this.isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}


	public Integer getGrnDetailId() {
		return grnDetailId;
	}


	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}
}
