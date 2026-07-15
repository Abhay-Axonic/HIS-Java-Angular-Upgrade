package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodDonorTypeMasterDto;
import com.param.bloodBank.service.IBloodDonorTypeMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodDonorTypeMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodDonorTypeMasterController.class);


	@Autowired
	IBloodDonorTypeMasterService ibloodDonorTypeMasterService;

	@RequestMapping(value = "/saveBloodDonorTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodDonorTypeMaster(@RequestBody BloodDonorTypeMasterDto bloodDonorTypeMasterDto) {
		try {
			return ibloodDonorTypeMasterService.saveBloodDonorTypeMaster(bloodDonorTypeMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodDonorTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodDonorTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodDonorTypeMaster(@RequestBody BloodDonorTypeMasterDto bloodDonorTypeMasterDto) {
		try {
			return ibloodDonorTypeMasterService.updateBloodDonorTypeMaster(bloodDonorTypeMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodDonorTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodDonorTypeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodDonorTypeMasterStatus(@RequestBody BloodDonorTypeMasterDto bloodDonorTypeMasterDto) {
		try {
			return ibloodDonorTypeMasterService.updateBloodDonorTypeMasterStatus(bloodDonorTypeMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodDonorTypeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodDonorTypeMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodDonorTypeMasterList(@RequestBody BloodDonorTypeMasterDto bloodDonorTypeMasterDto) {
		try {
			return ibloodDonorTypeMasterService.getBloodDonorTypeMasterList(bloodDonorTypeMasterDto);
		} catch (Exception e) {
			logger.error("getBloodDonorTypeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodDonorTypeMasterById/{bloodDonorTypeId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodDonorTypeMasterById(@PathVariable ("bloodDonorTypeId") Integer bloodDonorTypeId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodDonorTypeMasterService.getBloodDonorTypeMasterById(bloodDonorTypeId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodDonorTypeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodDonorTypeMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodDonorTypeMaster(@PathVariable ("organizationId") Integer organizationId,
												  @PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodDonorTypeMasterService.getActiveBloodDonorTypeMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodDonorTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodDonorTypeMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodDonorTypeMasterCount(@RequestBody BloodDonorTypeMasterDto bloodDonorTypeMasterDto) {
		try {
			return ibloodDonorTypeMasterService.getBloodDonorTypeMasterCount(bloodDonorTypeMasterDto);
		} catch (Exception e) {
			logger.error("getBloodDonorTypeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
