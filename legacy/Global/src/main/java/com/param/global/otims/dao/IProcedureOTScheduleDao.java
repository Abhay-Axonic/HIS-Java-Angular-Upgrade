package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.OtSchedulingDto;
import com.param.global.otims.dto.ProcedureOTScheduleDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureOTScheduleDao {
	Response saveProcedureOTSchedule(ProcedureOTScheduleDto procedureOTScheduleDto)throws ApplicationException;

	Response checkUsedSlot(OtSchedulingDto otSchedulingDto)throws ApplicationException;

	Response updateProcedureReqStatus(Integer proReqId, int i, String changeOfDate, String changeOfTime)throws ApplicationException;

	Response getProcedureOTScheduleById(ProcedureSchedulingDto procedureSchedulingDto)
			throws ApplicationException;

	Response saveProcedureOTSchedule(OTReservationDto oTReservationdto)throws ApplicationException;

	Response getOtCheckList(ProcedureSchedulingDto dto)throws ApplicationException;

	Response getPreOparetiveDeta(ProcedureSchedulingDto dto)throws ApplicationException;

	Response getPreOparetiveCheckListDeta(Integer proReqId)throws ApplicationException;

	Response getWheelInDataByProReqId(Integer proReqId)throws ApplicationException;
}
