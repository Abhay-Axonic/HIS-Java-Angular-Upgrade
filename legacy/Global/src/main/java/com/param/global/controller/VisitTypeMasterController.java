package com.param.global.controller;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.VisitTypeMasterDto;
import com.param.global.service.IVisitTypeMasterService;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VisitTypeMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(VisitTypeMasterController.class);

	@Autowired
	private IVisitTypeMasterService iVisitTypeMasterService;

	@RequestMapping(value = "/visitTypeMaster/unit/{unitId}/org/{orgId}", method = RequestMethod.GET)
	public Response getActiveVisitTypeMaster(@PathVariable("unitId") Integer unitId,
			@PathVariable("orgId") Integer orgId) {
		try {

				return iVisitTypeMasterService.getActiveVisitTypeList(unitId, orgId);

		} catch (Exception e) {
		logger.error("getActiveVisitTypeMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/saveVisitType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVisitType(@RequestBody VisitTypeMasterDto visitTypeMasterDto) {
		try {
			return iVisitTypeMasterService.saveVisitType(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("saveVisitType failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "/getVisitTypeById/visitTypeId/{visitId}/orgId/{orgId}", method = RequestMethod.GET)
	public Response getVisitTypeById(@PathVariable("visitId") Integer visitId,@PathVariable("orgId") Integer orgId) {
		try {
			return iVisitTypeMasterService.getVisitTypeById(visitId,orgId);
		} catch (Exception e) {
		logger.error("getVisitTypeById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "/updateVisitType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVisitType(@RequestBody VisitTypeMasterDto visitTypeMasterDto) {
		try {
			return iVisitTypeMasterService.updateVisitType(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("updateVisitType failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "/updateVisitTypeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVisitTypeStatus(@RequestBody VisitTypeMasterDto visitTypeMasterDto) {
		try {
			return iVisitTypeMasterService.updateVisitTypeStatus(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("updateVisitTypeStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/visitTypeMasterListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response visitTypeMasterListCount(@RequestBody VisitTypeMasterDto visitTypeMasterDto) {
		try {
			return iVisitTypeMasterService.getVisitTypeCount(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("visitTypeMasterListCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "/visitTypeMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response visitTypeMasterList(@RequestBody VisitTypeMasterDto visitTypeMasterDto) {
		try {
			return iVisitTypeMasterService.getVisitTypeList(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("visitTypeMasterList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
