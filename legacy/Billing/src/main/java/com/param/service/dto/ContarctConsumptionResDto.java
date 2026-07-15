package com.param.service.dto;


public class ContarctConsumptionResDto {
	
	private Integer patientSponsorDetailsId;
	private Integer contractId;
	private Double finalPrice;
	private Boolean isfromCap;
	public Integer getPatientSponsorDetailsId() {
		return patientSponsorDetailsId;
	}
	public void setPatientSponsorDetailsId(Integer patientSponsorDetailsId) {
		this.patientSponsorDetailsId = patientSponsorDetailsId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}
	public Boolean getIsfromCap() {
		return isfromCap;
	}
	public void setIsfromCap(Boolean isfromCap) {
		this.isfromCap = isfromCap;
	}

}
