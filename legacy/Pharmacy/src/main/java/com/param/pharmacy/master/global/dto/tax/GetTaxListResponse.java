package com.param.pharmacy.master.global.dto.tax;

public class GetTaxListResponse {

	protected Integer id;
	
    protected String name ;
	
	protected Double taxPercentage;
	
	public GetTaxListResponse(Integer id, String name, Double taxPercentage) {
		super();
		this.id = id;
		this.name = name;
		this.taxPercentage = taxPercentage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
}
