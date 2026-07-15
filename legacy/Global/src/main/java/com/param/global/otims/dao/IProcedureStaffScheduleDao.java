package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.global.otims.dto.StaffSchedulingDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureStaffScheduleDao {
	Response saveProcedureStaffSchedule(ProcedureStaffScheduleDto procedureStaffScheduleDto)
			throws ApplicationException;

	Response checkStaffUsedSlot(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response getProcedureStaffScheduleById(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException;

	Response saveProcedureStaffSchedule(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getotimsRoleByProReqId(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getRoleTypeByRoleId(ProcedureStaffScheduleDto procedureStaffScheduleDto)throws ApplicationException;

}
