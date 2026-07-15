package com.param.pharmacy.ip.dto.pharmacology;

import java.util.Date;

public class PrescriptionFrequencyDetailResp {
	
	Long prescriptionDetaild;
	
	String freqDescription;
	
	Double dose;
	
	Date startDate;
	
	Date stopDate;
	
	String unit;
	String frequency;

	public PrescriptionFrequencyDetailResp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getPrescriptionDetaild() {
		return prescriptionDetaild;
	}

	public void setPrescriptionDetaild(Long prescriptionDetaild) {
		this.prescriptionDetaild = prescriptionDetaild;
	}

	public String getFreqDescription() {
		return freqDescription;
	}

	public void setFreqDescription(String freqDescription) {
		this.freqDescription = freqDescription;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

}
