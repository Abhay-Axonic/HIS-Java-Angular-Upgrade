package com.param.assetManagement.auctionRequisition.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RejectReasonResponse {

	protected Integer id;
	
	protected String reason;

	public RejectReasonResponse() {
		super();
	}
	
	public RejectReasonResponse(Integer id, String reason) {
		super();
		this.id = id;
		this.reason = reason;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
