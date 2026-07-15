package com.param.billing.global.transaction.dto;
public class CompanyReceiptServiceDetailsDto {
	
	private Integer compReceiptServiceDetlsId;
	private Integer comReceiptDtlId;
	private Integer serviceId;
	private Integer itemId;
	private Double receivedAmount;
	private Double disallowedAmount;
	private Double otherDeduction;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private Double tdsAmount;
	
	public Double getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public Double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public Integer getCompReceiptServiceDetlsId() {
		return compReceiptServiceDetlsId;
	}

	public void setCompReceiptServiceDetlsId(Integer compReceiptServiceDetlsId) {
		this.compReceiptServiceDetlsId = compReceiptServiceDetlsId;
	}

	public Integer getComReceiptDtlId() {
		return comReceiptDtlId;
	}

	public void setComReceiptDtlId(Integer comReceiptDtlId) {
		this.comReceiptDtlId = comReceiptDtlId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	

}
