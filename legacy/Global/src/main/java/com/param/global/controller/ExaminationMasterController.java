package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ExaminationMasterDto;
import com.param.global.service.IExaminationMasterService;

@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExaminationMasterController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(ExaminationMasterController.class);

	@Autowired
	private IExaminationMasterService iExaminationMasterService;

	@RequestMapping(value = "/saveExaminationMaster",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveExaminationMaster(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.saveExaminationMaster(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while saving the examination master", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateExaminationMaster",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateExaminationMaster(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.updateExaminationMaster(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while updating the examination master", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/updateExaminationMasterStatus",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateExaminationMasterStatus(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.updateExaminationMasterStatus(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while updating the examination master status", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getExaminationMasterById",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getExaminationMasterById(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.getExaminationMasterById(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the examination master by id", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getExaminationMasterList",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getExaminationMasterList(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.getExaminationMasterList(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the examination master list", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getExaminationMasterActiveList",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getExaminationMasterActiveList(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.getExaminationMasterActiveList(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the examination master active list", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getExaminationMasterCount",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getExaminationMasterCount(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.getExaminationMasterCount(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the examination master count", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/searchExaminationMasterActiveList",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchExaminationMasterActiveList(@RequestBody ExaminationMasterDto examinationMasterDto) {
		try {
				return iExaminationMasterService.searchExaminationMasterActiveList(examinationMasterDto);
		} catch (Exception e) {
			logger.error("Error while searching the examination master active list", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
