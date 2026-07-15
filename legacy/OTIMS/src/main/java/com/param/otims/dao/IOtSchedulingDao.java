package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtSchedulingDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IOtSchedulingDao {

	Response saveOtScheduling(List<OtSchedulingDto> list)throws ApplicationException ;

	Response getOtScheduling(OtSchedulingDto otSchedulingDto)throws ApplicationException;

	Response deactivateOtSchedule(OtSchedulingDto otSchedulingDto)throws ApplicationException;

	Response checkOtScheduling(OtSchedulingDto dto)throws ApplicationException;

	Response toCheckSlotAvailability(OtSchedulingDto otSchedulingDto)throws ApplicationException;

	Response getOtSchedulingForOTcalender(OtSchedulingDto otSchedulingDto)throws ApplicationException;

}
