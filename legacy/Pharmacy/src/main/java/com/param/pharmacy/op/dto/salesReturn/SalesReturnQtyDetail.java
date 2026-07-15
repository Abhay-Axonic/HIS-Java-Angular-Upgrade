package com.param.pharmacy.op.dto.salesReturn;

public class SalesReturnQtyDetail {
	
	Long totalReturnQuantity;
	
	Long saleDetailId;

	public Long getTotalReturnQuantity() {
		return totalReturnQuantity;
	}

	public void setTotalReturnQuantity(Long totalReturnQuantity) {
		this.totalReturnQuantity = totalReturnQuantity;
	}

	public Long getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Long saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public SalesReturnQtyDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
}
