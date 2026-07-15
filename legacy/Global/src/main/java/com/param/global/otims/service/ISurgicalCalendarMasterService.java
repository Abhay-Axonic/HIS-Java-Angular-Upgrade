package com.param.global.otims.service;


import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.SurgicalCalendarMasterDto;
import com.param.global.otims.dto.SurgicalCalendarReqDto;
import com.param.global.otims.dto.SurgicalCalenderMapperDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})

public interface ISurgicalCalendarMasterService {
	public Response saveSurgicalCalendarMaster(SurgicalCalendarMasterDto  surgicalCalendarMasterDto) throws ApplicationException;
	
	public Response listSergicalCalender(SurgicalCalendarReqDto  surgicalCalendarReqDto) throws ApplicationException;
	public Response countSergicalCalender(SurgicalCalendarReqDto  surgicalCalendarReqDto) throws ApplicationException;
	public Response updateStatusByCalenderId(Integer surgicalCalenderId,char status) throws ApplicationException;
	public Response updateStatusByProcedureDate(SurgicalCalendarReqDto  surgicalCalendarReqDto) throws ApplicationException;
	public Response surgicalCalenderDetails(SurgicalCalendarReqDto  surgicalCalendarReqDto) throws ApplicationException;
	public Response getWeelkyCalenderSlots(SurgicalCalendarReqDto  surgicalCalendarReqDto) throws ApplicationException;
	public Response saveCalenderSlotMpper(SurgicalCalenderMapperDto  dto) throws ApplicationException;
	
	
}
