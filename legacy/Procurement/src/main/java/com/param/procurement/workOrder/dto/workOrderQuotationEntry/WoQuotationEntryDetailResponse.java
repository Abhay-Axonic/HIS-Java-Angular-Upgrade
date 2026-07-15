package com.param.procurement.workOrder.dto.workOrderQuotationEntry;

public class WoQuotationEntryDetailResponse {
	
	private Integer id;
	
	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer quantity;
	
	private String status;

	private String specification;
	
	private Double amount;

	private String batchNo;
	
	private String expectedExclusion;

	private String expectedInclusion;

	private String paymentTerms;

	private String remark;

	private Integer statusId;
	
	private Integer vendorQuotationDetailId;

	private Double vendorcopalias;
	
	private String expectedexclusionalias;
	
	private String expectedinclusionalias;
	
	private String paymenttermalias;
	
	
	public WoQuotationEntryDetailResponse(Integer id, Double vendorCop, String expectedExclusion, String expectedInclusion,
			String paymentTerm, String remark, Integer statusId, Integer vendorQuotationDetailId,
			Integer itemId, String itemCode, String itemName,Integer quantity, String status, String specification, String batchNo) {
		this.id = id;
		this.amount = vendorCop;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.status = status;
		this.specification = specification;
		this.batchNo = batchNo;
		this.expectedExclusion = expectedExclusion;
		this.expectedInclusion = expectedInclusion;
		this.paymentTerms = paymentTerm;
		this.remark = remark;
		this.statusId = statusId;
		this.vendorQuotationDetailId = vendorQuotationDetailId;
	}
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}



	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}
	
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getExpectedExclusion() {
		return expectedExclusion;
	}


	public void setExpectedExclusion(String expectedExclusion) {
		this.expectedExclusion = expectedExclusion;
	}


	public String getExpectedInclusion() {
		return expectedInclusion;
	}


	public void setExpectedInclusion(String expectedInclusion) {
		this.expectedInclusion = expectedInclusion;
	}


	

	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Integer getStatusId() {
		return statusId;
	}


	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getVendorQuotationDetailId() {
		return vendorQuotationDetailId;
	}


	public void setVendorQuotationDetailId(Integer vendorQuotationDetailId) {
		this.vendorQuotationDetailId = vendorQuotationDetailId;
	}
	

	
	public String getExpectedinclusionalias() {
		return expectedinclusionalias;
	}

	public void setExpectedinclusionalias(String expectedinclusionalias) {
		this.expectedinclusionalias = expectedinclusionalias;
	}
	public Double getVendorcopalias() {
		return vendorcopalias;
	}

	public void setVendorcopalias(Double vendorcopalias) {
		this.vendorcopalias = vendorcopalias;
	}

	public String getExpectedexclusionalias() {
		return expectedexclusionalias;
	}

	public void setExpectedexclusionalias(String expectedexclusionalias) {
		this.expectedexclusionalias = expectedexclusionalias;
	}


	public String getPaymenttermalias() {
		return paymenttermalias;
	}

	public void setPaymenttermalias(String paymenttermalias) {
		this.paymenttermalias = paymenttermalias;
	}
	
	public WoQuotationEntryDetailResponse() {
		super();
	}
}
