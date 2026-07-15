package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CorporateEmployeeMasterDto;
import com.param.global.service.ICorporateEmployeeMasterService;

@RestController
@RequestMapping(value="/api/global/")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CorporateEmployeeMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorporateEmployeeMasterController.class);

	@Autowired
	ICorporateEmployeeMasterService iCorporateEmployeeMasterService;


	@RequestMapping(value = "/saveCorporateEmployeeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCorporateEmployeeMaster(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.saveCorporateEmployeeMaster(dto);
		} catch (Exception e) {
			logger.error("Error while saving the corporate employee master", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getCorporateEmployeeMasterDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCorporateEmployeeMasterDetails(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.getCorporateEmployeeMasterDetails(dto);
		} catch (Exception e) {
			logger.error("Error while fetching the corporate employee master details", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getCorporateEmployeeDependantsDetailsByEmpId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCorporateEmployeeDependantsDetails(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.getCorporateEmployeeDependantsDetails(dto);
		} catch (Exception e) {
			logger.error("Error while fetching the corporate employee dependants details", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getCorporateEmployeeMasterDetailsById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCorporateEmployeeMasterDetailsById(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.getCorporateEmployeeMasterDetailsById(dto);
		} catch (Exception e) {
			logger.error("Error while fetching the corporate employee master details by id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/updateCorporateEmployeeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCorporateEmployeeMaster(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.updateCorporateEmployeeMaster(dto);
		} catch (Exception e) {
			logger.error("Error while updating the corporate employee master", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/updateCorporateEmployeeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCorporateEmployeeMasterStatus(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.updateCorporateEmployeeMasterStatus(dto);
		} catch (Exception e) {
			logger.error("Error while updating the corporate employee master status", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/searchCorporateEmployee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchCorporateEmployee(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.searchCorporateEmployee(dto);
		} catch (Exception e) {
			logger.error("Error while searching corporate employee", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/verifyCorporateEmployee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response verifyCorporateEmployee(
			@RequestBody CorporateEmployeeMasterDto dto) {
		try {
			return iCorporateEmployeeMasterService.verifyCorporateEmployee(dto);
		} catch (Exception e) {
			logger.error("Error while verifying corporate employee", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
