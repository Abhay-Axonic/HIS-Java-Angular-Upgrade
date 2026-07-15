package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodTransfusionPrecheckMasterDto;
import com.param.bloodBank.service.IBloodTransfusionPrecheckMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/bloodBank/precheckMaster")
@SuppressWarnings({"rawtypes"})
public class BloodTransfusionPrecheckMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTransfusionPrecheckMasterController.class);


	@Autowired
	IBloodTransfusionPrecheckMasterService iBloodTransfusionPrecheckMasterService;
	
	@RequestMapping(value = "/saveBloodTransfusionPrecheckMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodTransfusionPrecheckMaster(@RequestBody BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) {
		try {
			return iBloodTransfusionPrecheckMasterService.saveBloodTransfusionPrecheckMaster(bloodTransfusionPrecheckMasterDto	);
		} catch (Exception e) {
			logger.error("saveBloodTransfusionPrecheckMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodTransfusionPrecheckMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodTransfusionPrecheckMasterStatus(@RequestBody BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) {
		try {
			return iBloodTransfusionPrecheckMasterService.updateBloodTransfusionPrecheckMasterStatus(bloodTransfusionPrecheckMasterDto	);
		} catch (Exception e) {
			logger.error("updateBloodTransfusionPrecheckMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodTransfusionPrecheckMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodTransfusionPrecheckMasterList(@RequestBody BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) {
		try {
			return iBloodTransfusionPrecheckMasterService.getBloodTransfusionPrecheckMasterList(bloodTransfusionPrecheckMasterDto	);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBloodTransfusionPrecheckMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveBloodTransfusionPrecheckMasterList(@RequestBody BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) {
		try {
			return iBloodTransfusionPrecheckMasterService.getActiveBloodTransfusionPrecheckMasterList(bloodTransfusionPrecheckMasterDto	);
		} catch (Exception e) {
			logger.error("getActiveBloodTransfusionPrecheckMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodTransfusionPrecheckMasterListPagination", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodTransfusionPrecheckMasterListPagination(@RequestBody BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) {
		try {
			return iBloodTransfusionPrecheckMasterService.getBloodTransfusionPrecheckMasterListPagination(bloodTransfusionPrecheckMasterDto	);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterListPagination failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodTransfusionPrecheckMasterListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodTransfusionPrecheckMasterListCount(@RequestBody BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) {
		try {
			return iBloodTransfusionPrecheckMasterService.getBloodTransfusionPrecheckMasterListCount(bloodTransfusionPrecheckMasterDto	);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
