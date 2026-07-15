package com.param.procurement.workOrder.dto.workOrderSelection;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVDetailResponse;
@JsonInclude(Include.NON_NULL)
public class WOSelectionMapperResponse {

	private Integer id;

	private Boolean isAccepted;

	private Integer quantity;

	private String rejectNote;
	
	private List<WONegotiationDetailResponse> detail;

	private Boolean expectedDetails;

	private Boolean inclusionDetails;

	private Boolean paymentTerms;
	
	private Boolean exclusionFlag;

	private Boolean inclusionFlag;

	private Boolean paymentFlag;
	
	private String negotiationInclusionDetails;

	private String negotiationExclusionDetails;

	private String negotiationPaymentTerms;
	
	private Double amount;
	
	private String vendorName;
	
	private String vendorQRNumber;

	public List<WONegotiationDetailResponse> getDetail() {
		return detail;
	}

	public void setDetail(List<WONegotiationDetailResponse> detail) {
		this.detail = detail;
	}

	
	public Boolean getExclusionFlag() {
		return exclusionFlag;
	}

	public void setExclusionFlag(Boolean exclusionFlag) {
		this.exclusionFlag = exclusionFlag;
	}

	public Boolean getInclusionFlag() {
		return inclusionFlag;
	}

	public void setInclusionFlag(Boolean inclusionFlag) {
		this.inclusionFlag = inclusionFlag;
	}

	public Boolean getPaymentFlag() {
		return paymentFlag;
	}

	public void setPaymentFlag(Boolean paymentFlag) {
		this.paymentFlag = paymentFlag;
	}

	public String getNegotiationInclusionDetails() {
		return negotiationInclusionDetails;
	}

	public void setNegotiationInclusionDetails(String negotiationInclusionDetails) {
		this.negotiationInclusionDetails = negotiationInclusionDetails;
	}

	public String getNegotiationExclusionDetails() {
		return negotiationExclusionDetails;
	}

	public void setNegotiationExclusionDetails(String negotiationExclusionDetails) {
		this.negotiationExclusionDetails = negotiationExclusionDetails;
	}

	public String getNegotiationPaymentTerms() {
		return negotiationPaymentTerms;
	}

	public void setNegotiationPaymentTerms(String negotiationPaymentTerms) {
		this.negotiationPaymentTerms = negotiationPaymentTerms;
	}

	public WOSelectionMapperResponse() {
		super();
	}
	
	public WOSelectionMapperResponse(Integer id, Boolean isAccepted, Integer quantity, String rejectNote, Boolean expectedDetails, Boolean inclusionDetails, Boolean paymentTerms) {
		super();
		this.id = id;
		this.isAccepted = isAccepted;
		this.quantity = quantity;
		this.rejectNote = rejectNote;
		this.expectedDetails = expectedDetails;
		this.inclusionDetails = inclusionDetails;
		this.paymentTerms = paymentTerms;

		
	}
	

	public WOSelectionMapperResponse(Integer id, Boolean isAccepted, Integer quantity, String rejectNote, Boolean exclusionFlag, Boolean inclusionFlag, Boolean paymentFlag , Double amount,
			String expectedDetails, String inclusionDetails, String paymentTerms, String vendorName, String vendorQRNumber) {
		super();
		this.id = id;
		this.isAccepted = isAccepted;
		this.quantity = quantity;
		this.rejectNote = rejectNote;
		this.negotiationExclusionDetails = expectedDetails;
		this.negotiationInclusionDetails = inclusionDetails;
		this.negotiationPaymentTerms = paymentTerms;
		this.amount=amount;
		this.exclusionFlag=exclusionFlag;
		this.inclusionFlag=inclusionFlag;
		this.paymentFlag=paymentFlag;
		this.vendorName=vendorName;
		this.vendorQRNumber=vendorQRNumber;
		

		
	}
	public Boolean getExpectedDetails() {
		return expectedDetails;
	}

	public void setExpectedDetails(Boolean expectedDetails) {
		this.expectedDetails = expectedDetails;
	}

	public Boolean getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(Boolean inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public Boolean getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(Boolean paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}
	
	
	public void addItems(WONegotiationDetailResponse item) {
		if (this.detail == null)
			this.detail = new ArrayList<WONegotiationDetailResponse>();
		
		this.detail.add(item);
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorQRNumber() {
		return vendorQRNumber;
	}

	public void setVendorQRNumber(String vendorQRNumber) {
		this.vendorQRNumber = vendorQRNumber;
	}

	
	
}
