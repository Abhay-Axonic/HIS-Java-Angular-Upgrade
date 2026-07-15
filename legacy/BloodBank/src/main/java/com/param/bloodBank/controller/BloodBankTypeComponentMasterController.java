package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBankTypeComponentMasterDto;
import com.param.bloodBank.service.IBloodBankTypeComponentMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/bloodBank")
public class BloodBankTypeComponentMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(BloodBankTypeComponentMasterController.class);

	@Autowired
	private IBloodBankTypeComponentMasterService iBloodBankTypeComponentMasterService;
	
	@RequestMapping(value = "/saveBloodBankTypeComponentMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBankTypeComponentMaster(@RequestBody BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto){
		try{
			return iBloodBankTypeComponentMasterService.saveBloodBankTypeComponentMaster(bloodBankTypeComponentMasterDto);
		}catch(Exception e){
			logger.error("saveBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBankTypeComponentMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBankTypeComponentMaster(@RequestBody BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto){
		try{
			return iBloodBankTypeComponentMasterService.updateBloodBankTypeComponentMaster(bloodBankTypeComponentMasterDto);
		}catch(Exception e){
			logger.error("updateBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBankTypeComponentMasterStatus/{bloodBagTypeId}/{organizationId}/{unitId}/{status}" , method = RequestMethod.GET)
	public Response updateBloodBankTypeComponentMasterStatus(@PathVariable ("bloodBagTypeId") Integer bloodBagTypeId,
															 @PathVariable ("organizationId") Integer organizationId,
															 @PathVariable ("status") Character status,
															 @PathVariable ("unitId") Integer unitId){
		try{
			return iBloodBankTypeComponentMasterService.updateBloodBankTypeComponentMasterStatus(bloodBagTypeId, organizationId, status, unitId);
		}catch(Exception e){
			logger.error("updateBloodBankTypeComponentMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBankTypeComponentMasterList/{offset}/{noOfRecordsPerPage}" , method = RequestMethod.GET)
	public Response getBloodBankTypeComponentMasterList(@PathVariable ("offset") Integer offset,
													    @PathVariable ("noOfRecordsPerPage") Integer noOfRecordsPerPage){
		try{
			return iBloodBankTypeComponentMasterService.getBloodBankTypeComponentMasterList(offset, noOfRecordsPerPage);
		}catch(Exception e){
			logger.error("getBloodBankTypeComponentMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBankTypeComponentMasterById/{bloodBagTypeId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodBankTypeComponentMasterById(@PathVariable ("bloodBagTypeId") Integer bloodBagTypeId,
														@PathVariable ("organizationId") Integer organizationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iBloodBankTypeComponentMasterService.getBloodBankTypeComponentMasterById(bloodBagTypeId, organizationId, unitId);
		}catch(Exception e){
			logger.error("getBloodBankTypeComponentMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodBankTypeComponentMaster/{organizationId}/{unitId}" ,  method = RequestMethod.GET)
	public Response getActiveBloodBankTypeComponentMaster(@PathVariable ("organizationId") Integer organizationId,
			                                              @PathVariable ("unitId") Integer unitId){
		try{
			return iBloodBankTypeComponentMasterService.getActiveBloodBankTypeComponentMaster(organizationId, unitId);
		}catch(Exception e){
			logger.error("getActiveBloodBankTypeComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBankTypeComponentMasterCount/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getBloodBankTypeComponentMasterCount(@PathVariable ("organizationId") Integer organizationId,
            											 @PathVariable ("unitId") Integer unitId){
		try{
			return iBloodBankTypeComponentMasterService.getBloodBankTypeComponentMasterCount(organizationId, unitId);
		}catch(Exception e){
			logger.error("getBloodBankTypeComponentMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBagList/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getBloodBagList(@PathVariable ("organizationId") Integer organizationId,
			  @PathVariable ("unitId") Integer unitId){
		try{
			return iBloodBankTypeComponentMasterService.getBloodBagList(organizationId, unitId);
		}catch(Exception e){
			logger.error("getBloodBagList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getBloodComponentList/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getBloodComponentList(@PathVariable ("organizationId") Integer organizationId,
			  @PathVariable ("unitId") Integer unitId){
		try{
			return iBloodBankTypeComponentMasterService.getBloodComponentList(organizationId, unitId);
		}catch(Exception e){
			logger.error("getBloodComponentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDetailedComponentList/{bloodBagTypeId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getDetailedComponentList(@PathVariable ("bloodBagTypeId") Integer bloodBagTypeId,
														@PathVariable ("organizationId") Integer organizationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iBloodBankTypeComponentMasterService.getDetailedComponentList(bloodBagTypeId, organizationId, unitId);
		}catch(Exception e){
			logger.error("getDetailedComponentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
