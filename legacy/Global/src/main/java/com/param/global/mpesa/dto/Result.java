package com.param.global.mpesa.dto;

import java.util.HashMap;
import java.util.Map;

public class Result {

	 private String  ResultType;
	 private String  ResultCode;
	 private String  ResultDesc;
	 private String  OriginatorConversationID;
	 private String  ConversationID;
	 private String  TransactionID;
	 private ResultParameters ResultParameters;
	 private ReferenceData ReferenceData;

	 private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getResultType() {
		return ResultType;
	}
	public void setResultType(String resultType) {
		ResultType = resultType;
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
	public String getOriginatorConversationID() {
		return OriginatorConversationID;
	}
	public void setOriginatorConversationID(String originatorConversationID) {
		OriginatorConversationID = originatorConversationID;
	}
	public String getConversationID() {
		return ConversationID;
	}
	public void setConversationID(String conversationID) {
		ConversationID = conversationID;
	}
	public String getTransactionID() {
		return TransactionID;
	}
	public void setTransactionID(String transactionID) {
		TransactionID = transactionID;
	}
	public ResultParameters getResultParameters() {
		return ResultParameters;
	}
	public void setResultParameters(ResultParameters resultParameters) {
		ResultParameters = resultParameters;
	}
	public ReferenceData getReferenceData() {
		return ReferenceData;
	}
	public void setReferenceData(ReferenceData referenceData) {
		ReferenceData = referenceData;
	}
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
