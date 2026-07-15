package com.param.assetManagement.maintenance.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.assetManagement.maintenance.dto.PreventiveMaintenanceDto;
import com.param.assetManagement.maintenance.service.IPreventiveMaintenanceService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping(value="/api/assetManagement/maintenance")
@SuppressWarnings({"rawtypes"})
public class PreventiveMaintenanceController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PreventiveMaintenanceController.class);

	@Autowired
	private IPreventiveMaintenanceService iPreventiveMaintenanceService;

	@RequestMapping(value = "/saveAssetSchedule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAssetSchedule(@RequestBody List<PreventiveMaintenanceDto> preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.saveAssetSchedule(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error while saving asset schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateAssetSchedule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAssetSchedule(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.updateAssetSchedule(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error while updating asset schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/fetchAssetSchedule", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response fetchAssetSchedule(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto){
		try{
			return iPreventiveMaintenanceService.fetchAssetSchedule(preventiveMaintenanceDto);
		}catch(Exception e){
			logger.error("Error while fetching asset schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/fetchCheckListByAssetScheduleId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response fetchCheckListByAssetScheduleId(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto){
		try{
			return iPreventiveMaintenanceService.fetchCheckListByAssetScheduleId(preventiveMaintenanceDto);
		}catch(Exception e){
			logger.error("Error while fetching checklist by asset schedule id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/addAndUpdateChecklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response addAndUpdateChecklist(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.addAndUpdateChecklist(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error while adding/updating checklist", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/deleteChecklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteChecklist(@RequestBody PreventiveMaintenanceDto dto) {
		try {
			return iPreventiveMaintenanceService.deleteChecklist(dto);
		} catch (Exception e) {
			logger.error("Error while deleting checklist", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	/*@RequestMapping(value = "/saveAssetSchedule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<PreventiveMaintenanceDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, PreventiveMaintenanceDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}*/

	
	@RequestMapping(value = "/getActiveAssestList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveAssestList(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.getActiveAssestList(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while getting active asset list", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAssetDataToView",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAssetDataToView(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.getAssetDataToView(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while getting asset data to view", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAssetDetailsByCriteria", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAssetDetailsByCriteria(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.getAssetDetailsByCriteria(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while getting asset details by criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveChangedSchedule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveChangedSchedule(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.saveChangedSchedule(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while saving changed schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveCompletionDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCompletionDetails(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.saveCompletionDetails(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while saving completion details", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveAssignDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAssignDetails(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.saveAssignDetails(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while saving assign details", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/fetchScheduledCompletionAssignedData",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response fetchScheduledAndCompletionData(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.fetchScheduledAndCompletionData(preventiveMaintenanceDto);

		} catch (Exception e) {
			logger.error("Error while fetching scheduled and completion data", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/saveOrUpdatePMSDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveOrUpdatePMSDetails(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto) {
		try {
			return iPreventiveMaintenanceService.saveOrUpdatePMSDetails(preventiveMaintenanceDto);
		} catch (Exception e) {
			logger.error("Error while saving/updating PMS details", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/fetchPMSByPMSId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response fetchPMSByPMSId(@RequestBody PreventiveMaintenanceDto preventiveMaintenanceDto){
		try{
			return iPreventiveMaintenanceService.fetchPMSByPMSId(preventiveMaintenanceDto);
		}catch(Exception e){
			logger.error("Error while fetching PMS by PMS id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/deleteSpareById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteSpareById(@RequestBody PreventiveMaintenanceDto dto) {
		try {
			return iPreventiveMaintenanceService.deleteSpareById(dto);
		} catch (Exception e) {
			logger.error("Error while deleting spare by id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
