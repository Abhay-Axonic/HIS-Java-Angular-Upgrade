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
import com.param.global.otims.dto.SurgeryResourceConfigByCritertiaReqDto;
import com.param.global.otims.dto.SurgeryResourceConfigDto;
import com.param.global.otims.service.ISurgeryResourceConfigService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/otims")
@SuppressWarnings("rawtypes")
public class SurgeryResourceConfigController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(SurgeryResourceConfigController.class);

	
	@Autowired 
	ISurgeryResourceConfigService iSurgeryResourceConfigService; 
	
	/**
	 * METHOD TO SAVE  SURGERY RESOURCE CONFIGURATION
	 * @param surgeryResourceConfigDto
	 * @return
	 * @throws ApplicationException
	 */
	@RequestMapping(value="/saveSurgeryResourceConfig",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveSurgeryResourceConfig(@RequestBody SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException{
		try{
			return iSurgeryResourceConfigService.saveSurgeryResourceConfig(surgeryResourceConfigDto);
		}catch (Exception e) {
		logger.error("saveSurgeryResourceConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * UPDATE RESOURCE CONFIGURATION
	 * @param surgeryResourceConfigDto
	 * @return
	 * @throws ApplicationException
	 */
	@RequestMapping(value="/updateSurgeryResourceConfig",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateSurgeryResourceConfig(@RequestBody SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException{
		try{
			return iSurgeryResourceConfigService.updateSurgeryResourceConfig(surgeryResourceConfigDto);
		}catch (Exception e) {
		logger.error("updateSurgeryResourceConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * UPDATE RESOURCE CONFIGURATION STATUS
	 * @param surgeryResourceConfigDto
	 * @return
	 * @throws ApplicationException
	 */
	@RequestMapping(value="/updateSurgeryResourceConfigStatus",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateSurgeryResourceConfigStatus(@RequestBody SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException{
		try{
			return iSurgeryResourceConfigService.updateSurgeryResourceConfigStatus(surgeryResourceConfigDto);
		}catch (Exception e) {
		logger.error("updateSurgeryResourceConfigStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * METHOD TO GET RESOURCE CONFIGURATION LIST
	 * @param surgeryResourceConfigDto
	 * @return
	 * @throws ApplicationException
	 */
	@RequestMapping(value="/getSurgeryResourceConfigList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSurgeryResourceConfigList(@RequestBody SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException{
		try{
			return iSurgeryResourceConfigService.getSurgeryResourceConfigList(surgeryResourceConfigDto);
		}catch (Exception e) {
		logger.error("getSurgeryResourceConfigList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * GET RESOURCE CONFIGURATION LIST BY ID
	 * @param surgeryResourceConfigId
	 * @return
	 * @throws ApplicationException
	 */
	@RequestMapping(value="/getSurgeryResourceConfigListById/{surgeryResourceConfigId}",method=RequestMethod.GET)
	public Response getSurgeryResourceConfigListById(@PathVariable("surgeryResourceConfigId") Integer surgeryResourceConfigId) throws ApplicationException{
		try{
			return iSurgeryResourceConfigService.getSurgeryResourceConfigListById(surgeryResourceConfigId);
		}catch (Exception e) {
		logger.error("getSurgeryResourceConfigListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * GET RESOURCE CONFIGURATION LIST  COUNT
	 * @param surgeryResourceConfigDto
	 * @return
	 * @throws ApplicationException
	 */
	@RequestMapping(value="/getSurgeryResourceConfigListCount",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSurgeryResourceConfigListCount(@RequestBody SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException{
		try{
			 return iSurgeryResourceConfigService.getSurgeryResourceConfigListCount(surgeryResourceConfigDto);
		}catch (Exception e) {
		logger.error("getSurgeryResourceConfigListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * GET CONFIGRATION FOR PROCEDURE POSTING
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/proconfig/posting" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getConfigForProcPostingByCriteria(@RequestBody SurgeryResourceConfigByCritertiaReqDto reqDto){
		try{
			 return iSurgeryResourceConfigService.getConfigForProcPostingByCriteria(reqDto);
		}catch (Exception e) {
		logger.error("getConfigForProcPostingByCriteria failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value="/getSurgeryResourceConfigListByProcedureId",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSurgeryResourceConfigListByProcedureList(@RequestBody SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException{
		try{
			return iSurgeryResourceConfigService.getSurgeryResourceConfigListByProcedureId(surgeryResourceConfigDto);
		}catch (Exception e) {
		logger.error("getSurgeryResourceConfigListByProcedureList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

}
