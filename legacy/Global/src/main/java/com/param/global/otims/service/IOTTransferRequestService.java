package com.param.global.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTTransferRequestDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IOTTransferRequestService {

	Response saveOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response getOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response updateOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response getOTTransferByAdmissionId(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

	Response initOTTransferRequest(OTTransferRequestDto oTTransferRequestDto)throws ApplicationException;

}
