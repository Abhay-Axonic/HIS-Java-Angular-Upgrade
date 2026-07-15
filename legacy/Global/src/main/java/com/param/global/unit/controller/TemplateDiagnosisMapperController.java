package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.TemplateDiagnosisMapperDto;
import com.param.global.unit.service.ITemplateDiagnosisMapperService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/global")
public class TemplateDiagnosisMapperController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(TemplateDiagnosisMapperController.class);

	@Autowired
	private ITemplateDiagnosisMapperService iTemplateDiagnosisMapperService;
	
	/**
	 * SAVE TEMPLATE DIAGNIOSIS MAPPER
	 * @param templateDiagnosisMapperDto
	 * @return
	 */
	@RequestMapping(value = "/saveTemplateDiagnosisMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTemplateDiagnosisMapper(@RequestBody TemplateDiagnosisMapperDto templateDiagnosisMapperDto){
		try{
			return iTemplateDiagnosisMapperService.saveTemplateDiagnosisMapper(templateDiagnosisMapperDto);
		}catch(Exception e){
		logger.error("saveTemplateDiagnosisMapper failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * UPDATE TEMPLATE DIAGNOSIS MAPPER
	 * @param templateDiagnosisMapperDto
	 * @return
	 */
	@RequestMapping(value = "/updateTemplateDiagnosisMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTemplateDiagnosisMapper(@RequestBody TemplateDiagnosisMapperDto templateDiagnosisMapperDto){
		try{
			return iTemplateDiagnosisMapperService.updateTemplateDiagnosisMapper(templateDiagnosisMapperDto);
		}catch(Exception e){
		logger.error("updateTemplateDiagnosisMapper failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * UPDATE TEMPALTE DIAGNOSIS MAPPER STATUS
	 * @param templateId
	 * @param organisationId
	 * @param status
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/updateTemplateDiagnosisMapperStatus/{templateId}/{organisationId}/{unitId}/{status}" , method = RequestMethod.GET)
	public Response updateTemplateDiagnosisMapperStatus(@PathVariable ("templateId") Integer templateId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("status") Character status,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateDiagnosisMapperService.updateTemplateDiagnosisMapperStatus(templateId, organisationId, unitId, status);
		}catch(Exception e){
		logger.error("updateTemplateDiagnosisMapperStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * GET LIST OF TEMPLATE DIAGNOSIS MAPPER 
	 * @param offset
	 * @param noOfRecordsPerPage
	 * @return
	 */
	@RequestMapping(value = "/getTemplateDiagnosisMapperlist/{offset}/{noOfRecordsPerPage}" , method = RequestMethod.GET)
	public Response getTemplateDiagnosisMapperlist(@PathVariable ("offset") Integer offset,
            										@PathVariable ("noOfRecordsPerPage") Integer noOfRecordsPerPage){
		try{
			return iTemplateDiagnosisMapperService.getTemplateDiagnosisMapperlist(offset, noOfRecordsPerPage);
		}catch(Exception e){
		logger.error("getTemplateDiagnosisMapperlist failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * GET TEMPALTE BY TEMPLATE DIAGNOSIS MAPPER ID
	 * @param templateId
	 * @param organisationId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/getTemplateDiagnosisMapperById/{templateId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getTemplateDiagnosisMapperById(@PathVariable ("templateId") Integer templateId,
														  @PathVariable ("organisationId") Integer organisationId,
														  @PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateDiagnosisMapperService.getTemplateDiagnosisMapperById(templateId, organisationId, unitId);
		}catch(Exception e){
		logger.error("getTemplateDiagnosisMapperById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * GET ACTIVE TEMPLATE DIAGNOSIS MAPPER DETAILS
	 * @param templateDiagnosisMapperDto
	 * @return
	 */
	@RequestMapping(value = "/getActiveTemplateDiagnosisMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveTemplateDiagnosisMapper(@RequestBody TemplateDiagnosisMapperDto templateDiagnosisMapperDto){
		try{
			return iTemplateDiagnosisMapperService.getActiveTemplateDiagnosisMapper(templateDiagnosisMapperDto);
		}catch(Exception e){
		logger.error("getActiveTemplateDiagnosisMapper failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * GET TEMPLATE DETAILS BY TEMPLATE ID
	 * @param templateId
	 * @param organisationId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/getTemplateDiagnosisMapperDatailedList/{templateId}/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getTemplateDiagnosisMapperDatailedList(@PathVariable ("templateId") Integer templateId,
			  @PathVariable ("organisationId") Integer organisationId,
			  @PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateDiagnosisMapperService.getTemplateDiagnosisMapperDatailedList(templateId, organisationId, unitId);
		}catch(Exception e){
		logger.error("getTemplateDiagnosisMapperDatailedList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * GET TEMPLATE DIAGNOSIS MAPPER COUNT FOR PAGINATION
	 * @return
	 */
	@RequestMapping(value = "/getTemplateDiagnosisMapperCount" , method = RequestMethod.GET)
	public Response getTemplateDiagnosisMapperCount(){
		try{
			return iTemplateDiagnosisMapperService.getTemplateDiagnosisMapperCount();
		}catch(Exception e){
		logger.error("getTemplateDiagnosisMapperCount failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	/**
	 * GET TEMPLATE LIST TO MAP DIAGNOSIS
	 * @param organisationId
	 * @param unitId
	 * @return
	 */
	@RequestMapping(value = "/getTemplateList/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getTemplateList(@PathVariable ("organisationId") Integer organisationId,
			  @PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateDiagnosisMapperService.getTemplateList(organisationId, unitId);
		}catch(Exception e){
		logger.error("getTemplateList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getListOfDiagnosis/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getListOfDiagnosis(@PathVariable ("organisationId") Integer organisationId,
			  @PathVariable ("unitId") Integer unitId){
		try{
			return iTemplateDiagnosisMapperService.getListOfDiagnosis(organisationId, unitId);
		}catch(Exception e){
		logger.error("getListOfDiagnosis failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

}
