package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.DonorRejectionMasterDto;
import com.param.bloodBank.service.IDonorRejectionMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DonorRejectionMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorRejectionMasterController.class);

	@Autowired
	IDonorRejectionMasterService idonorRejectionMasterService;

	@RequestMapping(value = "/saveDonorRejectionMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDonorRejectionMaster(@RequestBody DonorRejectionMasterDto donorRejectionMasterDto) {
		try {
			return idonorRejectionMasterService.saveDonorRejectionMaster(donorRejectionMasterDto);
		} catch (Exception e) {
			logger.error("saveDonorRejectionMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDonorRejectionMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDonorRejectionMaster(@RequestBody DonorRejectionMasterDto donorRejectionMasterDto) {
		try {
			return idonorRejectionMasterService.updateDonorRejectionMaster(donorRejectionMasterDto);
		} catch (Exception e) {
			logger.error("updateDonorRejectionMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDonorRejectionMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDonorRejectionMasterStatus(@RequestBody DonorRejectionMasterDto donorRejectionMasterDto) {
		try {
			return idonorRejectionMasterService.updateDonorRejectionMasterStatus(donorRejectionMasterDto);
		} catch (Exception e) {
			logger.error("updateDonorRejectionMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDonorRejectionMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDonorRejectionMasterList(@RequestBody DonorRejectionMasterDto donorRejectionMasterDto) {
		try {
			return idonorRejectionMasterService.getDonorRejectionMasterList(donorRejectionMasterDto);
		} catch (Exception e) {
			logger.error("getDonorRejectionMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDonorRejectionMasterById/{donorRejectionId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getDonorRejectionMasterById(@PathVariable ("donorRejectionId") Integer donorRejectionId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return idonorRejectionMasterService.getDonorRejectionMasterById(donorRejectionId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getDonorRejectionMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveDonorRejectionMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveDonorRejectionMaster(@PathVariable ("organizationId") Integer organizationId,
			@PathVariable ("unitId") Integer unitId) {
		try {
			return idonorRejectionMasterService.getActiveDonorRejectionMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveDonorRejectionMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDonorRejectionMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDonorRejectionMasterCount(@RequestBody DonorRejectionMasterDto donorRejectionMasterDto) {
		try {
			return idonorRejectionMasterService.getDonorRejectionMasterCount(donorRejectionMasterDto);
		} catch (Exception e) {
			logger.error("getDonorRejectionMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

