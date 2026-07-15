package com.param.procurement.quotation.dto.quotationFill;

import java.util.List;

public class QuotationFillDetailRequestDto {
	
	private Integer id;

	private Integer statusId;
	
	private Double vendorCop;

	private String vendorExpectedPayment;

	private String vendorExpectedSupport;

	private String vendorTechnicalSpecification;

	private String vendorWarrantyPeriod;
	
	private String remark;
	
	private Integer qrDetailId;

	private Integer vqrDetailId;

	private List<QuotationFillAccessRequest> accessoriesDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Double getVendorCop() {
		return vendorCop;
	}

	public void setVendorCop(Double vendorCop) {
		this.vendorCop = vendorCop;
	}

	public String getVendorExpectedPayment() {
		return vendorExpectedPayment;
	}

	public void setVendorExpectedPayment(String vendorExpectedPayment) {
		this.vendorExpectedPayment = vendorExpectedPayment;
	}

	public String getVendorExpectedSupport() {
		return vendorExpectedSupport;
	}

	public void setVendorExpectedSupport(String vendorExpectedSupport) {
		this.vendorExpectedSupport = vendorExpectedSupport;
	}

	public String getVendorTechnicalSpecification() {
		return vendorTechnicalSpecification;
	}

	public void setVendorTechnicalSpecification(String vendorTechnicalSpecification) {
		this.vendorTechnicalSpecification = vendorTechnicalSpecification;
	}

	public String getVendorWarrantyPeriod() {
		return vendorWarrantyPeriod;
	}

	public void setVendorWarrantyPeriod(String vendorWarrantyPeriod) {
		this.vendorWarrantyPeriod = vendorWarrantyPeriod;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getQrDetailId() {
		return qrDetailId;
	}

	public void setQrDetailId(Integer qrDetailId) {
		this.qrDetailId = qrDetailId;
	}

	public Integer getVqrDetailId() {
		return vqrDetailId;
	}

	public void setVqrDetailId(Integer vqrDetailId) {
		this.vqrDetailId = vqrDetailId;
	}

	public List<QuotationFillAccessRequest> getAccessoriesDetails() {
		return accessoriesDetails;
	}

	public void setAccessoriesDetails(List<QuotationFillAccessRequest> accessoriesDetails) {
		this.accessoriesDetails = accessoriesDetails;
	}
	
	
	


}
