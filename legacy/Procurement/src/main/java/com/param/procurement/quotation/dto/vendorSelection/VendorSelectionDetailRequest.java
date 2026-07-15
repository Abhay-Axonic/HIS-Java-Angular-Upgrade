package com.param.procurement.quotation.dto.vendorSelection;

import java.util.List;

public class VendorSelectionDetailRequest {
	
	private Integer id;
	
	private Integer statusId;
	
	private List<VendorSelectionMapperRequest> vendorSelectionDetails;

	public VendorSelectionDetailRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<VendorSelectionMapperRequest> getVendorSelectionDetails() {
		return vendorSelectionDetails;
	}

	public void setVendorSelectionDetails(List<VendorSelectionMapperRequest> vendorSelectionDetails) {
		this.vendorSelectionDetails = vendorSelectionDetails;
	}
	
	

}
