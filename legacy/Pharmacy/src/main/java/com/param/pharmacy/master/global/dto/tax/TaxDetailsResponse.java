package com.param.pharmacy.master.global.dto.tax;

public class TaxDetailsResponse extends TaxResponse {
	
	public TaxDetailsResponse() {
		super();
		
	}

	public TaxDetailsResponse(Integer id, String code, String name, Double taxPercentage, Boolean status ) {
		super(id, code, name, taxPercentage, status);
		
	}


	public void add(TaxDetailsResponse taxResponse) {
		
	}

}
