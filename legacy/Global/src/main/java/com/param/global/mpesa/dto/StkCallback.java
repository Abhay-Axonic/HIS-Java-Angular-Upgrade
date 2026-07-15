package com.param.global.mpesa.dto;

import java.util.List;

public class StkCallback {

	private String MerchantRequestID;
	
	private String CheckoutRequestID;
	
	private String ResultCode;
	
	private String ResultDesc;
	
	private List<MpesaItem> item;
	
	private String transactionId;
	
	private Double amount;
	
	private String ResultParameters;
	
	private String ResultParameter;

	public String getMerchantRequestID() {
		return MerchantRequestID;
	}

	public void setMerchantRequestID(String merchantRequestID) {
		MerchantRequestID = merchantRequestID;
	}

	public String getCheckoutRequestID() {
		return CheckoutRequestID;
	}

	public void setCheckoutRequestID(String checkoutRequestID) {
		CheckoutRequestID = checkoutRequestID;
	}

	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public String getResultDesc() {
		return ResultDesc;
	}

	public void setResultDesc(String resultDesc) {
		ResultDesc = resultDesc;
	}

	public List<MpesaItem> getItem() {
		return item;
	}

	public void setItem(List<MpesaItem> item) {
		this.item = item;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	
	public String getResultParameters() {
		return ResultParameters;
	}

	public void setResultParameters(String resultParameters) {
		ResultParameters = resultParameters;
	}

	public String getResultParameter() {
		return ResultParameter;
	}

	public void setResultParameter(String resultParameter) {
		ResultParameter = resultParameter;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
