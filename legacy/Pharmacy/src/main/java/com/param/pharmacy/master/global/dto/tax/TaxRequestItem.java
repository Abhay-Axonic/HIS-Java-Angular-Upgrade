package com.param.pharmacy.master.global.dto.tax;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TaxRequestItem {

	@JsonInclude(Include.NON_NULL)
	protected Integer id; 
	
	protected Integer taxComponentId;
	
	protected Double taxPercentage;
	

	public TaxRequestItem() {
		super();
		
	}

	public TaxRequestItem(Integer id, Integer taxComponentId, Double taxPercentage) {
		super();
		this.id = id;
		this.taxComponentId = taxComponentId;
		this.taxPercentage = taxPercentage;
	}


	public Integer getTaxComponentId() {
		return taxComponentId;
	}

	public void setTaxComponentId(Integer taxComponentId) {
		this.taxComponentId = taxComponentId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}