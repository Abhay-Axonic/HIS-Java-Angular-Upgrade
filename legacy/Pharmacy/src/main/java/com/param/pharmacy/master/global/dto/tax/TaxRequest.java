package com.param.pharmacy.master.global.dto.tax;

import java.util.List;

public class TaxRequest extends TaxResponse {

	protected Integer taxComponentId;
	
	protected Double taxPercentage;
	
	protected Integer generalLedgerId;
	
	protected List<TaxRequestItem> items;
	
	public TaxRequest() {
		super();
	}


	public TaxRequest(Integer id, String code, String name,Double taxPercentage, Boolean status) {
		super(id,code, name, taxPercentage,status);
		
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


	public List<TaxRequestItem> getItems() {
		return items;
	}


	public void setItems(List<TaxRequestItem> items) {
		this.items = items;
	}


	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}


	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}


}
