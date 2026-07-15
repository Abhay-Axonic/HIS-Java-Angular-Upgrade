package com.param.global.pesapal.dao;

import com.param.global.common.Response;
import com.param.global.pesapal.dto.PesapalPaymentDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPesaPalIntegrationDao {

	Response getPesaPalAuthorizationData(PesapalPaymentDto reqDto) throws ApplicationException;

	Response savePesaPalPaymentRequestDetails(PesapalPaymentDto reqDto);

	Response updatePesapalTransactionStatus(PesapalPaymentDto reqDto)throws ApplicationException;

	Response getTransactionReferenceIdByTransactionId(PesapalPaymentDto reqDto)throws ApplicationException;

	Response getTransactionDetailByTransactionId(PesapalPaymentDto reqDto)throws ApplicationException;
}
