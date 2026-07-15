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
import com.param.global.otims.dto.OperationTheatreTypeDto;
import com.param.global.otims.service.IOperationTheatreTypeService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
	@RestController
	@RequestMapping(value="/api/otims")
	@SuppressWarnings({"rawtypes"})
	public class OperationTheatreTypeController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(OperationTheatreTypeController.class);

		@Autowired
		private IOperationTheatreTypeService iOperationTheatreTypeService;
		
		@RequestMapping(value = "/saveOperationTheatreType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response saveOperationTheatreType(@RequestBody OperationTheatreTypeDto operationTheatreTypeDto){
			
			try {
				return iOperationTheatreTypeService.saveOperationTheatreType(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("saveOperationTheatreType failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		@RequestMapping(value = "/updateOperationTheatreType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateOperationTheatreType(@RequestBody OperationTheatreTypeDto operationTheatreTypeDto){
			
			try {
				return iOperationTheatreTypeService.updateOperationTheatreType(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("updateOperationTheatreType failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		
		@RequestMapping(value = "/updateOperationTheatreTypeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateOperationTheatreTypeStatus(@RequestBody OperationTheatreTypeDto operationTheatreTypeDto){
			
			try {
				return iOperationTheatreTypeService.updateOperationTheatreTypeStatus(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("updateOperationTheatreTypeStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		
		@RequestMapping(value = "/getOperationTheatreTypeById/{operationTheatreTypeId}/{orgId}/{unitId}", method = RequestMethod.GET)
		public Response getOperationTheatreTypeById(@PathVariable ("operationTheatreTypeId") Integer operationTheatreTypeId,
													@PathVariable ("orgId") Integer orgId, @PathVariable ("unitId") Integer unitId){
			try {
				return iOperationTheatreTypeService.getOperationTheatreTypeById(operationTheatreTypeId, orgId, unitId);
				
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		
		@RequestMapping(value = "/getOperationTheatreTypeList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getOperationTheatreTypeList(@RequestBody OperationTheatreTypeDto operationTheatreTypeDto){
			
			try {
				return iOperationTheatreTypeService.getOperationTheatreTypeList(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		
		@RequestMapping(value = "/getActiveOperationTheatreTypeList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getActiveOperationTheatreTypeList(@RequestBody OperationTheatreTypeDto operationTheatreTypeDto){
			
			try {
				return iOperationTheatreTypeService.getActiveOperationTheatreTypeList(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("getActiveOperationTheatreTypeList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		
		@RequestMapping(value = "/getOperationTheatreTypeCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getOperationTheatreTypeCount(@RequestBody OperationTheatreTypeDto operationTheatreTypeDto){
			
			try {
				return iOperationTheatreTypeService.getOperationTheatreTypeCount(operationTheatreTypeDto);
				
			} catch (Exception e) {
			logger.error("getOperationTheatreTypeCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
}
