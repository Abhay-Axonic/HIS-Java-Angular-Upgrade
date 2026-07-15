package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.EquipmentSchedulingDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureEquipmentScheduleDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureEquipmentScheduleDao {

	Response saveProcedureEquipmentSchedule(ProcedureEquipmentScheduleDto procedureEquipmentScheduleDto)throws ApplicationException;

	Response checkEquipmentUsedSlot(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException;

	Response checkEquipmentMaintenamceStatus(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException;

	Response getProcedureEquipmentScheduleById(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException;

	Response saveProcedureEquipmentSchedule(OTReservationDto oTReservationdto)throws ApplicationException;

}
