package com.param.billing.global.transaction.config.dto;

public class RequestApprovalDto {
	
	private Integer expenseId;
	
	private Double amount;
	
	private Integer requestSentTo;
	
	private Integer companyReceiptsExpenseId;
	
	

	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}

	public Integer getRequestSentTo() {
		return requestSentTo;
	}

	public void setRequestSentTo(Integer requestSentTo) {
		this.requestSentTo = requestSentTo;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
}
