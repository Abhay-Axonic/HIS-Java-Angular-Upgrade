package com.param.billing.unit.dto;

import java.math.BigDecimal;

public class DepositMatrixResDto {
	
	private Integer mDepositMatrixId;
	
	private String bedCategory;

	private BigDecimal reservationDeposit;

	public String getBedCategory() {
		return bedCategory;
	}

	public void setBedCategory(String bedCategory) {
		this.bedCategory = bedCategory;
	}

	public BigDecimal getReservationDeposit() {
		return reservationDeposit;
	}

	public void setReservationDeposit(BigDecimal reservationDeposit) {
		this.reservationDeposit = reservationDeposit;
	}

	public Integer getmDepositMatrixId() {
		return mDepositMatrixId;
	}

	public void setmDepositMatrixId(Integer mDepositMatrixId) {
		this.mDepositMatrixId = mDepositMatrixId;
	}

	

}
