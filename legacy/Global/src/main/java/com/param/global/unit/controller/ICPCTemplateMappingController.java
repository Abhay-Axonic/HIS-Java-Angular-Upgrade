package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.ICPCTemplateMappingDto;
import com.param.global.unit.service.IICPCTemplateMappingService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/global")
public class ICPCTemplateMappingController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ICPCTemplateMappingController.class);

	@Autowired
	private IICPCTemplateMappingService iicpcTemplateMappingService;
	
	@RequestMapping(value = "/saveICPCTemplateMapping" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveICPCTemplateMapping(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.saveICPCTemplateMapping(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("saveICPCTemplateMapping failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateICPCTemplateMapping" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateICPCTemplateMapping(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.updateICPCTemplateMapping(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("updateICPCTemplateMapping failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCTemplateMappingById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getICPCTemplateMappingById(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.getICPCTemplateMappingById(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("getICPCTemplateMappingById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateICPCTemplateMappingStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateICPCTemplateMappingStatus(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.updateICPCTemplateMappingStatus(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("updateICPCTemplateMappingStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCTemplateMappingList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getICPCTemplateMappingList(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.getICPCTemplateMappingList(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("getICPCTemplateMappingList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCTemplateMappingCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getICPCTemplateMappingCount(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.getICPCTemplateMappingCount(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("getICPCTemplateMappingCount failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getListOfICPCSystems" , method = RequestMethod.GET)
	public Response getListOfICPCSystems(){
		try{
			return iicpcTemplateMappingService.getListOfICPCSystems();
		}catch(Exception e){
		logger.error("getListOfICPCSystems failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDetailedListOfICPCSystems" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDetailedListOfICPCSystems(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.getDetailedListOfICPCSystems(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("getDetailedListOfICPCSystems failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDetailedListOfICPCSystemsDescription" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDetailedListOfICPCSystemsDescription(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.getDetailedListOfICPCSystemsDescription(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("getDetailedListOfICPCSystemsDescription failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getICPCSystemsCodesBySystemId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getICPCSystemsCodesBySystemId(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.getICPCSystemsCodesBySystemId(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("getICPCSystemsCodesBySystemId failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getTemplateListForICPC" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTemplateListForICPC(@RequestBody ICPCTemplateMappingDto iCPCTemplateMappingDto){
		try{
			return iicpcTemplateMappingService.getTemplateListForICPC(iCPCTemplateMappingDto);
		}catch(Exception e){
		logger.error("getTemplateListForICPC failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
}
