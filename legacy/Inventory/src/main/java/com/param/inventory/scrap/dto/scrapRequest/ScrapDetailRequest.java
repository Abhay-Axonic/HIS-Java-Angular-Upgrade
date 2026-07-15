package com.param.inventory.scrap.dto.scrapRequest;

public class ScrapDetailRequest {

	protected Integer id;

	protected Long batchId;

	protected Integer scrapQuantity;

	protected Integer scrapReasonId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer statusId;
	
	protected Double amountCop;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getScrapQuantity() {
		return scrapQuantity;
	}

	public void setScrapQuantity(Integer scrapQuantity) {
		this.scrapQuantity = scrapQuantity;
	}

	public Integer getScrapReasonId() {
		return scrapReasonId;
	}

	public void setScrapReasonId(Integer scrapReasonId) {
		this.scrapReasonId = scrapReasonId;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Double getAmountCop() {
		return amountCop;
	}

	public void setAmountCop(Double amountCop) {
		this.amountCop = amountCop;
	}

}
