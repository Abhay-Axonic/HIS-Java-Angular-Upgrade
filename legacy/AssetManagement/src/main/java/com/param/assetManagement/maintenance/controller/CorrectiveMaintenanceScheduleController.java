package com.param.assetManagement.maintenance.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.assetManagement.maintenance.dao.ICorrectiveMaintenanceWorklistDao;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceScheduleDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceSparesComsumptionDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceWorklistDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceAssetReplacement;
import com.param.assetManagement.maintenance.service.ICorrectiveMaintenanceScheduleService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping(value="/api/assetManagement/maintenance")
@SuppressWarnings({"rawtypes"})
public class CorrectiveMaintenanceScheduleController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceScheduleController.class);

	@Autowired
	ICorrectiveMaintenanceScheduleService iCorrectiveMaintenanceScheduleService;

	@Autowired
	ICorrectiveMaintenanceWorklistDao iCorrectiveMaintenanceWorklistDao;

	@RequestMapping(value = "/saveCorrectiveMaintenanceSchedule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCorrectiveMaintenanceSchedule(@RequestBody CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) {
		try {

			 return  iCorrectiveMaintenanceScheduleService.saveCorrectiveMaintenanceSchedule(correctiveMaintenanceScheduleDto);

		} catch (Exception e) {
			logger.error("Error while saving corrective maintenance schedule", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<CorrectiveMaintenanceScheduleDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, CorrectiveMaintenanceScheduleDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}*/

	@RequestMapping(value = "/getCorrectiveMaintenanceScheduleByMultipleCrit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCorrectiveMaintenanceScheduleByMultipleCrit(@RequestBody CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) {
		try {
			return iCorrectiveMaintenanceScheduleService.getCorrectiveMaintenanceScheduleByMultipleCrit(correctiveMaintenanceScheduleDto);

		} catch (Exception e) {
			logger.error("Error while getting corrective maintenance schedule by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCorrectiveMainSparesComsumptionByitemId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCorrectiveMainSparesComsumptionByitemId(@RequestBody CorrectiveMaintenanceSparesComsumptionDto correctiveMaintenanceSparesComsumptionDto) {
		try {
			return iCorrectiveMaintenanceScheduleService.getCorrectiveMainSparesComsumptionByitemId(correctiveMaintenanceSparesComsumptionDto);

		} catch (Exception e) {
			logger.error("Error while getting corrective maintenance spares consumption by item id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getvendorlistByBatchId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getvendorlistByBatchId(@RequestBody CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto) {
		try {
			return iCorrectiveMaintenanceScheduleService.getvendorlistByBatchId(correctiveMaintenanceWorklistDto);

		} catch (Exception e) {
			logger.error("Error while getting vendor list by batch id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getSparesComsumptionlistById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSparesComsumptionlistById(@RequestBody CorrectiveMaintenanceScheduleDto orrectiveMaintenanceScheduleDto) {
		try {
			return iCorrectiveMaintenanceScheduleService.getSparesComsumptionlistById(orrectiveMaintenanceScheduleDto);

		} catch (Exception e) {
			logger.error("Error while getting spares consumption list by id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getreplaceItemById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getreplaceItemById(@RequestBody CorrectiveMaintenanceScheduleDto orrectiveMaintenanceScheduleDto) {
		try {
			return iCorrectiveMaintenanceScheduleService.getreplaceItemById(orrectiveMaintenanceScheduleDto);

		} catch (Exception e) {
			logger.error("Error while getting replace item by id", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getreplaceDetailOnReturnByMultipleCrit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getreplaceDetailOnReturnByMultipleCrit(@RequestBody CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) {
		try {
			return iCorrectiveMaintenanceScheduleService.getreplaceDetailOnReturnByMultipleCrit(correctiveMaintenanceScheduleDto);

		} catch (Exception e) {
			logger.error("Error while getting replace detail on return by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getdetailsOfCM", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getdetailsOfCM(@RequestBody CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) {
		try {
			return iCorrectiveMaintenanceScheduleService.getdetailsOfCM(correctiveMaintenanceScheduleDto);

		} catch (Exception e) {
			logger.error("Error while getting details of corrective maintenance", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/returnAsset", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response returnAsset(@RequestBody CorrectiveMaintenanceAssetReplacement correctiveMaintenanceAssetReplacement) {
		try {
			return iCorrectiveMaintenanceScheduleService.returnAsset(correctiveMaintenanceAssetReplacement);

		} catch (Exception e) {
			logger.error("Error while returning asset", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/rejectAsset", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response rejectAsset(@RequestBody CorrectiveMaintenanceAssetReplacement correctiveMaintenanceAssetReplacement) {
		try {
			return iCorrectiveMaintenanceScheduleService.rejectAsset(correctiveMaintenanceAssetReplacement);

		} catch (Exception e) {
			logger.error("Error while rejecting asset", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
