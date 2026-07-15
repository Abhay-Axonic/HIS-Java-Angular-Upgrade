package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOTIMSDashboardDao {

	Response saveProcedureSchedule(ProcedureSchedulingDto procedureSchedulingDto)throws ApplicationException;

	Response inActivePlannedProcedure(Integer proReqId)throws ApplicationException;

	Response getSurgeriesOfTheDayList(ProcedureSchedulingDto procedureSchedulingDto)throws ApplicationException;

	Response getProcedureSchedulingSurgeonList(ProcedureStaffScheduleDto dto)throws ApplicationException;

	Response cancelProcedureSchedule(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getProcedureSchedule(ProcedureSchedulingDto procedureSchedulingDto)throws ApplicationException;

	Response getSchedulingByProReqId(ProcedureSchedulingDto dto)throws ApplicationException;

	Response getOtDeatils(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getProcedureSchedulingSurgeonListForCalender(ProcedureStaffScheduleDto dto)throws ApplicationException;

}
