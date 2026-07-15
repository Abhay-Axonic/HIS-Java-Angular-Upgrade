package com.param.global.billing.dao;

import java.math.BigDecimal;

public class DepositConsumptionDto {
	private String billNo;
	private String billDate;
	private String visitNo;
	private String visitDate;
	private Integer visitTypeId;
	private BigDecimal amount;
	
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getVisitNo() {
		return visitNo;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
