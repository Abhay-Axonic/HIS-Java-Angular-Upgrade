package com.param.global.procedure.dto;

import java.util.List;

import com.param.global.billing.dto.PharmacyBillCommonDto;


public class OTCompletionRequest extends PharmacyBillCommonDto{
	
	protected Integer saleId;
	 	 	
	protected Integer issueId;

	protected Boolean isMedication;

	protected Boolean isImplant;

	protected Boolean isConsumable;

	protected List<OTCompletionItemRequest> items;

	public Boolean getIsMedication() {
		return isMedication;
	}

	public void setIsMedication(Boolean isMedication) {
		this.isMedication = isMedication;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsConsumable() {
		return isConsumable;
	}

	public void setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
	}


	public List<OTCompletionItemRequest> getItems() {
		return items;
	}

	public void setItems(List<OTCompletionItemRequest> items) {
		this.items = items;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}
	
}
