package com.param.global.otims.controller;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.AdditionalOtTimeDto;
import com.param.global.otims.service.IAdditionalOtTimeService;



import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/otims")
public class AdditionalOtTimeController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(AdditionalOtTimeController.class);

	
	@Autowired
	IAdditionalOtTimeService iAdditionalOtTimeService;
	
	@RequestMapping(value = "/additionalOtTime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAdditionalOtTime(@RequestBody AdditionalOtTimeDto additionalOtTimeDto){
		try {
			return iAdditionalOtTimeService.saveAdditionalOtTime(additionalOtTimeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateAdditionalOtTime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAdditionalOtTime(@RequestBody AdditionalOtTimeDto additionalOtTimeDto){
		try {
			return iAdditionalOtTimeService.updateAdditionalOtTime(additionalOtTimeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/additionalOtTime/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getAdditionalOtTimeList(@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId){
		try {
			return iAdditionalOtTimeService.getAdditionalOtTimeList(orgId,unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateStatusAdditionalOtTime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusAdditionalOtTime(@RequestBody AdditionalOtTimeDto additionalOtTimeDto){
		try {
			return iAdditionalOtTimeService.updateStatusAdditionalOtTime(additionalOtTimeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAddtionalRate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAddtionalRate(@RequestBody AdditionalOtTimeDto additionalOtTimeDto){
		try {
			return iAdditionalOtTimeService.getAddtionalRate(additionalOtTimeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}
