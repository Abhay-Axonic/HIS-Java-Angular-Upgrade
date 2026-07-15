package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.EmployeeCategoryMasterDto;
import com.param.global.billing.service.IEmployeeCategoryMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.UserRoleMapperDto;

@RestController
@RequestMapping("/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmployeeCategoryMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(EmployeeCategoryMasterController.class);

	@Autowired
	IEmployeeCategoryMasterService iEmployeeCategoryMasterService;

	@RequestMapping(value = "/saveEmployeeCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveEmployeeCategoryMaster(
			@RequestBody EmployeeCategoryMasterDto employeeCategoryMasterDto) {
		try {
			return iEmployeeCategoryMasterService.saveEmployeeCategoryMaster(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("saveEmployeeCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getEmployeeCategoryMasterById/{employeeId}/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getEmployeeCategoryMasterById(@PathVariable("employeeId") int employeeId,
			@PathVariable("orgId") int orgId) {
		try {
			return iEmployeeCategoryMasterService.getEmployeeCategoryMasterById(employeeId, orgId);
		} catch (Exception e) {
			logger.error("getEmployeeCategoryMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	//user for active only
	@RequestMapping(value = "/getEmployeeCategoryMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEmployeeCategoryMasterList(@RequestBody EmployeeCategoryMasterDto employeeCategoryMasterDto) {
		try {
			return iEmployeeCategoryMasterService.getEmployeeCategoryMasterList(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getEmployeeCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@RequestMapping(value = "/getActiveEmployeeCategoryMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveEmployeeCategoryMasterList(@RequestBody EmployeeCategoryMasterDto employeeCategoryMasterDto) {
		try {
			return iEmployeeCategoryMasterService.getActiveEmployeeCategoryMasterList(employeeCategoryMasterDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null, e.getMessage());
		}
	}*/

	@RequestMapping(value = "/updateEmployeeCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEmployeeCategoryMaster(
			@RequestBody EmployeeCategoryMasterDto employeeCategoryMasterDto) {
		try {
			return iEmployeeCategoryMasterService.updateEmployeeCategoryMaster(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateEmployeeCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateEmployeeCategoryMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEmployeeCategoryMasterStatus(
			@RequestBody EmployeeCategoryMasterDto employeeCategoryMasterDto) {
		try {
			return iEmployeeCategoryMasterService.updateEmployeeCategoryMasterStatus(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateEmployeeCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getEmployeeCategoryCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEmployeeCategoryCount(
			@RequestBody EmployeeCategoryMasterDto employeeCategoryMasterDto) {
		try {
			return iEmployeeCategoryMasterService.getEmployeeCategoryCount(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getEmployeeCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllEmployeeCategoryMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllEmployeeCategoryMasterList(@RequestBody EmployeeCategoryMasterDto employeeCategoryMasterDto) {
		try {
			return iEmployeeCategoryMasterService.getAllEmployeeCategoryMasterList(employeeCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getAllEmployeeCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}
