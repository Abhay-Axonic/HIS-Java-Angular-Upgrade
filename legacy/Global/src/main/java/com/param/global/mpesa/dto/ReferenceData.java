package com.param.global.mpesa.dto;

import java.util.HashMap;
import java.util.Map;

public class ReferenceData {

	
	private ReferenceItem ReferenceItem;
	

	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public ReferenceItem getReferenceItem() {
		return ReferenceItem;
	}

	public void setReferenceItem(ReferenceItem referenceItem) {
		ReferenceItem = referenceItem;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	
	
}
