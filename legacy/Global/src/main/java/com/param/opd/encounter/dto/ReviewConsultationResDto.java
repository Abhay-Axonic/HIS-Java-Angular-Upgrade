package com.param.opd.encounter.dto;

public class ReviewConsultationResDto {

	private Integer encounterId;
	private Integer serviceId;
	private Integer reviewAgainstSplId;
	private Integer reviewAgainstDocId;
	private String serviceStandardName;
	private String specialityName;
	private String subSpecialityName;
	private Integer orderDetailsId;
	private String encounterNumber;
	
	public Integer getReviewAgainstSplId() {
		return reviewAgainstSplId;
	}
	public void setReviewAgainstSplId(Integer reviewAgainstSplId) {
		this.reviewAgainstSplId = reviewAgainstSplId;
	}
	public Integer getReviewAgainstDocId() {
		return reviewAgainstDocId;
	}
	public void setReviewAgainstDocId(Integer reviewAgainstDocId) {
		this.reviewAgainstDocId = reviewAgainstDocId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceStandardName() {
		return serviceStandardName;
	}
	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getSubSpecialityName() {
		return subSpecialityName;
	}
	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public String getEncounterNumber() {
		return encounterNumber;
	}
	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}
	
	

}
