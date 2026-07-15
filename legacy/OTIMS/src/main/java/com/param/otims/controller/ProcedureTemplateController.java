package com.param.otims.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.service.IProcedureTemplateService;

@RestController
@RequestMapping(value = "/api/otims")
@SuppressWarnings({"rawtypes"})
public class ProcedureTemplateController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureTemplateController.class);

	@Autowired
	IProcedureTemplateService iProcedureTemplateService;
	
	@RequestMapping(value = "/saveProcedureConfigTemplate" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveProcedureConfigTemplate(@RequestBody ProcedureConfigurationDto reqDto){
		try{
			return iProcedureTemplateService.saveProcedureConfigTemplate(reqDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getProcedureConfigTemplate" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureConfigTemplate(@RequestBody ProcedureConfigurationDto reqDto){
		try{
			return iProcedureTemplateService.getProcedureConfigTemplate(reqDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getProcedureConfigList/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getProcedureConfigList(@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iProcedureTemplateService.getProcedureConfigList(orgId, unitId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getOTTThetreAndTableByServiceId/{serviceId}/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getOTTThetreAndTableByServiceId(@PathVariable("serviceId")int serviceId,@PathVariable("orgId")int orgId, @PathVariable("unitId")int unitId){
		try{
			return iProcedureTemplateService.getOTTThetreAndTableByServiceId(serviceId,orgId, unitId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getProcedureConfigureOnOTBillingCompletion/{admissionId}/{encounterId}/{serviceId}/{orgId}/{unitId}/{date}/{fromTime}/{toTime}/{toDate}" , method = RequestMethod.GET)
	public Response getProcedureConfigureOnOTBillingCompletion(@PathVariable("admissionId")int admissionId,@PathVariable("encounterId")int encounterId,@PathVariable("serviceId")int serviceId,@PathVariable("orgId")int orgId,
			@PathVariable("unitId")int unitId,@PathVariable("date")String date,@PathVariable("fromTime")String fromTime,@PathVariable("toTime")String toTime,@PathVariable("toDate")String toDate){
		try{
			return iProcedureTemplateService.getProcedureConfigureOnOTBillingCompletion(admissionId,encounterId,serviceId,orgId,unitId,date,fromTime,toTime,toDate);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
