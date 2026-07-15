package com.param.pharmacy.master.global.dto.tax;

import java.util.ArrayList;
import java.util.List;

public class GetTaxResponse {

	protected Integer id;

	protected String code;

	protected String name ;
	
	protected Double taxPercentage;

	protected Boolean status;
	
	protected List<GetTaxListResponse> items;	
	
	protected Integer generalLedgerId;
	
	public GetTaxResponse(Integer id, String code, String name, Double taxPercentage, Boolean status) {
		super();
		this.id = id;
		this.name = name;
		this.taxPercentage=taxPercentage;
		this.status = status;
		this.code = code;
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	public List<GetTaxListResponse> getItems() {
		return items;
	}


	public void setItems(List<GetTaxListResponse> items) {
		this.items = items;
	}


	public void addItems(GetTaxListResponse item) {
		if (this.items == null)
			this.items = new ArrayList<GetTaxListResponse>();
		
		this.items.add(item);
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}
	
}
