package com.param.pharmacy.prescription.dto;

import java.util.List;

public class IsInterventionDto {

private String prescription_mongo_id;
	
	private List<PrescriptionItem> items;

	public String getPrescription_mongo_id() {
		return prescription_mongo_id;
	}

	public void setPrescription_mongo_id(String prescription_mongo_id) {
		this.prescription_mongo_id = prescription_mongo_id;
	}

	public List<PrescriptionItem> getItems() {
		return items;
	}

	public void setItems(List<PrescriptionItem> items) {
		this.items = items;
	}
}
