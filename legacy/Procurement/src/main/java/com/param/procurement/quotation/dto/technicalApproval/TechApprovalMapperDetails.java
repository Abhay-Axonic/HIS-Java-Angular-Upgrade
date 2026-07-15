package com.param.procurement.quotation.dto.technicalApproval;

public class TechApprovalMapperDetails {
	
	private Integer id;

	private Integer vendorId;
	
	private Integer qfId;

	private String warrantyPeriod;

	private String supportTerms;

	private String technicalSpecifications;

	private String paymentTerms;

	private Boolean warrantyFlag;

	private Boolean supportFlag;

	private Boolean technicalSpecFlag;

	private Boolean paymentFlag;

	private String warrantyRejectNote;

	private String supportRejectNote;

	private String technicalSpecRejectNote;

	private String paymentRejectNote;

	private String remark;

	private String rejectNote;

	private Boolean accept;

	private Boolean reject;

	private Boolean onHold;
	
     private String vendorName;
	
	private String vendorQRNumber;
	
	
	public TechApprovalMapperDetails() {
		super();
	}

	public TechApprovalMapperDetails(Integer id, Integer vendorId, Integer qfId, String warrantyPeriod, String supportTerms,
			String technicalSpecifications, String paymentTerms, Boolean warrantyFlag, Boolean supportFlag,
			Boolean technicalSpecFlag, Boolean paymentFlag, String warrantyRejectNote, String supportRejectNote,
			String technicalSpecRejectNote, String paymentRejectNote, String remark, String rejectNote, Boolean accept,
			Boolean reject, Boolean onHold,String vendorName,String vendorQRNumber) {
		super();
		this.id= id;
		this.vendorId = vendorId;
		this.qfId = qfId;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecifications = technicalSpecifications;
		this.paymentTerms = paymentTerms;
		this.warrantyFlag = warrantyFlag;
		this.supportFlag = supportFlag;
		this.technicalSpecFlag = technicalSpecFlag;
		this.paymentFlag = paymentFlag;
		this.warrantyRejectNote = warrantyRejectNote;
		this.supportRejectNote = supportRejectNote;
		this.technicalSpecRejectNote = technicalSpecRejectNote;
		this.paymentRejectNote = paymentRejectNote;
		this.remark = remark;
		this.rejectNote = rejectNote;
		this.accept = accept;
		this.reject = reject;
		this.onHold = onHold;
		this.vendorName = vendorName;
		this.vendorQRNumber = vendorQRNumber;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	
	public Integer getQfId() {
		return qfId;
	}

	public void setQfId(Integer qfId) {
		this.qfId = qfId;
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

	public String getWarrantyRejectNote() {
		return warrantyRejectNote;
	}

	public void setWarrantyRejectNote(String warrantyRejectNote) {
		this.warrantyRejectNote = warrantyRejectNote;
	}

	public String getSupportRejectNote() {
		return supportRejectNote;
	}

	public void setSupportRejectNote(String supportRejectNote) {
		this.supportRejectNote = supportRejectNote;
	}

	public String getTechnicalSpecRejectNote() {
		return technicalSpecRejectNote;
	}

	public void setTechnicalSpecRejectNote(String technicalSpecRejectNote) {
		this.technicalSpecRejectNote = technicalSpecRejectNote;
	}

	public String getPaymentRejectNote() {
		return paymentRejectNote;
	}

	public void setPaymentRejectNote(String paymentRejectNote) {
		this.paymentRejectNote = paymentRejectNote;
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
