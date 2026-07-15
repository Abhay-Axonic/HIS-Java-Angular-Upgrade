package com.param.global.dto.global;

import java.util.Date;

public class PurchaseOrderStaged {
	
    private Integer id;
	
	private Integer stagedQuantity;
	
	private Date stagedDate;
	
	private Integer approvedQuantity;
	
	private Integer poApprovedQuantity;

	private Integer receivedQuantity;
	
	private Integer poDetailId;
	
	public Integer getPoDetailId() {
		return poDetailId;
	}


	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}

	public PurchaseOrderStaged() {
		super();
	}


	public PurchaseOrderStaged(Integer id, Integer receivedQuantity, Date stagedDate,Integer approvedQuantity,Integer poApprovedQuantity) {
		super();
		this.id = id;
		this.receivedQuantity = receivedQuantity;
		this.stagedDate = stagedDate;
		this.approvedQuantity = approvedQuantity;
		this.poApprovedQuantity = poApprovedQuantity;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getStagedQuantity() {
		return stagedQuantity;
	}


	public void setStagedQuantity(Integer stagedQuantity) {
		this.stagedQuantity = stagedQuantity;
	}


	public Date getStagedDate() {
		return stagedDate;
	}


	public void setStagedDate(Date stagedDate) {
		this.stagedDate = stagedDate;
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

	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}


	
}
