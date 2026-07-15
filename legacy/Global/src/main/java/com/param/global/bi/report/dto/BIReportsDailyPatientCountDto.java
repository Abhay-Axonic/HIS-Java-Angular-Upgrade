package com.param.global.bi.report.dto;

import java.math.BigDecimal;

public class BIReportsDailyPatientCountDto {
	private String createdDate;

	private BigDecimal ptcount;

	private BigDecimal male;

	private BigDecimal female;

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getPtcount() {
		return ptcount;
	}

	public void setPtcount(BigDecimal ptcount) {
		this.ptcount = ptcount;
	}

	public BigDecimal getMale() {
		return male;
	}

	public void setMale(BigDecimal male) {
		this.male = male;
	}

	public BigDecimal getFemale() {
		return female;
	}

	public void setFemale(BigDecimal female) {
		this.female = female;
	}

}
