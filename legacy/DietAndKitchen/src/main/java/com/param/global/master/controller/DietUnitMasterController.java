package com.param.global.master.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import com.param.global.master.dto.UnitOfMeasurmentMasterDto;
import com.param.global.master.service.IDietUnitMasterService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/api/diet/master")
public class DietUnitMasterController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(DietUnitMasterController.class);

	@Autowired
	IDietUnitMasterService iDietUnitMasterService;

	@RequestMapping(value = "/saveDietUnitMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDietUnitMaster(@RequestBody UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto) {
		try {
			return iDietUnitMasterService.saveDietUnitMaster(unitOfMeasurmentMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/updateDietUnitMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDietUnitMaster(@RequestBody UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto){
		
		try {
			return iDietUnitMasterService.updateDietUnitMaster(unitOfMeasurmentMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDietUnitMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDietUnitMasterStatus(@RequestBody UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto){
		
		try {
			return iDietUnitMasterService.updateDietUnitMasterStatus(unitOfMeasurmentMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDietUnitMasterById/{uomId}/{organizationId}", method = RequestMethod.GET)
	public Response getDietUnitMasterById(@PathVariable ("uomId") Integer uomId,
									@PathVariable ("organizationId") Integer organizationId){
		
		try {
			return iDietUnitMasterService.getDietUnitMasterById(uomId, organizationId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getDietUnitMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDietUnitMasterList(@RequestBody UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto){

		try {
			return iDietUnitMasterService.getDietUnitMasterList(unitOfMeasurmentMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDietUnitMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDietUnitMasterCount(@RequestBody UnitOfMeasurmentMasterDto unitOfMeasurmentMasterDto){
		
		try {
			return iDietUnitMasterService.getDietUnitMasterCount(unitOfMeasurmentMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveDietUnitMaster/{organizationId}", method = RequestMethod.GET)
	public Response getActiveDietUnitMaster(@PathVariable ("organizationId") Integer organizationId){
		
		try {
			return iDietUnitMasterService.getActiveDietUnitMaster(organizationId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
