package com.param.global.mpesa.dto;

import java.util.HashMap;
import java.util.Map;

public class TransactionResultDto {

	private Result Result;

	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Result getResult() {
		return Result;
	}

	public void setResult(Result result) {
		Result = result;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	
	
	
}
