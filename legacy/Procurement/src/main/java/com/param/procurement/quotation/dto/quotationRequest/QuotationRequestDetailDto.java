package com.param.procurement.quotation.dto.quotationRequest;

import java.util.List;

public class QuotationRequestDetailDto {
	
	private Integer id;
	
	private Integer itemId;
	
	private Integer statusId;

	private Integer quantity;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private String warrantyPeriod;
	
	private String paymentTerms;

	private String supportTerms;

	private String technicalSpecification;
	
	private List<PurchaseRequestDto> prIds;
	
	private List<Integer> prDetailId;
	
	private List<Integer> prId;
	
	private List<QRAccessoriesMapper> expectedAccessories;

	private Integer qrPendingQuantity;
	
	

	public Integer getQrPendingQuantity() {
		return qrPendingQuantity;
	}

	public void setQrPendingQuantity(Integer qrPendingQuantity) {
		this.qrPendingQuantity = qrPendingQuantity;
	}

	public QuotationRequestDetailDto() {
		super();
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getItemId() {
		return itemId;
	}


	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}


	public Integer getQuantity() {
		return quantity;
	}


	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public Integer getUomTypeId() {
		return uomTypeId;
	}


	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}


	public Integer getUomUnitId() {
		return uomUnitId;
	}


	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}


	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}


	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}


	public String getPaymentTerms() {
		return paymentTerms;
	}


	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}


	public String getSupportTerms() {
		return supportTerms;
	}


	public void setSupportTerms(String supportTerms) {
		this.supportTerms = supportTerms;
	}


	public String getTechnicalSpecification() {
		return technicalSpecification;
	}


	public void setTechnicalSpecification(String technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<QRAccessoriesMapper> getExpectedAccessories() {
		return expectedAccessories;
	}

	public void setExpectedAccessories(List<QRAccessoriesMapper> expectedAccessories) {
		this.expectedAccessories = expectedAccessories;
	}

	public List<Integer> getPrId() {
		return prId;
	}

	public void setPrId(List<Integer> prId) {
		this.prId = prId;
	}

	public List<PurchaseRequestDto> getPrIds() {
		return prIds;
	}

	public void setPrIds(List<PurchaseRequestDto> prIds) {
		this.prIds = prIds;
	}

	public List<Integer> getPrDetailId() {
		return prDetailId;
	}

	public void setPrDetailId(List<Integer> prDetailId) {
		this.prDetailId = prDetailId;
	}
	
}
