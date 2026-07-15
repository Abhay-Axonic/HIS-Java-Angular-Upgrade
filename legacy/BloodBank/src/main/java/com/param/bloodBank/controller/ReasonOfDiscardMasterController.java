package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.ReasonOfDiscardMasterDto;
import com.param.bloodBank.service.IReasonOfDiscardMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReasonOfDiscardMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(ReasonOfDiscardMasterController.class);

	@Autowired
	IReasonOfDiscardMasterService ireasonOfDiscardMasterService;

	@RequestMapping(value = "/saveReasonOfDiscardMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveReasonOfDiscardMaster(@RequestBody ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) {
		try {
			return ireasonOfDiscardMasterService.saveReasonOfDiscardMaster(reasonOfDiscardMasterDto);
		} catch (Exception e) {
			logger.error("saveReasonOfDiscardMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateReasonOfDiscardMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateReasonOfDiscardMaster(@RequestBody ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) {
		try {
			return ireasonOfDiscardMasterService.updateReasonOfDiscardMaster(reasonOfDiscardMasterDto);
		} catch (Exception e) {
			logger.error("updateReasonOfDiscardMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateReasonOfDiscardMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateReasonOfDiscardMasterStatus(@RequestBody ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) {
		try {
			return ireasonOfDiscardMasterService.updateReasonOfDiscardMasterStatus(reasonOfDiscardMasterDto);
		} catch (Exception e) {
			logger.error("updateReasonOfDiscardMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getReasonOfDiscardMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReasonOfDiscardMasterList(@RequestBody ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) {
		try {
			return ireasonOfDiscardMasterService.getReasonOfDiscardMasterList(reasonOfDiscardMasterDto);
		} catch (Exception e) {
			logger.error("getReasonOfDiscardMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getReasonOfDiscardMasterById/{reasonOfDiscardId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getReasonOfDiscardMasterById(@PathVariable ("reasonOfDiscardId") Integer reasonOfDiscardId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return ireasonOfDiscardMasterService.getReasonOfDiscardMasterById(reasonOfDiscardId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getReasonOfDiscardMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveReasonOfDiscardMaster/{organizationId/{unitId}", method = RequestMethod.GET)
	public Response getActiveReasonOfDiscardMaster(@PathVariable ("organizationId") Integer organizationId,
												   @PathVariable ("unitId") Integer unitId) {
		try {
			return ireasonOfDiscardMasterService.getActiveReasonOfDiscardMaster(organizationId, unitId);
		} catch (Exception e) {
			logger.error("getActiveReasonOfDiscardMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getReasonOfDiscardMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReasonOfDiscardMasterCount(@RequestBody ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) {
		try {
			return ireasonOfDiscardMasterService.getReasonOfDiscardMasterCount(reasonOfDiscardMasterDto);
		} catch (Exception e) {
			logger.error("getReasonOfDiscardMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllReasonOfDiscardMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllReasonOfDiscardMasterList(@RequestBody ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) {
		try {
			return ireasonOfDiscardMasterService.getAllReasonOfDiscardMasterList(reasonOfDiscardMasterDto);
		} catch (Exception e) {
			logger.error("getAllReasonOfDiscardMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}

