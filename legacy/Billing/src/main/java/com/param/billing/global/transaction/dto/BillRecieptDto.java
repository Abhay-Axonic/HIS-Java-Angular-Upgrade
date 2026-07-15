package com.param.billing.global.transaction.dto;

import com.param.global.unit.dto.SmsUtilityConfigInfoDto;

public class BillRecieptDto {

	private Integer recieptId;
	
	private Integer depositConsumptionId;
	
	private SmsUtilityConfigInfoDto smsInfo;
	
	private String receiptNo;

	private Integer billStatusId;
	
	private Integer BillId;
	
	public Integer getRecieptId() {
		return recieptId;
	}

	public void setRecieptId(Integer recieptId) {
		this.recieptId = recieptId;
	}

	public Integer getDepositConsumptionId() {
		return depositConsumptionId;
	}

	public void setDepositConsumptionId(Integer depositConsumptionId) {
		this.depositConsumptionId = depositConsumptionId;
	}

	public SmsUtilityConfigInfoDto getSmsInfo() {
		return smsInfo;
	}

	public void setSmsInfo(SmsUtilityConfigInfoDto smsInfo) {
		this.smsInfo = smsInfo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Integer getBillId() {
		return BillId;
	}

	public void setBillId(Integer billId) {
		BillId = billId;
	}
	
}
