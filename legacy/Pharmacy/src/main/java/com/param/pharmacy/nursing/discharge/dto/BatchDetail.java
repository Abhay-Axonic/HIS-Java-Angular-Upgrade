package com.param.pharmacy.nursing.discharge.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BatchDetail {
	 private String batcnno;
	 private Date expdate;
	 private Integer issuequantity;
	 private Integer acceptedqty;
	 private Integer batchId;
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getAcceptedqty() {
		return acceptedqty;
	}
	public void setAcceptedqty(Integer acceptedqty) {
		this.acceptedqty = acceptedqty;
	}
	public Integer getIssuequantity() {
		return issuequantity;
	}
	public void setIssuequantity(Integer issuequantity) {
		this.issuequantity = issuequantity;
	}
	public String getBatcnno() {
		return batcnno;
	}
	public void setBatcnno(String batcnno) {
		this.batcnno = batcnno;
	}
	public Date getExpdate() {
		return expdate;
	}
	public void setExpdate(Date expdate) {
		this.expdate = expdate;
	}
}
