package com.param.procurement.workOrder.dto.workOrderNegotiation;

import java.util.List;

public class WONegotiationDetailRequest {
	
	private Integer id;

	private Integer vendorQuotationDetailId;
	
	private List<WoNegotiationMapperRequest> mapperList;

	public WONegotiationDetailRequest() {
		super();
	}
	
	public WONegotiationDetailRequest(Integer id) {
		super();
		this.id = id;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVendorQuotationDetailId() {
		return vendorQuotationDetailId;
	}

	public void setVendorQuotationDetailId(Integer vendorQuotationDetailId) {
		this.vendorQuotationDetailId = vendorQuotationDetailId;
	}

	public List<WoNegotiationMapperRequest> getMapperList() {
		return mapperList;
	}

	public void setMapperList(List<WoNegotiationMapperRequest> mapperList) {
		this.mapperList = mapperList;
	}
	
}
