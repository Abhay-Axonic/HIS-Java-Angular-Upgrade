package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.ARCodeMasterDto;
import com.param.global.billing.service.IARCodeMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/global/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ARCodeMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(ARCodeMasterController.class);

	@Autowired
	IARCodeMasterService iARCodeMasterService;

	@RequestMapping(value = "/saveARCodeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveARCodeMaster(@RequestBody ARCodeMasterDto aRCodeMasterDto) {
		try {
			return iARCodeMasterService.saveARCodeMaster(aRCodeMasterDto);
		} catch (Exception e) {
			logger.error("saveARCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateARCodeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateARCodeMaster(@RequestBody ARCodeMasterDto aRCodeMasterDto) {
		try {
			return iARCodeMasterService.updateARCodeMaster(aRCodeMasterDto);
		} catch (Exception e) {
			logger.error("updateARCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateARCodeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateARCodeMasterStatus(@RequestBody ARCodeMasterDto aRCodeMasterDto) {
		try {
			return iARCodeMasterService.updateARCodeMasterStatus(aRCodeMasterDto);
		} catch (Exception e) {
			logger.error("updateARCodeMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getARCodeMasterList/{orgId}/{unitId}/{offset}/{noOfRecordsPerPage}", method = RequestMethod.GET)
	public Response getARCodeMasterList(@PathVariable ("orgId") Integer orgId,
													@PathVariable ("unitId") Integer unitId,@PathVariable ("offset") Integer offset,
													@PathVariable ("noOfRecordsPerPage") Integer noOfRecordsPerPage) {
		try {
			return iARCodeMasterService.getARCodeMasterList(orgId, unitId, offset, noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getARCodeMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getARCodeMasterById/{aRCodeId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getARCodeMasterById(@PathVariable ("aRCodeId") Integer aRCodeId, @PathVariable ("orgId") Integer orgId,
																	 	@PathVariable ("unitId") Integer unitId) {
		try {
			return iARCodeMasterService.getARCodeMasterById(aRCodeId, orgId, unitId);
		} catch (Exception e) {
			logger.error("getARCodeMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getARCodeMasterCount/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getARCodeMasterCount(@PathVariable ("orgId") Integer orgId,
		 	@PathVariable ("unitId") Integer unitId) {
		try {
			return iARCodeMasterService.getARCodeMasterCount(orgId, unitId);
		} catch (Exception e) {
			logger.error("getARCodeMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getActiveARCodeMaster/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveARCodeMaster(@PathVariable ("orgId") Integer orgId,
			@PathVariable ("unitId") Integer unitId) {
		try {
			return iARCodeMasterService.getActiveARCodeMaster(orgId,unitId);
		} catch (Exception e) {
			logger.error("getActiveARCodeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
