package com.param.pharmacy.master.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ServiceMasterDto;
import com.param.pharmacy.master.global.dto.equipment.GenericScheduleMasterDto;
import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;
import com.param.pharmacy.master.global.service.equipment.IGenericScheduleMasterService;
import com.param.pharmacy.master.global.service.equipment.IScheduleMasterService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/Pharmacy/global")
public class GenericScheduleMasterController implements ICommonConstants, IError {

	private static final Logger logger = Logger.getLogger(GenericScheduleMasterController.class);

	@Autowired
	private IScheduleMasterService iScheduleMasterService;

	@Autowired
	private IGenericScheduleMasterService iGenericScheduleMasterService;

	@RequestMapping(value = "/listScheduleDesc/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response listScheduleMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<ScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.listScheduleDesc(orgId, unitId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/listserviceCode/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response listserviceCode(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.listserviceCode(orgId, unitId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getServicecodeList/{genericScheduleId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getServicecodeList(
			@PathVariable(value = "genericScheduleId") Integer genericScheduleId,
			@PathVariable(value = "orgId") Integer orgId, @PathVariable(value = "unitId") Integer unitId) {
		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.getServicecodeList(genericScheduleId, orgId, unitId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/serviceInvestigation/{searchKeyword}/{unitId}/{orgId}", method = RequestMethod.GET)
	public Response searchServicesByNameAndCode(@PathVariable("searchKeyword") String searchKeyword,
			@PathVariable("unitId") Integer unitId, @PathVariable("orgId") Integer orgId) {
		try {
			ServiceMasterDto serviceMasterDto = new ServiceMasterDto();
			serviceMasterDto.setSearchKeyword(searchKeyword);
			serviceMasterDto.setUnitId(unitId);
			serviceMasterDto.setOrganizationId(orgId);
			return iGenericScheduleMasterService.searchServicesByNameAndCode(serviceMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/saveGenericScheduleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveGenericScheduleMaster(
			@RequestBody GenericScheduleMasterDto genericScheduleMasterDto) {
		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.saveGenericScheduleMaster(genericScheduleMasterDto);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getGenericScheduleMasterById/{orgId}/{unitId}/{genericScheduleId}", method = RequestMethod.GET)
	public @ResponseBody Response getGenericScheduleMasterById(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId,
			@PathVariable(value = "genericScheduleId") Integer genericScheduleId) {
		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.getGenericScheduleMasterById(orgId, unitId, genericScheduleId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateGenericScheduleMaster", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateGenericScheduleMaster(
			@RequestBody GenericScheduleMasterDto genericScheduleMasterDto) {
		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.updateGenericScheduleMaster(genericScheduleMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/activeInactiveGenericSchedule/{orgId}/{genericScheduleId}/{status}", method = RequestMethod.GET)
	public @ResponseBody Response activeInactiveGenericSchedule(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "genericScheduleId") Integer genericScheduleId,
			@PathVariable(value = "status") Character status) {

		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.activeInactiveGenericSchedule(orgId, genericScheduleId, status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/listGenericScheduleMaster/{orgId}/{unitId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response listGenericScheduleMaster(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId, @PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage) {
		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.listGenericScheduleMaster(orgId, unitId, offset, recordPerPage);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getTotalGenericScheduleMasterRecord/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalGenericScheduleMasterRecord(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "unitId") Integer unitId) {
		Response<GenericScheduleMasterDto, Integer> response = null;
		try {
			response = iGenericScheduleMasterService.getTotalGenericScheduleMasterRecord(orgId, unitId);
			return response;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
