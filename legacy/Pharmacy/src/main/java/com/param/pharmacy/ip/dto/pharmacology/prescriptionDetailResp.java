package com.param.pharmacy.ip.dto.pharmacology;

import java.util.Date;

public class prescriptionDetailResp {
	
	Long saleId;
	
	Long saleDetailId;
	
	Date prescriptionDate;
	
	String desc;
	Long prescriptionId;

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Long getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Long saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	} 
	
	
}
