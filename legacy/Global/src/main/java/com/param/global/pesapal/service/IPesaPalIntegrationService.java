package com.param.global.pesapal.service;

import javax.transaction.Transactional;

import com.param.global.pesapal.dto.PesapalPaymentDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IPesaPalIntegrationService {

	Response getPesaPalAuthorizationData(PesapalPaymentDto reqDto) throws ApplicationException;

	Response savePesaPalPaymentRequestDetails(PesapalPaymentDto reqDto) throws ApplicationException;

	Response updatePesapalTransactionStatus(PesapalPaymentDto reqDto)throws ApplicationException;

	Response getTransactionReferenceByTransactionId(PesapalPaymentDto reqDto)throws ApplicationException;

	Response getTransactionDetailByTransactionId(PesapalPaymentDto reqDto)throws ApplicationException;

}
