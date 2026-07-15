package com.param.global.controller;

import java.net.URI;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitTemplateConfigDto;
import com.param.global.service.ITemplateMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping(value = "/api/global")
public class TemplateController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(TemplateController.class);


	@Autowired
	private ITemplateMasterService iTemplateMasterService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 *  Save Template 
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/template" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTemplate(@RequestBody TemplateMasterDto reqDto){
		try{
			return iTemplateMasterService.saveTemplate(reqDto);
		}catch(Exception e){
		logger.error("saveTemplate failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/** 
	 *  Get Template List by OrgId and Unit Id
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/templates/{orgId}/{unitId}" , method = RequestMethod.GET )
	public Response getTemplateList(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iTemplateMasterService.getTemplateList(orgId, unitId);
		}catch(Exception e){
		logger.error("getTemplateList failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  GET Template BY ID 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/template/{id}" , method = RequestMethod.GET )
	public Response getTemplateById(@PathVariable("id")int id){
		try{
			return iTemplateMasterService.getTemplateById(id);
		}catch(Exception e){
		logger.error("getTemplateById failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  UPDATE Template STATUS 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/template/{id}/{status}" , method = RequestMethod.PATCH)
	public Response updateTemplateStatus(@PathVariable("id")int id, @PathVariable("status")char status){
		try{
			return iTemplateMasterService.updateTemplateStatus(id, status);
		}catch(Exception e){
		logger.error("updateTemplateStatus failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  UPDATE Template  
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/template" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTemplate(@RequestBody TemplateMasterDto dto){
		try{
			return iTemplateMasterService.updateTemplate(dto);
		}catch(Exception e){
		logger.error("updateTemplate failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  GET Template TYPES  
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/template/types")
	public Response getTemplateTypes(){
		try{
			return iTemplateMasterService.getTemplateTypes();
		}catch(Exception e){
		logger.error("getTemplateTypes failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  GET Template LIST BY DOCTOR SPECIALITY 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/coversheet/templates",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateByDoctorOrSpeciality(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getTemplateByDoctorOrSpeciality(templateReqDto);
		}catch(Exception e){
		logger.error("getTemplateByDoctorOrSpeciality failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	/*search sorting template*/
	@RequestMapping(value = "/coversheet/getTemplateBySort",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateBySort(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getTemplateBySort(templateReqDto);
		}catch(Exception e){
		logger.error("getTemplateBySort failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	/*search sorting template*/
	@RequestMapping(value = "/coversheet/getComplateTemplateBySort",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getComplateTemplateBySort(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getComplateTemplateBySort(templateReqDto);
		}catch(Exception e){
		logger.error("getComplateTemplateBySort failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 *  GET Templates For Complaints 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/templates/diagnosis",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplatesForDiagonosis(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getTemplatesForDiagonosis(templateReqDto);
		}catch(Exception e){
		logger.error("getTemplatesForDiagonosis failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/templates/complaints",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplatesForComplaints(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getTemplatesForComplaints(templateReqDto);
		}catch(Exception e){
		logger.error("getTemplatesForComplaints failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/templates/bytype",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplatesByType(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getTemplatesByType(templateReqDto);
		}catch(Exception e){
		logger.error("getTemplatesByType failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/templates/isRenderedingSpecilaity",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response isRenderedingSpecilaity(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.isRenderedingSpecilaity(templateReqDto);
		}catch(Exception e){
		logger.error("isRenderedingSpecilaity failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	
	@RequestMapping(value = "/templates/byService",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTempalteByService(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getTempalteByService(templateReqDto);
		}catch(Exception e){
		logger.error("getTempalteByService failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	
	/**@author Ganesh Chaudhari*/
	@RequestMapping(value = "/templateConfig" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateConfigByUnit(@RequestBody UnitTemplateConfigDto reqDto){
		try{
			return iTemplateMasterService.getTemplateConfigByUnit(reqDto);
		}catch(Exception e){
		logger.error("getTemplateConfigByUnit failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**@author Ganesh Chaudhari*/
	@RequestMapping(value = "/getTemplateListFormEngine" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateListFormEngine(@RequestBody UnitTemplateConfigDto reqDto)
	{
		try{
			Response res = iTemplateMasterService.getTemplateConfigByUnit(reqDto);
			if(res.getObject()!=null) {
				reqDto = (UnitTemplateConfigDto)res.getObject();
				HttpHeaders headers = new HttpHeaders();
				headers.set("X-Api-Key",reqDto.getApiKey()); 
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
				headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
				headers.setContentType(MediaType.APPLICATION_JSON);
				org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(headers);
				URI url= new URI( reqDto.getTemplateUrl()+"getFormList");		 
				ResponseEntity<String> stringResponse = restTemplate.exchange(url,HttpMethod.POST ,request, String.class);
				return new Response<>(SUCCESS, SUCCESS, SUCCESS_MESSAGE, null, stringResponse.getBody());
			}
			return new Response<>(ERROR, ERROR_CODE, "Please configure form engine.", null, null);
		}catch(Exception e){
		logger.error("getTemplateListFormEngine failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/templates/habit",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplatesForHabit(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getTemplatesForHabit(templateReqDto);
		}catch(Exception e){
		logger.error("getTemplatesForHabit failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	
	@RequestMapping(value = "/updateTemplates",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTemplates(@RequestBody TemplateTransactionDto templateTransactionDto){
		try{
			
			return iTemplateMasterService.updateTemplates(templateTransactionDto);
		}catch(Exception e){
		logger.error("updateTemplates failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value = "/coversheet/templates/favourite",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCoversheetTemplateFavourite(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateMasterService.getCoversheetTemplateFavourite(templateReqDto);
		}catch(Exception e){
		logger.error("getCoversheetTemplateFavourite failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
}
