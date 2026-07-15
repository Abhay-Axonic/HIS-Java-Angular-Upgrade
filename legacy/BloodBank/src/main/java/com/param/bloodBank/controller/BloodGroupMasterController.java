package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodGroupMasterDto;
import com.param.bloodBank.service.IBloodGroupMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodGroupMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodGroupMasterController.class);

	@Autowired
	IBloodGroupMasterService iBloodGroupMasterService;

	@RequestMapping(value = "/saveBloodGroupMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodGroupMaster(@RequestBody BloodGroupMasterDto bloodGroupMasterDto) {
		try {
			return iBloodGroupMasterService.saveBloodGroupMaster(bloodGroupMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodGroupMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodGroupMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodGroupMaster(@RequestBody BloodGroupMasterDto bloodGroupMasterDto) {
		try {
			return iBloodGroupMasterService.updateBloodGroupMaster(bloodGroupMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodGroupMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodGroupMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodGroupMasterStatus(@RequestBody BloodGroupMasterDto bloodGroupMasterDto) {
		try {
			return iBloodGroupMasterService.updateBloodGroupMasterStatus(bloodGroupMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodGroupMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodGroupMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodGroupMasterList(@RequestBody BloodGroupMasterDto bloodGroupMasterDto) {
		try {
			return iBloodGroupMasterService.getBloodGroupMasterList(bloodGroupMasterDto);
		} catch (Exception e) {
			logger.error("getBloodGroupMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodGroupMasterById/{bloodGroupId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodGroupMasterById(@PathVariable ("bloodGroupId") Integer bloodGroupId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return iBloodGroupMasterService.getBloodGroupMasterById(bloodGroupId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodGroupMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodGroupMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodGroupMaster(@PathVariable ("organizationId") Integer organizationId,
											 @PathVariable ("unitId") Integer unitId) {
		try {
			return iBloodGroupMasterService.getActiveBloodGroupMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodGroupMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodGroupMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodGroupMasterCount(@RequestBody BloodGroupMasterDto bloodGroupMasterDto) {
		try {
			return iBloodGroupMasterService.getBloodGroupMasterCount(bloodGroupMasterDto);
		} catch (Exception e) {
			logger.error("getBloodGroupMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

