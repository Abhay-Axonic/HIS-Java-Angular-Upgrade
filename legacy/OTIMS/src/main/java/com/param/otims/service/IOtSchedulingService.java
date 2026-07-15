package com.param.otims.service;

import javax.transaction.Transactional;
import com.param.global.common.Response;
import com.param.global.otims.dto.OtSchedulingDto;
import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IOtSchedulingService {

	Response saveOtScheduling(OtSchedulingDto otSchedulingDto) throws ApplicationException;

	Response getOtScheduling(OtSchedulingDto otSchedulingDto)throws ApplicationException;

	Response deactivateOtSchedule(OtSchedulingDto otSchedulingDto)throws ApplicationException;

	Response toCheckSlotAvailability(OtSchedulingDto otSchedulingDto)throws ApplicationException;

	Response getOtSchedulingForOTcalender(OtSchedulingDto otSchedulingDto)throws ApplicationException;

}
