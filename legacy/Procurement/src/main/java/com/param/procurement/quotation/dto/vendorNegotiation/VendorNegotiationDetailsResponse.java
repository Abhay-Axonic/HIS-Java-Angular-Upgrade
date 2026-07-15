package com.param.procurement.quotation.dto.vendorNegotiation;

import java.util.List;

public class VendorNegotiationDetailsResponse {

	private Integer id;
	
	private Integer qrDetailId;

	private Integer itemId;

	private String itemName;

	private Integer quantity;

	private String warrantyPeriod;

	private String supportTerms;

	private String technicalSpecifications;

	private String paymentTerms;

	private Double cop;

	private String remark;

	private String vendorWarrantyPeriod;

	private String vendorSupportTerms;

	private String vendorPaymentTerms;

	private String vendorTechSpec;

	private Double vendorCop;

	private String vendorRemark;
	
	private List<VendorNegotiationMapperResp> negotiationDetails;

	public VendorNegotiationDetailsResponse() {
		super();
	}

	public VendorNegotiationDetailsResponse(Integer id, Integer qrDetailId, Integer itemId, String itemName, Integer quantity,
			String warrantyPeriod, String supportTerms, String technicalSpecifications, String paymentTerms, Double cop,
			String remark, String vendorWarrantyPeriod, String vendorSupportTerms, String vendorPaymentTerms,
			String vendorTechSpec, Double vendorCop, String vendorRemark,List<VendorNegotiationMapperResp> negotiationDetails) {
		super();
		this.id = id;
		this.qrDetailId = qrDetailId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.quantity = quantity;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecifications = technicalSpecifications;
		this.paymentTerms = paymentTerms;
		this.cop = cop;
		this.remark = remark;
		this.vendorWarrantyPeriod = vendorWarrantyPeriod;
		this.vendorSupportTerms = vendorSupportTerms;
		this.vendorPaymentTerms = vendorPaymentTerms;
		this.vendorTechSpec = vendorTechSpec;
		this.vendorCop = vendorCop;
		this.vendorRemark = vendorRemark;
		this.negotiationDetails = negotiationDetails;
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

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVendorWarrantyPeriod() {
		return vendorWarrantyPeriod;
	}

	public void setVendorWarrantyPeriod(String vendorWarrantyPeriod) {
		this.vendorWarrantyPeriod = vendorWarrantyPeriod;
	}

	public String getVendorSupportTerms() {
		return vendorSupportTerms;
	}

	public void setVendorSupportTerms(String vendorSupportTerms) {
		this.vendorSupportTerms = vendorSupportTerms;
	}

	public String getVendorPaymentTerms() {
		return vendorPaymentTerms;
	}

	public void setVendorPaymentTerms(String vendorPaymentTerms) {
		this.vendorPaymentTerms = vendorPaymentTerms;
	}

	public String getVendorTechSpec() {
		return vendorTechSpec;
	}

	public void setVendorTechSpec(String vendorTechSpec) {
		this.vendorTechSpec = vendorTechSpec;
	}

	public Double getVendorCop() {
		return vendorCop;
	}

	public void setVendorCop(Double vendorCop) {
		this.vendorCop = vendorCop;
	}

	public String getVendorRemark() {
		return vendorRemark;
	}

	public void setVendorRemark(String vendorRemark) {
		this.vendorRemark = vendorRemark;
	}

	public List<VendorNegotiationMapperResp> getNegotiationDetails() {
		return negotiationDetails;
	}

	public void setNegotiationDetails(List<VendorNegotiationMapperResp> negotiationDetails) {
		this.negotiationDetails = negotiationDetails;
	}

	public Integer getQrDetailId() {
		return qrDetailId;
	}

	public void setQrDetailId(Integer qrDetailId) {
		this.qrDetailId = qrDetailId;
	}
	
	
	
}
