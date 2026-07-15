package com.param.common.constants;

public enum RequestType {
	
	DirectRequest(1), 
	AgainstIndent(2), 
	AgainstPurchaseRequest(3), 
	AgainstPurchaseOrder(4),
	AgainstWorkOrderQuotationRequest(12), 
	AgainstWorkOrderRequest(13),
	AgainstQuotationRequest(14),
	AgainstConsignment(16);
	
	private Integer requestTypeId;

	public Integer getRequestType() {
		return this.requestTypeId;
	}

	private RequestType(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}
}
