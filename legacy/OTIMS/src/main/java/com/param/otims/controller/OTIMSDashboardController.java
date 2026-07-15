package com.param.otims.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.otims.service.IOTIMSDashboardService;

@RestController
@RequestMapping(value = "/api/otims/dashboard")
@SuppressWarnings({"rawtypes"})
public class OTIMSDashboardController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(OTIMSDashboardController.class);

	@Autowired
	IOTIMSDashboardService iOTIMSDashboardService;
	
	@RequestMapping(value = "/saveProcedureSchedulingDetails" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveProcedureSchedulingDetails(@RequestBody ProcedureSchedulingDto procedureSchedulingDto){
		try{
			return iOTIMSDashboardService.saveProcedureSchedulingDetails(procedureSchedulingDto,0);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@RequestMapping(value = "/saveProcedureSchedulingDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<ProcedureSchedulingDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, ProcedureSchedulingDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}
	*/
	@RequestMapping(value = "/getProcedureSchedulingDetails" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureSchedulingDetails(@RequestBody ProcedureSchedulingDto procedureSchedulingDto){
		try{
			return iOTIMSDashboardService.getProcedureSchedulingDetails(procedureSchedulingDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/rescheduleProcedure" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response rescheduleProcedure(@RequestBody ProcedureSchedulingDto procedureSchedulingDto){
		try{
			return iOTIMSDashboardService.rescheduleProcedure(procedureSchedulingDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getSurgeriesOfTheDayList" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSurgeriesOfTheDayList(@RequestBody ProcedureSchedulingDto procedureSchedulingDto){
		try{
			return iOTIMSDashboardService.getSurgeriesOfTheDayList(procedureSchedulingDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getProcedureSchedulingSurgeonList" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureSchedulingSurgeonList(@RequestBody ProcedureStaffScheduleDto dto){
		try{
			return iOTIMSDashboardService.getProcedureSchedulingSurgeonList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getSchedulingByProReqId" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSchedulingByProReqId(@RequestBody ProcedureSchedulingDto dto){
		try{
			return iOTIMSDashboardService.getSchedulingByProReqId(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getOtTableByScheduleId" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOtTableByScheduleId(@RequestBody ProcedureSchedulingDto dto){
		try{
			return iOTIMSDashboardService.getOtTableByScheduleId(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getOtCheckList" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOtCheckList(@RequestBody ProcedureSchedulingDto dto){
		try{
			return iOTIMSDashboardService.getOtCheckList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getPreOparetiveDeta" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPreOparetiveDeta(@RequestBody ProcedureSchedulingDto dto){
		try{
			return iOTIMSDashboardService.getPreOparetiveDeta(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
