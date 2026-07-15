package com.param.global.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderChecklistDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderChecklistDao {

	Response getChecklistDetailsByProcedureOrderId(ProcedureOrderConsentDto consentDto) throws ApplicationException;
	
	Response saveProcedureOrderCheckList(List<ProcedureOrderChecklistDto> procedureOrderCheckList, int proReqId,
			Integer procedureOrderId)throws ApplicationException;

	Response cancelProcedureOrderCheckList(OTReservationDto oTReservationdto) throws ApplicationException;



}
