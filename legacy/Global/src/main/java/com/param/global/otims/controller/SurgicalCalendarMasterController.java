package com.param.global.otims.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.SurgicalCalendarMasterDto;
import com.param.global.otims.dto.SurgicalCalendarReqDto;
import com.param.global.otims.service.ISurgicalCalendarMasterService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/otims")
@SuppressWarnings({"rawtypes","unchecked"})
public class SurgicalCalendarMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(SurgicalCalendarMasterController.class);

	@Autowired
	ISurgicalCalendarMasterService iSurgicalCalendarMasterService; 
	
	@RequestMapping(value="/surgicalCalender", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveSurgicalCalendarMaster(@RequestBody SurgicalCalendarMasterDto surgicalCalendarMasterDto) {
		try {
				return iSurgicalCalendarMasterService.saveSurgicalCalendarMaster(surgicalCalendarMasterDto);
			
		}catch (Exception e) {
		logger.error("saveSurgicalCalendarMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/surgicalCalender/list", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response listSergicalCalender(@RequestBody SurgicalCalendarReqDto surgicalCalendarReqDto) {
		try {
				return iSurgicalCalendarMasterService.listSergicalCalender(surgicalCalendarReqDto);
			
		}catch (Exception e) {
		logger.error("listSergicalCalender failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/surgicalCalender/list/count", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response countSergicalCalender(@RequestBody SurgicalCalendarReqDto surgicalCalendarReqDto) {
		try {
				return iSurgicalCalendarMasterService.countSergicalCalender(surgicalCalendarReqDto);
			
		}catch (Exception e) {
		logger.error("countSergicalCalender failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/surgicalCalender/{surgicalCalenderId}/status/{status}")
	public Response updateStatusByCalenderId(@PathVariable Map<String,String> params) {
		try {
			
			int surgicalCalenderId = Integer.parseInt(params.get("surgicalCalenderId").trim());
			char status = params.get("status").trim().charAt(0);
				return iSurgicalCalendarMasterService.updateStatusByCalenderId(surgicalCalenderId,status);
			
		}catch (Exception e) {
		logger.error("updateStatusByCalenderId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/surgicalCalender/all/status", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusByProcedureDate(@RequestBody SurgicalCalendarReqDto surgicalCalendarReqDto) {
		try {
			
				return iSurgicalCalendarMasterService.updateStatusByProcedureDate(surgicalCalendarReqDto);
			
		}catch (Exception e) {
		logger.error("updateStatusByProcedureDate failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/surgicalCalender/details", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response surgicalCalenderDetails(@RequestBody SurgicalCalendarReqDto surgicalCalendarReqDto) {
		try {
			
				return iSurgicalCalendarMasterService.surgicalCalenderDetails(surgicalCalendarReqDto);
			
		}catch (Exception e) {
		logger.error("surgicalCalenderDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/surgicalCalender/weekly", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getWeelkyCalenderSlots(@RequestBody SurgicalCalendarReqDto surgicalCalendarReqDto) {
		try {
			
				return iSurgicalCalendarMasterService.getWeelkyCalenderSlots(surgicalCalendarReqDto);
			
		}catch (Exception e) {
		logger.error("getWeelkyCalenderSlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
