package com.param.pharmacy.nursing.discharge.dto;

public class MedicationReconciliationSaveItem {

	private Long id;
	
	private Integer quantityToOrdered;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantityToOrdered() {
		return quantityToOrdered;
	}

	public void setQuantityToOrdered(Integer quantityToOrdered) {
		this.quantityToOrdered = quantityToOrdered;
	}
	
}
