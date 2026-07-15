package com.param.global.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderConsentDao {
	
	Response getConsentDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException;

	Response saveProcedureOrderConsent(List<ProcedureOrderConsentDto> procedureOrderConsentList, int proReqId,
			Integer procedureOrderId)throws ApplicationException;

	Response cancelProcedureOrderConsent(OTReservationDto oTReservationdto)throws ApplicationException;

	
}
