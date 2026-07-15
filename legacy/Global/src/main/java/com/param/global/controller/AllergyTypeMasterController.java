package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AllergyTypeMasterDto;
import com.param.global.service.IAllergyTypeMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : ALLERGY TYPE MASTER
DATE:12/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AllergyTypeMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(AllergyTypeMasterController.class);


	@Autowired 
	private IAllergyTypeMasterService iAllergyTypeMasterService;

	@RequestMapping(value = "/getAllergyTypeMaster", method = RequestMethod.GET)
	public @ResponseBody Response listOfAllergyTypeMaster(@RequestHeader(value = "unit_id") int unitId,
			@RequestHeader(value = "organization_id") int organizationId) {
		try {
			AllergyTypeMasterDto allergyTypeMasterDto = new AllergyTypeMasterDto();
			allergyTypeMasterDto.setUnitId(unitId);
			allergyTypeMasterDto.setOrganizationId(organizationId);
			return iAllergyTypeMasterService.getListOfAllergyTypeMaster(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "saveAllergyTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAllergyTypeMaster(@RequestBody AllergyTypeMasterDto allergyTypeMasterDto) {
		try {
			return iAllergyTypeMasterService.saveAllergyTypeMaster(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "updateAllergyTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAllergyTypeMaster(@RequestBody AllergyTypeMasterDto allergyTypeMasterDto) {
		try {
			return iAllergyTypeMasterService.updateAllergyTypeMaster(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "updateAllergyTypeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAllergyTypeMasterStatus(@RequestBody AllergyTypeMasterDto allergyTypeMasterDto) {
		try {
			return iAllergyTypeMasterService.updateAllergyTypeMasterStatus(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "getAllergyTypeMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllergyTypeMasterList(@RequestBody AllergyTypeMasterDto allergyTypeMasterDto) {
		try {
			return iAllergyTypeMasterService.getAllergyTypeMasterList(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "getAllergyTypeMasterListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllergyTypeMasterCount(@RequestBody AllergyTypeMasterDto allergyTypeMasterDto) {
		try {
			return iAllergyTypeMasterService.getAllergyTypeMasterListCount(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "getAllergyTypeMasterListById/{allergyTypeId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getAllergyTypeMasterListById(@PathVariable("allergyTypeId") Integer allergyTypeId,
			@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId) {
		try {
			return iAllergyTypeMasterService.getAllergyTypeMasterListById(allergyTypeId,orgId,unitId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
