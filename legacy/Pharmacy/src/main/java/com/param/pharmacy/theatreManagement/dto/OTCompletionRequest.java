package com.param.pharmacy.theatreManagement.dto;

import java.util.List;

import com.param.pharmacy.theatreManagement.dto.OTCompletionItemRequest;

public class OTCompletionRequest {

	protected Integer admissionId;

	protected Integer storeId;

	protected Boolean isMedication;

	protected Boolean isImplant;

	protected Boolean isConsumable;

	protected Double taxAmount;

	protected Double totalAmount;

	protected Double netAmount;

	protected List<OTCompletionItemRequest> items;

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

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

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public List<OTCompletionItemRequest> getItems() {
		return items;
	}

	public void setItems(List<OTCompletionItemRequest> items) {
		this.items = items;
	}

}
