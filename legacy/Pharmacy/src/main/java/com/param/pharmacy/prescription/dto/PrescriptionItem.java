package com.param.pharmacy.prescription.dto;

public class PrescriptionItem {

	private Integer item_id;
	 
    private boolean is_intervention;

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public boolean isIs_intervention() {
		return is_intervention;
	}

	public void setIs_intervention(boolean is_intervention) {
		this.is_intervention = is_intervention;
	}
}
