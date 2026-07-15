package com.param.procurement.purchase.dto.purchaseOrder;

import java.util.Date;

public class PurchaseOrderStagedDto {
	
	private Integer id;
	
	private Integer quantity;
	
	private Date stagedDate;
	
	private Integer approvedQuantity;
	
	private Integer poDetailId;
	


	public PurchaseOrderStagedDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
	
}
