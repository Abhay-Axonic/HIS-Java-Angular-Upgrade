package com.param.billing.cashDesk.service;

import java.util.List;

import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.cashDesk.dto.InterimHandoverMasterDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IInterimHandoverMasterService {
	
	Response saveInterimHandover(InterimHandoverMasterDto interimHandoverMasterDto);

	Response getListOfDenominations(DenominationMasterDto denominationMasterDto);

	Response saveDenominations(List<DenominationDetailsMasterDto> denominationDetailsMasterDto);

	Response getInterimHandoverList(InterimHandoverMasterDto interimHandoverMasterDto);

	//Response validateUser1(InterimHandoverMasterDto interimHandoverMasterDto);

	Response getICHNumberList(InterimHandoverMasterDto interimHandoverMasterDto);

	Response getDenominationDetailsList(DenominationDetailsMasterDto denominationDetailsMasterDto);
	
	Response getChequeList(PaymentReceiptDetailsDto paymentReceiptDetailsDto);

	Response getCardTransactionList(PaymentReceiptDetailsDto paymentDepositDetailsDto);

	Response saveChequeInterimTransaction(InterimHandoverMasterDto interimHandoverMasterDto);

	Response saveCardInterimTransaction(InterimHandoverMasterDto interimHandoverMasterDto);

	Response updateInterimChequeStatus(List<PaymentReceiptDetailsDto> paymentDepositDetailsDto);

	Response updateInterimCardStatus(List<PaymentReceiptDetailsDto> paymentDepositDetailsDto);

	Response updateInterimStatusToComplete(InterimHandoverMasterDto interimHandoverMasterDto) throws ApplicationException;
}
