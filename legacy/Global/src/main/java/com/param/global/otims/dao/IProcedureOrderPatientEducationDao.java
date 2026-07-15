package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderConsentDto;
import com.param.global.otims.dto.ProcedureOrderPatientEducationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderPatientEducationDao {

	Response getPatientEducationDetailsByProcedureOrderId(ProcedureOrderConsentDto dto) throws ApplicationException;

	Response saveProcedureOrderPatientEducation(ProcedureOrderPatientEducationDto procedureOrderPatientEducationDto,
			int proReqId, Integer procedureOrderId) throws ApplicationException;

	Response cancelProcedureOrderPatientEducation(OTReservationDto oTReservationdto)throws ApplicationException;

}
