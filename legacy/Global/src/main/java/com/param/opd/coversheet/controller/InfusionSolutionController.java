package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.InfusionSolutionDto;
import com.param.opd.coversheet.service.IInfusionSolutionService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/opd")
@SuppressWarnings({"rawtypes"})
public class InfusionSolutionController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(InfusionSolutionController.class);

	@Autowired
	private IInfusionSolutionService iInfusionSolutionService;
	
	@RequestMapping(value = "/saveInfusionSolution", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveInfusionSolution(@RequestBody  InfusionSolutionDto  InfusionSolutionDto){
		
		try {
			return iInfusionSolutionService.saveInfusionSolution( InfusionSolutionDto);
			
		} catch (Exception e) {
		logger.error("saveInfusionSolution failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getInfusionSolutionItemList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getInfusionSolutionItemList(@RequestBody  InfusionSolutionDto  InfusionSolutionDto){
		
		try {
			return iInfusionSolutionService.getInfusionSolutionItemList( InfusionSolutionDto);
			
		} catch (Exception e) {
		logger.error("getInfusionSolutionItemList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getInfusionAdditiveItemList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getInfusionAdditiveItemList(@RequestBody  InfusionSolutionDto  InfusionSolutionDto){
		
		try {
			return iInfusionSolutionService.getInfusionAdditiveItemList( InfusionSolutionDto);
			
		} catch (Exception e) {
		logger.error("getInfusionAdditiveItemList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	@RequestMapping(value = "/getListOfInfusion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListOfInfusion(@RequestBody  InfusionSolutionDto  InfusionSolutionDto){
		
		try {
			return iInfusionSolutionService.getListOfInfusion( InfusionSolutionDto);
			
		} catch (Exception e) {
		logger.error("getListOfInfusion failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAdditiveListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAdditiveListById(@RequestBody  InfusionSolutionDto  InfusionSolutionDto){
		
		try {
			return iInfusionSolutionService.getAdditiveListById( InfusionSolutionDto);
			
		} catch (Exception e) {
		logger.error("getAdditiveListById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	
	@RequestMapping(value = "/updateInfusionStopFlag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateInfusionStopFlag(@RequestBody  InfusionSolutionDto  infusionSolutionDto){
		try {
			return iInfusionSolutionService.updateInfusionStopFlag(infusionSolutionDto);
			
		} catch (Exception e) {
		logger.error("updateInfusionStopFlag failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
