package com.param.procurement.quotation.dto.quotationRequest;

import java.util.List;

public class PurchaseRequestDto {
	
	private Integer prId;
	
	private List<Integer> prDetailId;

	public PurchaseRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PurchaseRequestDto(Integer prId, List<Integer> prDetailId) {
		super();
		this.prId = prId;
		this.prDetailId = prDetailId;
	}

	public Integer getPrId() {
		return prId;
	}

	public void setPrId(Integer prId) {
		this.prId = prId;
	}

	public List<Integer> getPrDetailId() {
		return prDetailId;
	}

	public void setPrDetailId(List<Integer> prDetailId) {
		this.prDetailId = prDetailId;
	}
	
}
