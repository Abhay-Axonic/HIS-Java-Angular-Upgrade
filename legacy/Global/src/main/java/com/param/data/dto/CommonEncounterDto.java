package com.param.data.dto;

import java.math.BigInteger;

public class CommonEncounterDto {

	private BigInteger encounterNo;
	private Integer patientId;
	private String encounterDate;
	private String primaryDoctor;
	private String reviewDoctor;
	private String paymentType;
	private String patientType;

	public BigInteger getEncounterNo() {
		return encounterNo;
	}

	public void setEncounterNo(BigInteger encounterNo) {
		this.encounterNo = encounterNo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}

	public String getPrimaryDoctor() {
		return primaryDoctor;
	}

	public void setPrimaryDoctor(String primaryDoctor) {
		this.primaryDoctor = primaryDoctor;
	}

	public String getReviewDoctor() {
		return reviewDoctor;
	}

	public void setReviewDoctor(String reviewDoctor) {
		this.reviewDoctor = reviewDoctor;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

}
