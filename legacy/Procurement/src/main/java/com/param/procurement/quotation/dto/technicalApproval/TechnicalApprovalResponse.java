package com.param.procurement.quotation.dto.technicalApproval;

import java.util.ArrayList;
import java.util.List;

public class TechnicalApprovalResponse {

	private Integer id;

	private String qrNumber;
	
	private List<TechnicalApprovalDetailsResponse> items;
	
	public TechnicalApprovalResponse(Integer id, String qrNumber) {
		super();
		this.id = id;
		this.qrNumber = qrNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public List<TechnicalApprovalDetailsResponse> getItems() {
		return items;
	}

	public void setItems(List<TechnicalApprovalDetailsResponse> items) {
		this.items = items;
	}
	
	public void addItems(TechnicalApprovalDetailsResponse item) {
		if (this.items == null)
			this.items = new ArrayList<TechnicalApprovalDetailsResponse>();
		
		this.items.add(item);
	}
	
}
