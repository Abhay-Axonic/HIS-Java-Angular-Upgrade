package com.param.global.billing.dto;

public class GetDoctorShareDto {

	private Double docShare;
	
	private Double docShareAmount;
	
	private String effectiveDate;
	
	private Integer docShareType;
	
	public Double getDocShareAmount() {
		return docShareAmount;
	}

	public void setDocShareAmount(Double docShareAmount) {
		this.docShareAmount = docShareAmount;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Double getDocShare() {
		return docShare;
	}

	public void setDocShare(Double docShare) {
		this.docShare = docShare;
	}

	public Integer getDocShareType() {
		return docShareType;
	}

	public void setDocShareType(Integer docShareType) {
		this.docShareType = docShareType;
	}
	
}
