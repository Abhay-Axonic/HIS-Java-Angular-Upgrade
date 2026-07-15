package com.param.procurement.quotation.dto.quotationRequest;

import java.util.Date;

public class PurchaseRequestDetailsResponse {
	
	private Long id;

	private String prNumber;
	
	private Date prDate;
	
	

	public PurchaseRequestDetailsResponse() {
		super();
	}

	public PurchaseRequestDetailsResponse(Long id, String prNumber, Date prDate) {
		super();
		this.id = id;
		this.prNumber = prNumber;
		this.prDate = prDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	public Date getPrDate() {
		return prDate;
	}

	public void setPrDate(Date prDate) {
		this.prDate = prDate;
	}
	
}
