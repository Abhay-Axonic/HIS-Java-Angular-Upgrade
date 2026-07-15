package com.param.procurement.quotation.dto.vendorSelection;

import java.util.Objects;

public class VendorSelectionMapperResponse {

	private Integer id;

	private String vendorName;

	private String vendorQRNumber;

	private Boolean isAccept;

	private Boolean warrantyFlag;

	private Boolean supportFlag;

	private Boolean technicalSpecFlag;

	private Boolean paymentFlag;

	private Integer quantity;

	private Double cop;

	private String remark;

	private String rejectNote;

	private String negotiationWarrantyPeriod;

	private String negotiationSupportTerms;

	private String negotiationTechSpec;

	private String negotiationPaymentTerms;

	public VendorSelectionMapperResponse() {
		super();
	}

	public VendorSelectionMapperResponse(Integer id, String vendorName, String vendorQRNumber, Boolean isAccept,
			Boolean warrantyFlag, Boolean supportFlag, Boolean technicalSpecFlag, Boolean paymentFlag, Integer quantity,
			Double cop, String remark, String rejectNote, String negotiationWarrantyPeriod,
			String negotiationSupportTerms, String negotiationTechSpec, String negotiationPaymentTerms) {
		super();
		this.id = id;
		this.vendorName = vendorName;
		this.vendorQRNumber = vendorQRNumber;
		this.isAccept = isAccept;
		this.warrantyFlag = warrantyFlag;
		this.supportFlag = supportFlag;
		this.technicalSpecFlag = technicalSpecFlag;
		this.paymentFlag = paymentFlag;
		this.quantity = quantity;
		this.cop = cop;
		this.remark = remark;
		this.rejectNote = rejectNote;
		this.negotiationWarrantyPeriod = negotiationWarrantyPeriod;
		this.negotiationSupportTerms = negotiationSupportTerms;
		this.negotiationTechSpec = negotiationTechSpec;
		this.negotiationPaymentTerms = negotiationPaymentTerms;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(Boolean isAccept) {
		this.isAccept = isAccept;
	}

	public Boolean getWarrantyFlag() {
		return warrantyFlag;
	}

	public void setWarrantyFlag(Boolean warrantyFlag) {
		this.warrantyFlag = warrantyFlag;
	}

	public Boolean getSupportFlag() {
		return supportFlag;
	}

	public void setSupportFlag(Boolean supportFlag) {
		this.supportFlag = supportFlag;
	}

	public Boolean getTechnicalSpecFlag() {
		return technicalSpecFlag;
	}

	public void setTechnicalSpecFlag(Boolean technicalSpecFlag) {
		this.technicalSpecFlag = technicalSpecFlag;
	}

	public Boolean getPaymentFlag() {
		return paymentFlag;
	}

	public void setPaymentFlag(Boolean paymentFlag) {
		this.paymentFlag = paymentFlag;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
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

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	private VendorSelectionMapperResponse e;

	public VendorSelectionMapperResponse(VendorSelectionMapperResponse e) {
		this.e = e;
	}

	public VendorSelectionMapperResponse unwrap() {
		return this.e;
	}

	/*@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		VendorSelectionMapperResponse that = (VendorSelectionMapperResponse) o;
		return Objects.equals(e.getVendorName(), that.e.getVendorName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(e.getVendorName());
	}*/
}
