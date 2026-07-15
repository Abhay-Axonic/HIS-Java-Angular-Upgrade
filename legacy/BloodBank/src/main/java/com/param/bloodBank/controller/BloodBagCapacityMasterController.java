package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBagCapacityMasterDto;
import com.param.bloodBank.service.IBloodBagCapacityMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodBagCapacityMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagCapacityMasterController.class);

	@Autowired
	IBloodBagCapacityMasterService ibloodBagCapacityMasterService;

	@RequestMapping(value = "/saveBloodBagCapacityMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBagCapacityMaster(@RequestBody BloodBagCapacityMasterDto bloodBagCapacityMasterDto) {
		try {
			return ibloodBagCapacityMasterService.saveBloodBagCapacityMaster(bloodBagCapacityMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodBagCapacityMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBagCapacityMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBagCapacityMaster(@RequestBody BloodBagCapacityMasterDto bloodBagCapacityMasterDto) {
		try {
			return ibloodBagCapacityMasterService.updateBloodBagCapacityMaster(bloodBagCapacityMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodBagCapacityMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBagCapacityMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBagCapacityMasterStatus(@RequestBody BloodBagCapacityMasterDto bloodBagCapacityMasterDto) {
		try {
			return ibloodBagCapacityMasterService.updateBloodBagCapacityMasterStatus(bloodBagCapacityMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodBagCapacityMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBagCapacityMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodBagCapacityMasterList(@RequestBody BloodBagCapacityMasterDto bloodBagCapacityMasterDto) {
		try {
			return ibloodBagCapacityMasterService.getBloodBagCapacityMasterList(bloodBagCapacityMasterDto);
		} catch (Exception e) {
			logger.error("getBloodBagCapacityMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBagCapacityMasterById/{bloodBagCapacityId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodBagCapacityMasterById(@PathVariable ("bloodBagCapacityId") Integer bloodBagCapacityId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodBagCapacityMasterService.getBloodBagCapacityMasterById(bloodBagCapacityId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodBagCapacityMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodBagCapacityMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodBagCapacityMaster(@PathVariable ("organizationId") Integer organizationId,
													@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodBagCapacityMasterService.getActiveBloodBagCapacityMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodBagCapacityMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBagCapacityMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodBagCapacityMasterCount(@RequestBody BloodBagCapacityMasterDto bloodBagCapacityMasterDto) {
		try {
			return ibloodBagCapacityMasterService.getBloodBagCapacityMasterCount(bloodBagCapacityMasterDto);
		} catch (Exception e) {
			logger.error("getBloodBagCapacityMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

