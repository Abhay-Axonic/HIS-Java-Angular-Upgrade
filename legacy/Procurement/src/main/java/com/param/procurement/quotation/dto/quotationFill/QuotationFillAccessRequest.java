package com.param.procurement.quotation.dto.quotationFill;

public class QuotationFillAccessRequest {
	
     private Integer id;
     
     private String name;
     
     private Integer qrAccessMapperId;
     
     private String details;

     private String vendorDetails;

	public QuotationFillAccessRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public QuotationFillAccessRequest(Integer id, String name, String details, String vendorDetails) {
		super();
		this.id = id;
		this.name = name;
		this.details = details;
		this.vendorDetails = vendorDetails;
	}

	public QuotationFillAccessRequest(Integer id, String name, String details) {
		super();
		this.id = id;
		this.name = name;
		this.details = details;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(String vendorDetails) {
		this.vendorDetails = vendorDetails;
	}

	public Integer getQrAccessMapperId() {
		return qrAccessMapperId;
	}

	public void setQrAccessMapperId(Integer qrAccessMapperId) {
		this.qrAccessMapperId = qrAccessMapperId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
     
 }
