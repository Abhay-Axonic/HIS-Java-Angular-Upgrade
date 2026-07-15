package com.param.pharmacy.ip.dto.returnAcceptance;

public class PharmacySaleReturnDto {

	private Long saleDetailId;
	private Long saleId;
	private Integer itemId;
	private Integer productCategoryId;
	private Long batchId;
	private Integer billableConsumptionId;
	private Integer billbleConsumptionDetailId;
	
	public PharmacySaleReturnDto()
	{
		super();
	}
	
	public Long getSaleDetailId() {
		return saleDetailId;
	}
	public void setSaleDetailId(Long saleDetailId) {
		this.saleDetailId = saleDetailId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getBillableConsumptionId() {
		return billableConsumptionId;
	}

	public void setBillableConsumptionId(Integer billableConsumptionId) {
		this.billableConsumptionId = billableConsumptionId;
	}

	public Integer getBillbleConsumptionDetailId() {
		return billbleConsumptionDetailId;
	}

	public void setBillbleConsumptionDetailId(Integer billbleConsumptionDetailId) {
		this.billbleConsumptionDetailId = billbleConsumptionDetailId;
	}
	
	
}
