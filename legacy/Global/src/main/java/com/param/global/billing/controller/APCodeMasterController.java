package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.APCodeMasterDto;
import com.param.global.billing.service.IAPCodeMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;


@RestController
@RequestMapping("/global/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class APCodeMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(APCodeMasterController.class);

	@Autowired
	IAPCodeMasterService iAPCodeMasterService;

	@RequestMapping(value = "/saveAPCodeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAPCodeMaster(@RequestBody APCodeMasterDto aPCodeMasterDto) {
		try {
			return iAPCodeMasterService.saveAPCodeMaster(aPCodeMasterDto);
		} catch (Exception e) {
			logger.error("saveAPCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateAPCodeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAPCodeMaster(@RequestBody APCodeMasterDto aPCodeMasterDto) {
		try {
			return iAPCodeMasterService.updateAPCodeMaster(aPCodeMasterDto);
		} catch (Exception e) {
			logger.error("updateAPCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateAPCodeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAPCodeMasterStatus(@RequestBody APCodeMasterDto aPCodeMasterDto) {
		try {
			return iAPCodeMasterService.updateAPCodeMasterStatus(aPCodeMasterDto);
		} catch (Exception e) {
			logger.error("updateAPCodeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAPCodeMasterList/{orgId}/{unitId}/{offset}/{noOfRecordsPerPage}", method = RequestMethod.GET)
	public Response getAPCodeMasterList(@PathVariable ("orgId") Integer orgId,
													@PathVariable ("unitId") Integer unitId, @PathVariable ("offset") Integer offset,
													@PathVariable ("noOfRecordsPerPage") Integer noOfRecordsPerPage) {
		try {
			return iAPCodeMasterService.getAPCodeMasterList(orgId, unitId,offset, noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getAPCodeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAPCodeMasterById/{aPCodeId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getAPCodeMasterById(@PathVariable ("aPCodeId") Integer aPCodeId ,@PathVariable ("orgId") Integer orgId
			,@PathVariable ("unitId") Integer unitId) {
		try {
			return iAPCodeMasterService.getAPCodeMasterById(aPCodeId,orgId,unitId);
		} catch (Exception e) {
			logger.error("getAPCodeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAPCodeMasterCount/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getAPCodeMasterCount(@PathVariable ("orgId") Integer orgId,
			@PathVariable ("unitId") Integer unitId) {
		try {
			return iAPCodeMasterService.getAPCodeMasterCount(orgId,unitId);
		} catch (Exception e) {
			logger.error("getAPCodeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActiveAPCodeMaster/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveAPCodeMaster(@PathVariable ("orgId") Integer orgId,
			@PathVariable ("unitId") Integer unitId) {
		try {
			return iAPCodeMasterService.getActiveAPCodeMaster(orgId,unitId);
		} catch (Exception e) {
			logger.error("getActiveAPCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
