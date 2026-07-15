package com.param.global.mpesa.Dao;

import com.param.global.common.Response;
import com.param.global.mpesa.dto.MpesaTransctionDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IMpesaTransctionDetailsDao {

	Response saveSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)throws ApplicationException;

	Response validateSTKPushPayment(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)throws ApplicationException;

	Response updateSTKPushSimulation(MpesaTransctionDetailsDto mpesaTransctionDetailsDto)throws ApplicationException;

}
