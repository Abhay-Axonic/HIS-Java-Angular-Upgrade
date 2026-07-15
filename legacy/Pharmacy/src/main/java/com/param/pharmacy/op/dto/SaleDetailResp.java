package com.param.pharmacy.op.dto;

public class SaleDetailResp {
	
	String batchDetails;
	
	Long detailId;

	public String getBatchDetails() {
		return batchDetails;
	}

	public void setBatchDetails(String batchDetails) {
		this.batchDetails = batchDetails;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public SaleDetailResp() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
