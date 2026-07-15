package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBankMasterDto;
import com.param.bloodBank.service.IBloodBankMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodBankMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankMasterController.class);

	@Autowired
	IBloodBankMasterService ibloodBankMasterService;

	@RequestMapping(value = "/saveBloodBankMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBankMaster(@RequestBody BloodBankMasterDto bloodBankMasterDto) {
		try {
			return ibloodBankMasterService.saveBloodBankMaster(bloodBankMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodBankMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBankMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBankMaster(@RequestBody BloodBankMasterDto bloodBankMasterDto) {
		try {
			return ibloodBankMasterService.updateBloodBankMaster(bloodBankMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodBankMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBankMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBankMasterStatus(@RequestBody BloodBankMasterDto bloodBankMasterDto) {
		try {
			return ibloodBankMasterService.updateBloodBankMasterStatus(bloodBankMasterDto);
		} catch (Exception e) {
			logger.error("updateBloodBankMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBankMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodBankMasterList(@RequestBody BloodBankMasterDto bloodBankMasterDto) {
		try {
			return ibloodBankMasterService.getBloodBankMasterList(bloodBankMasterDto);
		} catch (Exception e) {
			logger.error("getBloodBankMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBankMasterById/{bloodBankId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodBankMasterById(@PathVariable ("bloodBankId") Integer bloodBankId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodBankMasterService.getBloodBankMasterById(bloodBankId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodBankMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodBankMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveBloodBankMaster(@PathVariable ("organizationId") Integer organizationId,
											 @PathVariable ("unitId") Integer unitId) {
		try {
			return ibloodBankMasterService.getActiveBloodBankMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveBloodBankMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodBankMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodBankMasterCount(@RequestBody BloodBankMasterDto bloodBankMasterDto) {
		try {
			return ibloodBankMasterService.getBloodBankMasterCount(bloodBankMasterDto);
		} catch (Exception e) {
			logger.error("getBloodBankMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

