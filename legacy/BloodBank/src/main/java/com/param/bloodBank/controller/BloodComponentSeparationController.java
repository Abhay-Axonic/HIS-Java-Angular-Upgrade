package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodComponentSeparationDetailsDto;
import com.param.bloodBank.dto.BloodComponentSeparationDto;
import com.param.bloodBank.service.IBloodComponentSeparationService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/blood/component/separation")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodComponentSeparationController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentSeparationController.class);


	@Autowired 
	IBloodComponentSeparationService iBloodComponentSeparationService;
	
	
	@RequestMapping(value = "/saveBloodComponentSeparation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodComponentSeparation(@RequestBody BloodComponentSeparationDto dto) {
		try {
			return iBloodComponentSeparationService.saveBloodComponentSeparation(dto);
		} catch (Exception e) {
			logger.error("saveBloodComponentSeparation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getComponentSeparationListForStockUpdation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getComponentSeparationListForStockUpdation(@RequestBody BloodComponentSeparationDetailsDto dto) {
		try {
			return iBloodComponentSeparationService.getComponentSeparationListForStockUpdation(dto);
		} catch (Exception e) {
			logger.error("getComponentSeparationListForStockUpdation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
