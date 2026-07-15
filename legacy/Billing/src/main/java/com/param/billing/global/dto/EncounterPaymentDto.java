package com.param.billing.global.dto;

import java.util.List;

import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

public class EncounterPaymentDto {

	private List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList;
	
	private EncounterMasterDto encounterMasterDto;
	
	private List<OrderDetailsMasterDto> orderDetailsList;
	
	private Character isNursingAssessmentRequired;
	
	private Character transferToActiveDeposit;
	
	private List<PaymentDepositDto> paymentDepositDetailsList;
	
	private Double totalDepositConsumption;
	
	private List<OTReservationDto> otReservationList;
	
	private Character cashSettlement;
	
	public Character getCashSettlement() {
		return cashSettlement;
	}

	public void setCashSettlement(Character cashSettlement) {
		this.cashSettlement = cashSettlement;
	}

	public Double getTotalDepositConsumption() {
		return totalDepositConsumption;
	}

	public void setTotalDepositConsumption(Double totalDepositConsumption) {
		this.totalDepositConsumption = totalDepositConsumption;
	}

	public List<PaymentDepositDto> getPaymentDepositDetailsList() {
		return paymentDepositDetailsList;
	}

	public void setPaymentDepositDetailsList(List<PaymentDepositDto> paymentDepositDetailsList) {
		this.paymentDepositDetailsList = paymentDepositDetailsList;
	}

	public Character getTransferToActiveDeposit() {
		return transferToActiveDeposit;
	}

	public void setTransferToActiveDeposit(Character transferToActiveDeposit) {
		this.transferToActiveDeposit = transferToActiveDeposit;
	}

	public Character getIsNursingAssessmentRequired() {
		return isNursingAssessmentRequired;
	}

	public void setIsNursingAssessmentRequired(Character isNursingAssessmentRequired) {
		this.isNursingAssessmentRequired = isNursingAssessmentRequired;
	}

	public List<PaymentReceiptDetailsDto> getPaymentReceiptDetailsDtosList() {
		return paymentReceiptDetailsDtosList;
	}

	public void setPaymentReceiptDetailsDtosList(List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList) {
		this.paymentReceiptDetailsDtosList = paymentReceiptDetailsDtosList;
	}

	public EncounterMasterDto getEncounterMasterDto() {
		return encounterMasterDto;
	}

	public void setEncounterMasterDto(EncounterMasterDto encounterMasterDto) {
		this.encounterMasterDto = encounterMasterDto;
	}

	public List<OrderDetailsMasterDto> getOrderDetailsList() {
		return orderDetailsList;
	}

	public void setOrderDetailsList(List<OrderDetailsMasterDto> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}

	public List<OTReservationDto> getOtReservationList() {
		return otReservationList;
	}

	public void setOtReservationList(List<OTReservationDto> otReservationList) {
		this.otReservationList = otReservationList;
	}
	
}
