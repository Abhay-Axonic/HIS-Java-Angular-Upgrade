package com.param.global.mpesa.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultParameters {

	private List<ResultParameter>  ResultParameter;
	

	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public List<ResultParameter> getResultParameter() {
		return ResultParameter;
	}

	public void setResultParameter(List<ResultParameter> resultParameter) {
		ResultParameter = resultParameter;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	
	
}
