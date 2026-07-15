package com.param.pharmacy.nursing.discharge.dto;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class MedicationReconciliationSave extends CookieObjectDto {

	private Long id;
	
	private Integer statusId;
	
	private List<MedicationReconciliationSaveItem> items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<MedicationReconciliationSaveItem> getItems() {
		return items;
	}

	public void setItems(List<MedicationReconciliationSaveItem> items) {
		this.items = items;
	}
	
}
