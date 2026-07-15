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
import com.param.global.otims.dto.SubSequentProcedureConfigDto;
import com.param.global.otims.service.ISubSequentProcedureConfigService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/**
 * CLASS FOR SUBSEQUENT PROCEDURE CONFIGURATION 
 * @author User
 *
 */
@RestController
@RequestMapping(value="/api/otims")
@SuppressWarnings("rawtypes")
public class SubSequentProcedureConfigController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(SubSequentProcedureConfigController.class);

	@Autowired
	ISubSequentProcedureConfigService iSubSequentProcedureConfigService;
	 
	/**
	 * METHOD TO SAVE SUB SEQUENT PROCEDURE CONFIGURATION
	 * @param subSequentProcedureConfigDto
	 * @return
	 */
	@RequestMapping(value="/saveSubSequentProcedureConfig",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveSubSequentProcedureConfig(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.saveSubSequentProcedureConfig(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("saveSubSequentProcedureConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * UPDATE SUBSEQUENT PROCEDURE CONFIGURATION
	 * @param subSequentProcedureConfigDto
	 * @return
	 */
	@RequestMapping(value="/updateSubSequentProcedureConfig",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateSubSequentProcedureConfig(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.updateSubSequentProcedureConfig(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("updateSubSequentProcedureConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * UPDATE SUBSEQUENT PROCEDURE CONFIGURATION STATUS
	 * @param subSequentProcedureConfigDto
	 * @return
	 */
	@RequestMapping(value="/updateSubSequentProcedureConfigStatus",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateSubSequentProcedureConfigStatus(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.updateSubSequentProcedureConfigStatus(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("updateSubSequentProcedureConfigStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * METHOD TO GET SUBSEQUENT PROCEDURE CONFIGURATION LIST
	 * @param subSequentProcedureConfigDto
	 * @return
	 */
	@RequestMapping(value="/getSubSequentProcedureConfigList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSubSequentProcedureConfigList(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.getSubSequentProcedureConfigList(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("getSubSequentProcedureConfigList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * GET SUBSEQUENT PROCEDURE CONFIGURATION BY ID
	 * @param subSequentProcConfigId
	 * @param orgId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value="/getSubSequentProcedureConfigById/{subSequentProcConfigId}/{orgId}/{unitId}",method=RequestMethod.GET)
	public Response getSubSequentProcedureConfigById(@PathVariable("subSequentProcConfigId") Integer subSequentProcConfigId,
													 @PathVariable("orgId") Integer orgId,
													 @PathVariable("unitId") Integer unitId){
		try{
			return iSubSequentProcedureConfigService.getSubSequentProcedureConfigById(subSequentProcConfigId, orgId, unitId);
		}catch(Exception e){
		logger.error("getSubSequentProcedureConfigById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * METHOD TO GET ACTIVE SUBSEQUENT PROCEDURE CONFIGURATION
	 * @param subSequentProcedureConfigDto
	 * @return
	 */
	@RequestMapping(value="/getActiveSubSequentProcedureConfigList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveSubSequentProcedureConfigList(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.getActiveSubSequentProcedureConfigList(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("getActiveSubSequentProcedureConfigList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * METHOD TO GET PROCEDURE CONFIGURATION COUNT
	 * @param subSequentProcedureConfigDto
	 * @return
	 */
	@RequestMapping(value="/getSubSequentProcedureConfigCount",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSubSequentProcedureConfigCount(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.getSubSequentProcedureConfigCount(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("getSubSequentProcedureConfigCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getSubSequentDetailsListById",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSubSequentDetailsListById(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.getSubSequentDetailsListById(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("getSubSequentDetailsListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getSubSequentDetailsListBySpecialityId",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSubSequentDetailsListBySpecialityId(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.getSubSequentDetailsListBySpecialityId(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("getSubSequentDetailsListBySpecialityId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getSubSequentDetailsAndInfoListBySpecialityId",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getSubSequentDetailsAndInfoListBySpecialityId(@RequestBody SubSequentProcedureConfigDto subSequentProcedureConfigDto){
		try{
			return iSubSequentProcedureConfigService.getSubSequentDetailsAndInfoListBySpecialityId(subSequentProcedureConfigDto);
		}catch(Exception e){
		logger.error("getSubSequentDetailsAndInfoListBySpecialityId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
