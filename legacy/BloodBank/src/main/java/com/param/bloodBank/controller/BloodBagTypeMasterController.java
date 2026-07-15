package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBagTypeMasterDto;
import com.param.bloodBank.service.IBloodBagTypeMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodBagTypeMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagTypeMasterController.class);

	@Autowired
	IBloodBagTypeMasterService ibloodBagTypeMasterService;

	@RequestMapping(value = "/saveBloodBagTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBagTypeMaster(@RequestBody BloodBagTypeMasterDto bloodBagTypeMasterDto) {
		try {
			return ibloodBagTypeMasterService.saveBloodBagTypeMaster(bloodBagTypeMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodBagTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBagTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBagTypeMaster(@RequestBody BloodBagTypeMasterDto bloodBagTypeMasterDto) {
		try {
			return ibloodBagTypeMasterService.updateBloodBagTypeMaster(bloodBagTypeMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodBagTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBagTypeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBagTypeMasterStatus(@RequestBody BloodBagTypeMasterDto bloodBagTypeMasterDto) {
		try {
			return ibloodBagTypeMasterService.updateBloodBagTypeMasterStatus(bloodBagTypeMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodBagTypeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBagTypeMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodBagTypeMasterList(@RequestBody BloodBagTypeMasterDto bloodBagTypeMasterDto) {
		try {
			return ibloodBagTypeMasterService.getBloodBagTypeMasterList(bloodBagTypeMasterDto);
		} catch (Exception e) {
			logger.error("getBloodBagTypeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBagTypeMasterById/{bloodBagTypeId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodBagTypeMasterById(@PathVariable ("bloodBagTypeId") Integer bloodBagTypeId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodBagTypeMasterService.getBloodBagTypeMasterById(bloodBagTypeId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodBagTypeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodBagTypeMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodBagTypeMaster(@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodBagTypeMasterService.getActiveBloodBagTypeMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodBagTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBagTypeMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodBagTypeMasterCount(@RequestBody BloodBagTypeMasterDto bloodBagTypeMasterDto) {
		try {
			return ibloodBagTypeMasterService.getBloodBagTypeMasterCount(bloodBagTypeMasterDto);
		} catch (Exception e) {
			logger.error("getBloodBagTypeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

