package com.param.procurement.quotation.dto.vendorSelection;

public class VendorSelectionMapperRequest {
	
	private Integer id;

	private Boolean isAccept;
	private Boolean isReject;
	
	private Boolean warrantyPeriod;
	
	private Boolean supportTerm;

	private Boolean technicalSpecification;

	private Boolean expectedPayment;

	private Integer quantity;

	private String remark;
	
	private String rejectNote;

	public Boolean getIsReject() {
		return isReject;
	}

	public void setIsReject(Boolean isReject) {
		this.isReject = isReject;
	}

	public VendorSelectionMapperRequest() {
		super();
	}

	public VendorSelectionMapperRequest(Integer id, Boolean isAccept, Boolean warrantyPeriod, Boolean supportTerm,
			Boolean technicalSpecification, Boolean expectedPayment, Integer quantity, String remark,
			String rejectNote) {
		super();
		this.id = id;
		this.isAccept = isAccept;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerm = supportTerm;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
		this.quantity = quantity;
		this.remark = remark;
		this.rejectNote = rejectNote;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(Boolean isAccept) {
		this.isAccept = isAccept;
	}

	public Boolean getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(Boolean warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public Boolean getSupportTerm() {
		return supportTerm;
	}

	public void setSupportTerm(Boolean supportTerm) {
		this.supportTerm = supportTerm;
	}

	public Boolean getTechnicalSpecification() {
		return technicalSpecification;
	}

	public void setTechnicalSpecification(Boolean technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public Boolean getExpectedPayment() {
		return expectedPayment;
	}

	public void setExpectedPayment(Boolean expectedPayment) {
		this.expectedPayment = expectedPayment;
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
}
