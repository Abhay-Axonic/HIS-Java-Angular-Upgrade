package com.param.global.mpesa.dto;

import java.util.HashMap;
import java.util.Map;

public class ResultParameter {

	private String Key;
	private String Value;

	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	
}
