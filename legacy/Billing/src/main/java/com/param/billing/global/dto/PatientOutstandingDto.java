package com.param.billing.global.dto;

import java.math.BigInteger;

public class PatientOutstandingDto {
	private Integer status;
	private Double outstandingAmt;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getOutstandingAmt() {
		return outstandingAmt;
	}

	public void setOutstandingAmt(Double outstandingAmt) {
		this.outstandingAmt = outstandingAmt;
	}
}
