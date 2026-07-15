package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodTestMasterDto;
import com.param.bloodBank.service.IBloodTestMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodTestMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTestMasterController.class);

	@Autowired
	IBloodTestMasterService iBloodTestMasterService;

	@RequestMapping(value = "/saveBloodTestMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodTestMaster(@RequestBody BloodTestMasterDto bloodTestMasterDto) {
		try {
			return iBloodTestMasterService.saveBloodTestMaster(bloodTestMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodTestMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodTestMaster(@RequestBody BloodTestMasterDto bloodTestMasterDto) {
		try {
			return iBloodTestMasterService.updateBloodTestMaster(bloodTestMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodTestMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodTestMasterStatus(@RequestBody BloodTestMasterDto bloodTestMasterDto) {
		try {
			return iBloodTestMasterService.updateBloodTestMasterStatus(bloodTestMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodTestMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodTestMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodTestMasterList(@RequestBody BloodTestMasterDto bloodTestMasterDto) {
		try {
			return iBloodTestMasterService.getBloodTestMasterList(bloodTestMasterDto);
		} catch (Exception e) {
			logger.error("getBloodTestMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodTestMasterById/{bloodTestId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodTestMasterById(@PathVariable ("bloodTestId") Integer bloodTestId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return iBloodTestMasterService.getBloodTestMasterById(bloodTestId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodTestMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodTestMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodTestMaster(@PathVariable ("organizationId") Integer organizationId,
											 @PathVariable ("unitId") Integer unitId) {
		try {
			return iBloodTestMasterService.getActiveBloodTestMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodTestMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodTestMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodTestMasterCount(@RequestBody BloodTestMasterDto bloodTestMasterDto) {
		try {
			return iBloodTestMasterService.getBloodTestMasterCount(bloodTestMasterDto);
		} catch (Exception e) {
			logger.error("getBloodTestMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

