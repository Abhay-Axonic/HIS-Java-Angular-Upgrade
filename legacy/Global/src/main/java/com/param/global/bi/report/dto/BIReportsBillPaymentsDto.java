package com.param.global.bi.report.dto;

public class BIReportsBillPaymentsDto {

	private String billDateTime;

	private Double outstanding;

	public String getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(String billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

}
