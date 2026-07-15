package com.param.billing.global.dto;

import java.util.List;

import com.param.billing.global.transaction.dto.PaymentRefundDetailsDto;
import com.param.billing.global.transaction.dto.PaymentRefundMasterDto;

public class RefundCollectionReqDto {

	private PaymentRefundMasterDto paymentRefundMasterDto;
	private List<PaymentRefundDetailsDto> listPaymentRefundDetails;
	
	public PaymentRefundMasterDto getPaymentRefundMasterDto() {
		return paymentRefundMasterDto;
	}
	public void setPaymentRefundMasterDto(PaymentRefundMasterDto paymentRefundMasterDto) {
		this.paymentRefundMasterDto = paymentRefundMasterDto;
	}
	public List<PaymentRefundDetailsDto> getListPaymentRefundDetails() {
		return listPaymentRefundDetails;
	}
	public void setListPaymentRefundDetails(List<PaymentRefundDetailsDto> listPaymentRefundDetails) {
		this.listPaymentRefundDetails = listPaymentRefundDetails;
	}
	
	
}
