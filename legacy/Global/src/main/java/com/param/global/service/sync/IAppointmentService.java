package com.param.global.service.sync;

import com.param.global.common.Response;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAppointmentService {
	public Response confirmAppointment(ScheduleAppointmentReqDto reqDto)throws ApplicationException;

	public Response cancelorRescheduleAppointment(ScheduleAppointmentReqDto reqDto)throws ApplicationException;

	public Response getAppointmentUniqueId(SlotMasterDto slotMasterDtoss)throws ApplicationException;

		
}
