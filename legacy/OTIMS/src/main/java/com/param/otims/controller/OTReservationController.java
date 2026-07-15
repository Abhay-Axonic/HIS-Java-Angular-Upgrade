package com.param.otims.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.otims.service.IOTReservationService;

@RestController
@RequestMapping(value = "/api/otims/OTReservation")
@SuppressWarnings({"rawtypes"})
public class OTReservationController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(OTReservationController.class);

	@Autowired
	IOTReservationService iOTReservationService;
	
	@RequestMapping(value = "/saveOTReservation" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveOTReservation(@RequestBody OTReservationDto OTReservationdto,HttpServletRequest request){
		try{
			Response res =  iOTReservationService.saveOTReservation(OTReservationdto,request);
			if(res.getStatus().equals(SUCCESS)) {
				OTReservationDto dto = (OTReservationDto) res.getObject();
				return iOTReservationService.updateProReqMasterStatus(dto.getProReqId());
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,null);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	
	/*@RequestMapping(value = "/saveOTReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<OTReservationDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, OTReservationDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}
	*/
	
	@RequestMapping(value = "/getEquipmentTyoeAndAssetItem/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getEquipmentTyoeAndAssetItem(@PathVariable("orgId")int orgId,@PathVariable("unitId")int unitId){
		try{
			return iOTReservationService.getEquipmentTyoeAndAssetItem(orgId,unitId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchOTReservation" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchOTReservation(@RequestBody ProcedureOrderMasterDto procedureOrderMasterDto){
		try{
			return iOTReservationService.searchOTReservation(procedureOrderMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}

	@RequestMapping(value = "/searchReservedProcedureName" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchReservedProcedureName(@RequestBody ProcedureOrderMasterDto procedureOrderMasterDto){
		try{
			return iOTReservationService.searchReservedProcedureName(procedureOrderMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/cancelOTReservation" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelOTReservation(@RequestBody OTReservationDto OTReservationdto){
		try{
			
			return iOTReservationService.cancelOTReservation(OTReservationdto);
		
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/getOTReservationListByPatientId" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOTReservationListByPatientId(@RequestBody OTReservationDto OTReservationdto){
		try{
			
			return iOTReservationService.getOTReservationListByPatientId(OTReservationdto);
		
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/getOTReservationListByPatientId/forDeposite" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOTReservationListByPatientIdForDeposite(@RequestBody OTReservationDto OTReservationdto){
		try{
			
			return iOTReservationService.getOTReservationListByPatientIdForDeposite(OTReservationdto);
		
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/getOTReservationForOtCalender" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOTReservationForOtCalender(@RequestBody ProcedureOrderMasterDto procedureOrderMasterDto){
		try{
			return iOTReservationService.getOTReservationForOtCalender(procedureOrderMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		
	}
}
