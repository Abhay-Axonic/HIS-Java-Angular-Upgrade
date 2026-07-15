package com.param.global.mpesa.service;

import com.param.global.billing.dto.PinPadDetailsDto;
import com.param.global.common.Response;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.mpesa.dto.MpesaTransctionDetailsDto;
import com.param.global.mpesa.dto.StkCallback;
import com.param.global.mpesa.dto.TransactionResultDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IMpesaMasterServiceImpl {

	
	Response validateAmountByNumber(MpesaMasterDto mpesaMasterDto);

	Response savePinPanDetaild(PinPadDetailsDto pinPadDetailsDto)throws ApplicationException;

	Response updatePinpadDetails(PinPadDetailsDto pinPadDetailsDto)throws ApplicationException;

	Response requestSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)throws ApplicationException;

	Response validateSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)throws ApplicationException;

	Response saveTransactionDate(TransactionResultDto dto)throws ApplicationException;

	Response updateTransactionDate(StkCallback stkDto)throws ApplicationException;

	Response saveValidationData(StkCallback stkDto)throws ApplicationException;

	//Response updateByNumber(MpesaMasterDto mpesaMasterDto);

}
