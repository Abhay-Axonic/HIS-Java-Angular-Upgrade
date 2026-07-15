package com.param.global.mpesa.Dao;

import com.param.global.billing.dto.PinPadDetailsDto;
import com.param.global.common.Response;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.mpesa.dto.StkCallback;
import com.param.global.mpesa.dto.TransactionResultDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMpesaMasterDao {

	
	Response validateAmountByNumber(MpesaMasterDto mpesaMasterDto);

	Response validateMpesaTrancastion(MpesaMasterDto mpesaMasterDto);

	Response savePinPanDetaild(PinPadDetailsDto pinPadDetailsDto) throws ApplicationException;

	Response updatePinpadDetails(PinPadDetailsDto pinPadDetailsDto)throws ApplicationException;

	Response saveTransactionDate(TransactionResultDto dto)throws ApplicationException;

	Response getTransactionData(String mpesaTxcode)throws ApplicationException;

	Response updateTransactionDate(StkCallback stkDto)throws ApplicationException;

	Response saveValidationData(StkCallback stkDto)throws ApplicationException;

	Response updateRazorPay(String transactionNo)throws ApplicationException;

	Response updateCCAvenue(String transactionNo, Integer billId)throws ApplicationException;

	

}
