package com.param.procurement.quotation.dto.technicalApproval;

import java.util.List;

public class TechnicalApprovalDetailsResponse {

	private Integer id;

	private Integer itemId;

	private String itemName;

	private Integer quantity;

	private String itemDetails;
	
	private String warrantyPeriod;

	private String supportTerms;

	private String technicalSpecifications;

	private String paymentTerms;
	
	private List<TechApprovalMapperDetails> vendorDetails;
	
	
	public TechnicalApprovalDetailsResponse() {
		super();
	}

	public TechnicalApprovalDetailsResponse(Integer id, Integer itemId, String itemName, Integer quantity,
			String warrantyPeriod, String supportTerms, String technicalSpecifications,
			String paymentTerms,List<TechApprovalMapperDetails> vendorDetails) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.quantity = quantity;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecifications = technicalSpecifications;
		this.paymentTerms = paymentTerms;
		this.vendorDetails = vendorDetails;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(String itemDetails) {
		this.itemDetails = itemDetails;
	}

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getSupportTerms() {
		return supportTerms;
	}

	public void setSupportTerms(String supportTerms) {
		this.supportTerms = supportTerms;
	}

	public String getTechnicalSpecifications() {
		return technicalSpecifications;
	}

	public void setTechnicalSpecifications(String technicalSpecifications) {
		this.technicalSpecifications = technicalSpecifications;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public List<TechApprovalMapperDetails> getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(List<TechApprovalMapperDetails> vendorDetails) {
		this.vendorDetails = vendorDetails;
	}
	
}
