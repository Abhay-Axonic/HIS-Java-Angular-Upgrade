package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IOTIMSDashboardService {

	Response saveProcedureSchedulingDetails(ProcedureSchedulingDto procedureSchedulingDto, int otreservation) throws ApplicationException;

	Response getProcedureSchedulingDetails(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException;

	Response rescheduleProcedure(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException;

	Response getSurgeriesOfTheDayList(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException;

	Response getProcedureSchedulingSurgeonList(ProcedureStaffScheduleDto dto) throws ApplicationException;

	Response cancelProcedureSchedulingDetails(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getSchedulingByProReqId(ProcedureSchedulingDto dto)throws ApplicationException;

	Response getOtTableByScheduleId(ProcedureSchedulingDto dto)throws ApplicationException;

	Response getOtCheckList(ProcedureSchedulingDto dto)throws ApplicationException;

	Response getPreOparetiveDeta(ProcedureSchedulingDto dto)throws ApplicationException;

}
