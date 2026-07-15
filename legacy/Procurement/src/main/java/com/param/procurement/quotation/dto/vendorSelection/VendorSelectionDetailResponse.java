package com.param.procurement.quotation.dto.vendorSelection;

import java.util.List;

public class VendorSelectionDetailResponse {

	private Integer id;

	private Integer itemId;

	private String itemName;

	private Integer quantity;

	private String warrantyPeriod;

	private String supportTerms;

	private String technicalSpecifications;

	private String paymentTerms;

	private String remark;
	
	private List<VendorSelectionMapperResponse> vendorSelectionDetails;

	public VendorSelectionDetailResponse() {
		super();
	}

	public VendorSelectionDetailResponse(Integer id, Integer itemId, String itemName, Integer quantity,
			String warrantyPeriod, String supportTerms, String technicalSpecifications, String paymentTerms,
			String remark,List<VendorSelectionMapperResponse> vendorSelectionDetails) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.quantity = quantity;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecifications = technicalSpecifications;
		this.paymentTerms = paymentTerms;
		this.remark = remark;
		this.vendorSelectionDetails = vendorSelectionDetails;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<VendorSelectionMapperResponse> getVendorSelectionDetails() {
		return vendorSelectionDetails;
	}

	public void setVendorSelectionDetails(List<VendorSelectionMapperResponse> vendorSelectionDetails) {
		this.vendorSelectionDetails = vendorSelectionDetails;
	}
	
}
