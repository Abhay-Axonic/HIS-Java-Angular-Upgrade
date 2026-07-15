package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTTransferRequestDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOTTransferRequestDao {

	Response saveOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response getOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response updateScheduleProcedureStatus(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response updateOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response getOTTransferByAdmissionId(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response initOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

}
