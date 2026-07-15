package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.dto.ExaminationMapperDto;
import com.param.opd.coversheet.service.IComplaintAppointmentMapperService;
import com.param.opd.coversheet.service.IExaminationMapperService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExaminationMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ExaminationMapperController.class);


	@Autowired
	private IExaminationMapperService iExaminationMapperService;
	
	@RequestMapping(value="/saveExaminationMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveExaminationMapper(@RequestBody ExaminationMapperDto examinationMapperDto) {
		try
		{
			return iExaminationMapperService.saveExaminationMapper(examinationMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateExaminationMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateExaminationMapper(@RequestBody ExaminationMapperDto examinationMapperDto) {
		try
		{
			return iExaminationMapperService.updateExaminationMapper(examinationMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getExaminationMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getExaminationMapperById(@RequestBody ExaminationMapperDto examinationMapperDto) {
		try
		{
			return iExaminationMapperService.getExaminationMapperById(examinationMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getExaminationMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getExaminationMapperList(@RequestBody ExaminationMapperDto examinationMapperDto) {
		try
		{
			return iExaminationMapperService.getExaminationMapperList(examinationMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getMostlyUsedExaminationMapperListFor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMostlyUsedExaminationMapperListFor(@RequestBody ExaminationMapperDto examinationMapperDto) {
		try
		{
			return iExaminationMapperService.getMostlyUsedExaminationMapperListFor(examinationMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/removeExaminationMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response removeExaminationMapper(@RequestBody ExaminationMapperDto examinationMapperDto) {
		try
		{
			return iExaminationMapperService.removeExaminationMapper(examinationMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
