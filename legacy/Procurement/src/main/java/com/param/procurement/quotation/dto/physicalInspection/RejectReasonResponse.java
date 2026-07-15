package com.param.procurement.quotation.dto.physicalInspection;

public class RejectReasonResponse
{
	private Integer id;
	
	private String reason;

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

	public RejectReasonResponse(Integer id, String reason) {
		super();
		this.id = id;
		this.reason = reason;
	}
	
}