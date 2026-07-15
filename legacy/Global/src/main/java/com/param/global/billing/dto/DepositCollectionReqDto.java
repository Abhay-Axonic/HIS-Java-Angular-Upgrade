package com.param.global.billing.dto;

import java.util.List;

public class DepositCollectionReqDto {
	private DepositMasterDto depositMasterDto;
	private List<PaymentDepositDetailsDto> listPaymentDepositDetailsDto;
	
	public DepositMasterDto getDepositMasterDto() {
		return depositMasterDto;
	}
	public void setDepositMasterDto(DepositMasterDto depositMasterDto) {
		this.depositMasterDto = depositMasterDto;
	}
	public List<PaymentDepositDetailsDto> getListPaymentDepositDetailsDto() {
		return listPaymentDepositDetailsDto;
	}
	public void setListPaymentDepositDetailsDto(List<PaymentDepositDetailsDto> listPaymentDepositDetailsDto) {
		this.listPaymentDepositDetailsDto = listPaymentDepositDetailsDto;
	}
}
