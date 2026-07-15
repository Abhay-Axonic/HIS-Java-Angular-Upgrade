package com.param.global.otims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OperationTheatreDto;
import com.param.global.otims.service.IOperationTheatreService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/otims")
@SuppressWarnings({"rawtypes"})
public class OperationTheatreController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(OperationTheatreController.class);

	
	@Autowired
	IOperationTheatreService iOperationTheatreService;
	
	@RequestMapping(value="/saveOperationTheatre", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveOperationTheatre(@RequestBody OperationTheatreDto operationTheatreDto){
		try{
			return iOperationTheatreService.saveOperationTheatre(operationTheatreDto);
		}catch (Exception e) {
		logger.error("saveOperationTheatre failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateOperationTheatre", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateOperationTheatre(@RequestBody OperationTheatreDto operationTheatreDto){
		try{
			return iOperationTheatreService.updateOperationTheatre(operationTheatreDto);
		}catch (Exception e) {
		logger.error("updateOperationTheatre failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateOperationTheatreStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateOperationTheatreStatus(@RequestBody OperationTheatreDto operationTheatreDto){
		try{
			return iOperationTheatreService.updateOperationTheatreStatus(operationTheatreDto);
		}catch (Exception e) {
		logger.error("updateOperationTheatreStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getOperationTheatreList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getOperationTheatreList(@RequestBody OperationTheatreDto operationTheatreDto){
		try{
			return iOperationTheatreService.getOperationTheatreList(operationTheatreDto);
		}catch (Exception e) {
		logger.error("getOperationTheatreList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getOperationTheatreById/{operationTheatreId}/{orgId}/{unitId}", method=RequestMethod.GET)
	public Response getOperationTheatreById(@PathVariable ("operationTheatreId") Integer operationTheatreId,
											@PathVariable ("orgId") Integer orgId,
											@PathVariable ("unitId") Integer unitId){
		try{
			return iOperationTheatreService.getOperationTheatreById(operationTheatreId, orgId, unitId);
		}catch (Exception e) {
		logger.error("getOperationTheatreById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getOperationTheatreCount", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getOperationTheatreCount(@RequestBody OperationTheatreDto operationTheatreDto){
		try{
			return iOperationTheatreService.getOperationTheatreCount(operationTheatreDto);
		}catch (Exception e) {
		logger.error("getOperationTheatreCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getActiveOperationTheatreList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveOperationTheatreList(@RequestBody OperationTheatreDto operationTheatreDto){
		try{
			return iOperationTheatreService.getActiveOperationTheatreList(operationTheatreDto);
		}catch (Exception e) {
		logger.error("getActiveOperationTheatreList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getOperationTheatreByTypeId/{operationTheatreTypeId}/{orgId}/{unitId}", method=RequestMethod.GET)
	public Response getOperationTheatreByTypeId(@PathVariable ("operationTheatreTypeId") Integer operationTheatreTypeId,
											@PathVariable ("orgId") Integer orgId,
											@PathVariable ("unitId") Integer unitId){
		try{
			return iOperationTheatreService.getOperationTheatreByTypeId(operationTheatreTypeId, orgId, unitId);
		}catch (Exception e) {
		logger.error("getOperationTheatreByTypeId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
