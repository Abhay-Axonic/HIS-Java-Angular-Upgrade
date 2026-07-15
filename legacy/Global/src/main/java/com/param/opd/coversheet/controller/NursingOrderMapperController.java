package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.opd.coversheet.dto.NursingOrderMapperDto;
import com.param.opd.coversheet.dto.PendingNursingOrderAdministrationDto;
import com.param.opd.coversheet.service.INursingOrderMapperService;

@RestController
@RequestMapping(value="/api/opd")
@SuppressWarnings({"rawtypes"})
public class NursingOrderMapperController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(NursingOrderMapperController.class);

	@Autowired
	private INursingOrderMapperService iNursingOrderMapperService;
	
	@RequestMapping(value = "/saveNursingOrderMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNursingOrderMapper(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		
		try {
			return iNursingOrderMapperService.saveNursingOrderMapper( nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("saveNursingOrderMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateNursingOrderMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNursingOrderMapper(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		
		try {
			return iNursingOrderMapperService.updateNursingOrderMapper( nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("updateNursingOrderMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getNursingOrderMapperById/{nursingOrderMapperId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getNursingOrderMapperById(@PathVariable ("nursingOrderMapperId") Integer nursingOrderMapperId, @PathVariable ("organizationId") Integer organizationId,
																				@PathVariable ("unitId") Integer unitId){
		
		try {
			return iNursingOrderMapperService.getNursingOrderMapperById(nursingOrderMapperId, organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getNursingOrderMapperById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getNursingOrderMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNursingOrderMapperList(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		
		try {
			return iNursingOrderMapperService.getNursingOrderMapperList( nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getNursingOrderMapperList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getNursingOrderMapperListByAdmissionId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNursingOrderMapperListByAdmissionId(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		
		try {
			return iNursingOrderMapperService.getNursingOrderMapperListByAdmissionId( nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getNursingOrderMapperListByAdmissionId failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveNursingOrderMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveNursingOrderMapperList(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		
		try {
			return iNursingOrderMapperService.getActiveNursingOrderMapperList( nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getActiveNursingOrderMapperList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getListOfPatientsOfNursingOrders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListOfNursingOrders(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		try {
			return iNursingOrderMapperService.getListOfNursingOrders(nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getListOfNursingOrders failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateNursingOrdersStatusByNurse", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNursingOrdersStatusByNurse(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		try {
			return iNursingOrderMapperService.updateNursingOrdersStatusByNurse(nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("updateNursingOrdersStatusByNurse failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/updateNursingOrdersStartOrStop", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNursingOrdersStartOrStop(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		try {
			return iNursingOrderMapperService.updateNursingOrdersStartOrStop(nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("updateNursingOrdersStartOrStop failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/pendingOrders/getNurseOrders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNurseOrdersForPendingOrdersScreen(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		try {
			return iNursingOrderMapperService.getNurseOrdersForPendingOrdersScreen(nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getNurseOrdersForPendingOrdersScreen failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/pendingOrders/getNurseOrdersByDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNurseOrdersForPendingOrdersScreenByDate(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		try {
			return iNursingOrderMapperService.getNurseOrdersForPendingOrdersScreenByDate(nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getNurseOrdersForPendingOrdersScreenByDate failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/pendingOrders/getNurseOrdersDateWiseList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNurseOrdersDateWiseList(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		try {
			return iNursingOrderMapperService.getNurseOrdersDateWiseList(nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getNurseOrdersDateWiseList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/pendingOrders/saveActualAdministrationTime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveActualAdministrationTime(@RequestBody  PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto){
		try {
			return iNursingOrderMapperService.saveActualAdministrationTime(pendingNursingOrderAdministrationDto);
			
		} catch (Exception e) {
		logger.error("saveActualAdministrationTime failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/pendingOrders/getPendingNurOrdListByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPendingNurOrdListByPatientId(@RequestBody  PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto){
		try {
			return iNursingOrderMapperService.getPendingNurOrdListByPatientId(pendingNursingOrderAdministrationDto);
			
		} catch (Exception e) {
		logger.error("getPendingNurOrdListByPatientId failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/pendingOrders/getNurseOrdersDateWiseListWebApp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNurseOrdersDateWiseListWebApp(@RequestBody  NursingOrderMapperDto  nursingOrderMapperDto){
		try {
			return iNursingOrderMapperService.getNurseOrdersDateWiseListWebApp(nursingOrderMapperDto);
			
		} catch (Exception e) {
		logger.error("getNurseOrdersDateWiseListWebApp failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
