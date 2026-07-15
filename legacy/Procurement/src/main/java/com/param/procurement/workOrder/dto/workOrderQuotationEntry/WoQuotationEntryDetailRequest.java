package com.param.procurement.workOrder.dto.workOrderQuotationEntry;

import com.param.entity.model.master.Status;

public class WoQuotationEntryDetailRequest {

	private Integer id;

	private String expectedExclusion;

	private String expectedInclusion;

	private String paymentTerm;

	private String remark;

	private Integer statusId;
	
	private Double vendorCop;

	public Double getVendorCop() {
		return vendorCop;
	}

	public void setVendorCop(Double vendorCop) {
		this.vendorCop = vendorCop;
	}

	private Integer vendorQuotationDetailId;

	public WoQuotationEntryDetailRequest() {
		super();
	}
	
	public WoQuotationEntryDetailRequest(Integer id, String expectedExclusion, String expectedInclusion,
			String paymentTerm, String remark, Integer statusId, Integer vendorQuotationDetailId) {
		super();
		this.id = id;
		this.expectedExclusion = expectedExclusion;
		this.expectedInclusion = expectedInclusion;
		this.paymentTerm = paymentTerm;
		this.remark = remark;
		this.statusId = statusId;
		this.vendorQuotationDetailId = vendorQuotationDetailId;
	}

/*	public WoQuotationEntryDetailRequest(Integer id, String expectedExclusion, String expectedInclusion,
			String paymentTerm, String remark, Integer statusId, Integer vendorQuotationDetailId, Double vendorCop) {
		this( id,  expectedExclusion,  expectedInclusion,
			 paymentTerm,  remark,  statusId,  vendorQuotationDetailId);
		this.vendorCop = vendorCop;
	}
*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	
}
