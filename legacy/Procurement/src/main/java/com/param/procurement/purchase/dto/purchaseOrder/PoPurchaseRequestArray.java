package com.param.procurement.purchase.dto.purchaseOrder;

public class PoPurchaseRequestArray {
	
	private Integer id ;
	
	private Integer approvedQuantity;
	
	private String prNo;
	
	private Integer poDetailId;
	
	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}

	public PoPurchaseRequestArray() {
		super();
	}
	
	public PoPurchaseRequestArray(Integer id, Integer approvedQuantity,String prNo) {
		this.id = id;
		this.approvedQuantity = approvedQuantity;
		this.prNo = prNo;
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

	public String getPrNo() {
		return prNo;
	}

	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}
	
	

}
