package com.param.procurement.workOrder.dto.workOrderNegotiation;

import java.util.Date;

public class WoNegotiationMapperRequest {
	
	private Integer id;
	
	private Integer negotiationQuantity;
	
	private Double negotiationAmount;
	
	private String negotiationInclusion;

	private String negotiationExclusion;

	private String negotiationPaymentTerms;

	private String negotiationRemark;
	
	private Boolean accept;
	
	private Boolean reject;
	
	private Boolean onHold;
	
	private String rejectNote;
	
	private Date negotiationDate;
	
	private Integer negotitateStatus;
	
	private Integer vendorId;

	public Integer getVendorId() {
		return vendorId;
	}


	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}


	public Integer getNegotitateStatus() {
		return negotitateStatus;
	}


	public void setNegotitateStatus(Integer negotitateStatus) {
		this.negotitateStatus = negotitateStatus;
	}


	public WoNegotiationMapperRequest() {
		super();
	}

	
	public WoNegotiationMapperRequest(Integer id, Integer negotiationQuantity, Double negotiationAmount,
			String negotiationInclusion, String negotiationExclusion, String negotiationPaymentTerms,
			String negotiationRemark, Boolean accept, Boolean reject, Boolean onHold, String rejectNote,Date negotiationDate) {
		super();
		this.id = id;
		this.negotiationQuantity = negotiationQuantity;
		this.negotiationAmount = negotiationAmount;
		this.negotiationInclusion = negotiationInclusion;
		this.negotiationExclusion = negotiationExclusion;
		this.negotiationPaymentTerms = negotiationPaymentTerms;
		this.negotiationRemark = negotiationRemark;
		this.accept = accept;
		this.reject = reject;
		this.onHold = onHold;
		this.rejectNote = rejectNote;
		this.negotiationDate = negotiationDate;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getNegotiationQuantity() {
		return negotiationQuantity;
	}


	public void setNegotiationQuantity(Integer negotiationQuantity) {
		this.negotiationQuantity = negotiationQuantity;
	}


	

	public Double getNegotiationAmount() {
		return negotiationAmount;
	}


	public void setNegotiationAmount(Double negotiationAmount) {
		this.negotiationAmount = negotiationAmount;
	}


	public String getNegotiationInclusion() {
		return negotiationInclusion;
	}


	public void setNegotiationInclusion(String negotiationInclusion) {
		this.negotiationInclusion = negotiationInclusion;
	}


	public String getNegotiationExclusion() {
		return negotiationExclusion;
	}


	public void setNegotiationExclusion(String negotiationExclusion) {
		this.negotiationExclusion = negotiationExclusion;
	}


	public String getNegotiationPaymentTerms() {
		return negotiationPaymentTerms;
	}


	public void setNegotiationPaymentTerms(String negotiationPaymentTerms) {
		this.negotiationPaymentTerms = negotiationPaymentTerms;
	}


	public String getNegotiationRemark() {
		return negotiationRemark;
	}


	public void setNegotiationRemark(String negotiationRemark) {
		this.negotiationRemark = negotiationRemark;
	}


	public Boolean getAccept() {
		return accept;
	}


	public void setAccept(Boolean accept) {
		this.accept = accept;
	}


	public Boolean getReject() {
		return reject;
	}


	public void setReject(Boolean reject) {
		this.reject = reject;
	}


	public Boolean getOnHold() {
		return onHold;
	}


	public void setOnHold(Boolean onHold) {
		this.onHold = onHold;
	}


	public String getRejectNote() {
		return rejectNote;
	}


	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}


	public Date getNegotiationDate() {
		return negotiationDate;
	}


	public void setNegotiationDate(Date negotiationDate) {
		this.negotiationDate = negotiationDate;
	}

	

}
