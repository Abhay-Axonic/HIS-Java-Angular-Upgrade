package com.param.global.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.SurgicalCalendarDetailsDto;
import com.param.global.otims.dto.SurgicalCalendarMasterDto;
import com.param.global.otims.dto.SurgicalCalendarReqDto;
import com.param.global.otims.dto.SurgicalCalenderMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes" })

public interface ISurgicalCalendarMasterDao {

	public Response saveSurgicalCalendarMaster(List<SurgicalCalendarDetailsDto> surgicalCalendarDetailsDtoList)
			throws ApplicationException;
	
	
	public Response listSergicalCalender(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException;
	public Response countSergicalCalender(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException;
	public Response updateStatusByCalenderId(Integer surgicalCalenderId, char status) throws ApplicationException;
	public Response updateSlotStatusCalenderId(Integer surgicalCalenderId, Integer calenderStatusId) throws ApplicationException;
	public Response updateStatusByProcedureDate(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException;
	public Response surgicalCalenderDetails(SurgicalCalendarReqDto  surgicalCalendarReqDto) throws ApplicationException;
	public Response getWeelkyCalenderSlots(SurgicalCalendarReqDto  surgicalCalendarReqDto) throws ApplicationException;
	public Response saveCalenderSlotMpper(SurgicalCalenderMapperDto  dto) throws ApplicationException;

}
