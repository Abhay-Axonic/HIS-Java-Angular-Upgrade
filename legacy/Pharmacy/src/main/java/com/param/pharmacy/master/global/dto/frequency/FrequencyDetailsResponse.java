package com.param.pharmacy.master.global.dto.frequency;

public class FrequencyDetailsResponse extends FrequencyResponse {
	
	
	public FrequencyDetailsResponse() {
		super();
		
	}

	public FrequencyDetailsResponse(Integer id, String code, String description, Integer noOfAdministrationTime, Boolean status) {
		super(id, code, description, noOfAdministrationTime, status);
		
	}

	public void add(FrequencyDetailsResponse frequencyResponse) {
		
	}
}
