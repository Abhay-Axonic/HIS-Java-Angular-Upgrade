package com.param.procurement.quotation.dto.vendorNegotiation;

import java.util.Date;

public class VendorNegotiationMapperResp {

	private Integer id;

	private Date negotiationDate;

	private Double negotiationCop;

	private String negotiationRemark;

	private String negotiationWarrantyPeriod;

	private String negotiationSupportTerms;

	private String negotiationTechSpec;

	private String negotiationPaymentTerms;

	private Boolean accept;

	private Boolean reject;

	private Boolean onHold;

	private String rejectNote;


	public VendorNegotiationMapperResp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VendorNegotiationMapperResp(Integer id, Date negotiationDate, Double negotiationCop,
			String negotiationRemark, String negotiationWarrantyPeriod, String negotiationSupportTerms,
			String negotiationTechSpec, String negotiationPaymentTerms, Boolean accept, Boolean reject, Boolean onHold,
			String rejectNote) {
		super();
		this.id = id;
		this.negotiationDate = negotiationDate;
		this.negotiationCop = negotiationCop;
		this.negotiationRemark = negotiationRemark;
		this.negotiationWarrantyPeriod = negotiationWarrantyPeriod;
		this.negotiationSupportTerms = negotiationSupportTerms;
		this.negotiationTechSpec = negotiationTechSpec;
		this.negotiationPaymentTerms = negotiationPaymentTerms;
		this.accept = accept;
		this.reject = reject;
		this.onHold = onHold;
		this.rejectNote = rejectNote;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getNegotiationDate() {
		return negotiationDate;
	}

	public void setNegotiationDate(Date negotiationDate) {
		this.negotiationDate = negotiationDate;
	}

	public String getNegotiationRemark() {
		return negotiationRemark;
	}

	public void setNegotiationRemark(String negotiationRemark) {
		this.negotiationRemark = negotiationRemark;
	}

	public String getNegotiationWarrantyPeriod() {
		return negotiationWarrantyPeriod;
	}

	public void setNegotiationWarrantyPeriod(String negotiationWarrantyPeriod) {
		this.negotiationWarrantyPeriod = negotiationWarrantyPeriod;
	}

	public String getNegotiationSupportTerms() {
		return negotiationSupportTerms;
	}

	public void setNegotiationSupportTerms(String negotiationSupportTerms) {
		this.negotiationSupportTerms = negotiationSupportTerms;
	}

	public String getNegotiationTechSpec() {
		return negotiationTechSpec;
	}

	public void setNegotiationTechSpec(String negotiationTechSpec) {
		this.negotiationTechSpec = negotiationTechSpec;
	}

	public String getNegotiationPaymentTerms() {
		return negotiationPaymentTerms;
	}

	public void setNegotiationPaymentTerms(String negotiationPaymentTerms) {
		this.negotiationPaymentTerms = negotiationPaymentTerms;
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

	public Double getNegotiationCop() {
		return negotiationCop;
	}

	public void setNegotiationCop(Double negotiationCop) {
		this.negotiationCop = negotiationCop;
	}

}
