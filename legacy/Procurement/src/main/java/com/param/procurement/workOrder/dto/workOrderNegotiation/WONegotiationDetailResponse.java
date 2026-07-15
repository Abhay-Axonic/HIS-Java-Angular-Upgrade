package com.param.procurement.workOrder.dto.workOrderNegotiation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class WONegotiationDetailResponse {
	
	private Integer id;
	
	

	private Integer itemId;

	private String itemCode;

	private String itemName;
	
	private String serialNumber;
	
	
	private String inclusionDetails;
	
	private String exclusionDetails;
	
	private String paymentTerms;

	private Integer quantity;
	
	private Double amount;
	
	private String  vendorInclusionDetails;
	
	private String vendorExclusionDetails;
	
	private String vendorPaymentTerms;
	
	private String vendorRemark;
	
	private Integer vendorQuantity;
	
	private Integer vendorAmount;

	private String specification;
	
	private String remark;
	
	
	
	private List<WoNegotiationMapperRequest> negotiationDetails;
	
	public WONegotiationDetailResponse() {
		super();
	}
	
	public WONegotiationDetailResponse(String inclusionDetails, String exclusionDetails, String paymentTerms) {
		this.inclusionDetails = inclusionDetails;
		this.exclusionDetails = exclusionDetails;
		this.paymentTerms = paymentTerms;
	}

	public WONegotiationDetailResponse(Integer batchId,Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			String specification,String serialNumber,String inclusionDetails, String exclusionDetails, String paymentTerms,
			Double amount, String vendorInclusionDetails, String vendorExclusionDetails, String vendorPaymentTerms,
			String vendorRemark, Integer vendorQuantity, Integer vendorAmount,List<WoNegotiationMapperRequest> negotiationDetails) {
		this.id=id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.specification = specification;
		this.serialNumber = serialNumber;
		this.inclusionDetails = inclusionDetails;
		this.exclusionDetails = exclusionDetails;
		this.paymentTerms = paymentTerms;
		this.amount = amount;
		this.vendorInclusionDetails = vendorInclusionDetails;
		this.vendorExclusionDetails = vendorExclusionDetails;
		this.vendorPaymentTerms = vendorPaymentTerms;
		this.vendorRemark = vendorRemark;
		this.vendorQuantity = vendorQuantity;
		this.vendorAmount = vendorAmount;
		this.negotiationDetails = negotiationDetails;
	}
	
	
	public WONegotiationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			String specification, String batchNo, String expectedInclusion, String venExpectedInclusion,
			String venExpectedExclusion, String venPaymentTerm,String remark, Double vendorCop, String vendorRemark,
			List<WoNegotiationMapperRequest> woNegotiationMapperResponse) {
		this.id=id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.specification = specification;
		this.serialNumber = batchNo;
		this.vendorInclusionDetails = expectedInclusion;
		this.inclusionDetails = venExpectedInclusion;
		this.exclusionDetails = venExpectedExclusion;
		this.paymentTerms = venPaymentTerm;
		this.remark=remark;
		this.amount=vendorCop;
		this.vendorRemark = vendorRemark;
		this.negotiationDetails = woNegotiationMapperResponse;
		
		
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(String inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public String getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(String exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getVendorInclusionDetails() {
		return vendorInclusionDetails;
	}

	public void setVendorInclusionDetails(String vendorInclusionDetails) {
		this.vendorInclusionDetails = vendorInclusionDetails;
	}

	public String getVendorExclusionDetails() {
		return vendorExclusionDetails;
	}

	public void setVendorExclusionDetails(String vendorExclusionDetails) {
		this.vendorExclusionDetails = vendorExclusionDetails;
	}

	public String getVendorPaymentTerms() {
		return vendorPaymentTerms;
	}

	public void setVendorPaymentTerms(String vendorPaymentTerms) {
		this.vendorPaymentTerms = vendorPaymentTerms;
	}

	public String getVendorRemark() {
		return vendorRemark;
	}

	public void setVendorRemark(String vendorRemark) {
		this.vendorRemark = vendorRemark;
	}

	public Integer getVendorQuantity() {
		return vendorQuantity;
	}

	public void setVendorQuantity(Integer vendorQuantity) {
		this.vendorQuantity = vendorQuantity;
	}

	public Integer getVendorAmount() {
		return vendorAmount;
	}

	public void setVendorAmount(Integer vendorAmount) {
		this.vendorAmount = vendorAmount;
	}

	public List<WoNegotiationMapperRequest> getNegotiationDetails() {
		return negotiationDetails;
	}

	public void setNegotiationDetails(List<WoNegotiationMapperRequest> negotiationDetails) {
		this.negotiationDetails = negotiationDetails;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public void addItems(WoNegotiationMapperRequest item) {
		if (this.negotiationDetails == null)
			this.negotiationDetails = new ArrayList<WoNegotiationMapperRequest>();
		
		this.negotiationDetails.add(item);
	}
}
