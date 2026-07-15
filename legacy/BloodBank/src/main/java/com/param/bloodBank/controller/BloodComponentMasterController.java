package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodComponentMasterDto;
import com.param.bloodBank.service.IBloodComponentMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodComponentMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentMasterController.class);

	@Autowired
	IBloodComponentMasterService ibloodComponentMasterService;

	@RequestMapping(value = "/saveBloodComponentMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodComponentMaster(@RequestBody BloodComponentMasterDto bloodComponentMasterDto) {
		try {
			return ibloodComponentMasterService.saveBloodComponentMaster(bloodComponentMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodComponentMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodComponentMaster(@RequestBody BloodComponentMasterDto bloodComponentMasterDto) {
		try {
			return ibloodComponentMasterService.updateBloodComponentMaster(bloodComponentMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodComponentMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodComponentMasterStatus(@RequestBody BloodComponentMasterDto bloodComponentMasterDto) {
		try {
			return ibloodComponentMasterService.updateBloodComponentMasterStatus(bloodComponentMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodComponentMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodComponentMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodComponentMasterList(@RequestBody BloodComponentMasterDto bloodComponentMasterDto) {
		try {
			return ibloodComponentMasterService.getBloodComponentMasterList(bloodComponentMasterDto);
		} catch (Exception e) {
			logger.error("getBloodComponentMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodComponentMasterById/{bloodComponentId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodComponentMasterById(@PathVariable ("bloodComponentId") Integer bloodComponentId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodComponentMasterService.getBloodComponentMasterById(bloodComponentId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodComponentMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodComponentMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodComponentMaster(@PathVariable ("organizationId") Integer organizationId,
			                                      @PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodComponentMasterService.getActiveBloodComponentMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodComponentMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodComponentMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodComponentMasterCount(@RequestBody BloodComponentMasterDto bloodComponentMasterDto) {
		try {
			return ibloodComponentMasterService.getBloodComponentMasterCount(bloodComponentMasterDto);
		} catch (Exception e) {
			logger.error("getBloodComponentMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

