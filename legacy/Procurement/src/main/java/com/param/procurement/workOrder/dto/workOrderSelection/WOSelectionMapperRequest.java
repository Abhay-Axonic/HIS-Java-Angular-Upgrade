package com.param.procurement.workOrder.dto.workOrderSelection;

public class WOSelectionMapperRequest {
	
	private Integer id;

	private Boolean isAccepted;

	private Integer quantity;

	private Integer quotationNegotiationId;

	private String rejectNote;
	
	private String remark;
	
	private Boolean paymentTerms;
	
	private Boolean exclusionDetails;
	
	private Boolean inclusionDetails;

	public WOSelectionMapperRequest() {
		super();
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

	public Integer getQuotationNegotiationId() {
		return quotationNegotiationId;
	}

	public void setQuotationNegotiationId(Integer quotationNegotiationId) {
		this.quotationNegotiationId = quotationNegotiationId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(Boolean paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Boolean getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(Boolean exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public Boolean getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(Boolean inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}
	
	
	
}
