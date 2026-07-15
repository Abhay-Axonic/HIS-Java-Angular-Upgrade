package com.param.procurement.quotation.dto.vendorNegotiation;

public class VendorNegotiationDetailsRequest {
	
	private Integer id;
	
	private VendorNegotiationMapperResp negotiationDetails;
	
	private Integer qrDetailId;

	public VendorNegotiationDetailsRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VendorNegotiationMapperResp getNegotiationDetails() {
		return negotiationDetails;
	}

	public void setNegotiationDetails(VendorNegotiationMapperResp negotiationDetails) {
		this.negotiationDetails = negotiationDetails;
	}

	public Integer getQrDetailId() {
		return qrDetailId;
	}

	public void setQrDetailId(Integer qrDetailId) {
		this.qrDetailId = qrDetailId;
	}
	
}
